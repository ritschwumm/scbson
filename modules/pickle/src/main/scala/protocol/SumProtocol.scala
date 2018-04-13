package scbson.pickle.protocol

import scala.reflect._

import scutil.base.implicits._
import scutil.lang._

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object SumProtocol extends SumProtocol

trait SumProtocol {
	type PartialFormat[T]	= PBijection[T,BsonValue]

	def sumFormat[T](partials:ISeq[PartialFormat[T]]):Format[T]	=
			Format[T](
				(it:T)			=> partials collapseMapFirst { _ get it } getOrElse fail("no matching constructor found"),
				(it:BsonValue)	=> partials collapseMapFirst { _ set it } getOrElse fail("no matching constructor found")
			)
			
	//------------------------------------------------------------------------------
	
	object Summand {
		/** DSL for name -> format construction */
		implicit def namedSummand[T,C<:T:ClassTag](pair:(String, Format[C])):Summand[T,C]	=
				Summand(pair._1, pair._2)
			
		/** identified by runtime class */
		implicit def classTagSummand[T,C<:T:ClassTag](format:Format[C]):Summand[T,C]	=
				Summand(classTag[C].runtimeClass.getName, format)
	
		/** identified by runtime class */
		implicit def classSummand[T,C<:T:ClassTag:Format](clazz:Class[C]):Summand[T,C]	=
				Summand(clazz.getName, implicitly[Format[C]])
	}
	
	/** NOTE this is not erasure-safe */
	case class Summand[T,C<:T:ClassTag](identifier:String, format:Format[C]) {
		private val tag		= {
			val origTag	= classTag[C]
			val	rtClass	= origTag.runtimeClass
			if (rtClass.isPrimitive)	ClassTag(rtClass.boxed)
			else						origTag
		}
		
		def castValue(value:T):Option[C]	=
				tag unapply value
	}
}