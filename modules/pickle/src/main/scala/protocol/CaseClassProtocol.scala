package scbson.pickle.protocol

import scala.language.implicitConversions
import scala.reflect._

import scutil.core.implicits._
import scutil.lang._

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated {
	def caseObjectFormat[T](singleton:T):Format[T]	=
		Format[T](constant(BsonDocument.empty), constant(singleton))

	def caseClassFormat0[T](apply:()=>T, unapply:T=>Boolean):Format[T]	=
		Format[T](
			(out:T)	=> {
				BsonDocument(Seq.empty)
			},
			(in:BsonValue)	=> {
				val _	= documentMap(in)
				apply()
			}
		)

	def caseClassFormat1[S1:Format,T](apply:S1=>T, unapply:T=>Option[S1])(implicit FN:FieldNames[T]):Format[T]	=
		FN.names match {
			case Vector(k1)	=>
				Format[T](
					(out:T)	=> {
						val fields	= unapplyTotal(unapply, out)
						BsonDocument(Seq(
							k1	-> doWrite[S1](fields)
						))
					},
					(in:BsonValue)	=> {
						val map	= documentMap(in)
						apply(
							doReadUnsafe[S1](map(k1))
						)
					}
				)
			case x =>
				sys error s"unexpected number of field names: ${x.toString}"
		}

	/*
	def caseClassFormat2[S1:Format,S2:Format,T:Fielding](
		apply:(S1,S2)=>T,
		unapply:T=>Option[(S1,S2)]
	):Format[T]	= {
		val Seq(k1,k2)	= Fielder[T]
		new Format[T] {
			def write(out:T):BsonValue	= {
				val fields	= unapply(out).get
				BsonDocument.Var(
					k1	-> doWrite[S1](fields._1),
					k2	-> doWrite[S2](fields._2)
				)
			}
			def read(in:BsonValue):T	= {
				val map	= documentMap(in)
				apply(
					doRead[S1](map(k1)),
					doRead[S2](map(k2))
				)
			}
		}
	}
	*/

	//------------------------------------------------------------------------------

	// TODO simplify - get rid of the ClassTag

	private val typeTag	= ""

	/** uses a field with an empty name for the specific constructor */
	def caseClassSumFormat[T](summands:CaseSummand[T,_]*):Format[T]	=
		sumFormat(summands.toVector map (_.partialFormat))

	private type PartialFormat[T]	= PBijection[T,BsonValue]

	private def sumFormat[T](partials:Seq[PartialFormat[T]]):Format[T]	=
		Format[T](
			(it:T)			=> partials collectFirstSome { _ get it } getOrElse fail("no matching constructor found"),
			(it:BsonValue)	=> partials collectFirstSome { _ set it } getOrElse fail("no matching constructor found")
		)

	object CaseSummand {
		/** DSL for name -> format construction */
		implicit def namedSummand[T,C<:T:ClassTag](pair:(String, Format[C])):CaseSummand[T,C]	=
			CaseSummand(pair._1, pair._2)
	}

	/**
	 * injects the type tag as a field with an empty name into an existing object
	 * NOTE this is not erasure-safe
	 */
	case class CaseSummand[T,C<:T:ClassTag](identifier:String, format:Format[C]) {
		private val tag		= {
			val origTag	= classTag[C]
			val	rtClass	= origTag.runtimeClass
			if (rtClass.isPrimitive)	ClassTag(rtClass.boxed)
			else						origTag
		}

		// TODO this is just a regular write guarded by the possibility of a downcast, which is essentially an isInstanceOf
		private def write(value:T):Option[BsonValue]	=
			tag unapply value map { (it:C) =>
				BsonDocument.Var(typeTag -> BsonString(identifier)) ++
				downcast[BsonDocument](format get it)
			}

		private def read(bson:BsonValue):Option[T]	=
			documentValue(bson)
			.exists	{ _ == ((typeTag, BsonString(identifier))) }
			.option	{ format set bson }

		def partialFormat:PartialFormat[T]	=
			PBijection(write, read)
	}
}
