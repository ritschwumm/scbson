package scbson.serialization

import reflect.runtime.universe._

import scutil.Implicits._

import scbson._

import BSONSerializationUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated with SumProtocol {
	def caseObjectFormat[T:TypeTag](singleton:T):Format[T]	= new Format[T] {
		def write(out:T):BSONValue	= {
			BSONDocument.empty
		}
		def read(in:BSONValue):T	= {
			singleton
		}
	}
	
	def caseClassFormat1[S1:Format,T:TypeTag](
		apply:S1=>T,
		unapply:T=>Option[S1]
	):Format[T]	= {
		val Seq(k1)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):BSONValue	= {
				val fields	= unapply(out).get
				BSONVarDocument(
					k1	-> doWrite[S1](fields)
				)
			}
			def read(in:BSONValue):T	= {
				val map	= forceMap(in)
				apply(
					doRead[S1](map(k1))
				)
			}
		}
	}
	
	/*
	def caseClassFormat2[S1:Format,S2:Format,T:TypeTag](
		apply:(S1,S2)=>T, 
		unapply:T=>Option[(S1,S2)]
	):Format[T]	= {
		val Seq(k1,k2)	= fieldNamesFor[T]
		new Format[T] {
			def write(out:T):BSONValue	= {
				val fields	= unapply(out).get
				BSONVarDocument(
					k1	-> doWrite[S1](fields._1),
					k2	-> doWrite[S2](fields._2)
				)
			}
			def read(in:BSONValue):T	= {
				val map	= forceMap(in)
				apply(
					doRead[S1](map(k1)),
					doRead[S2](map(k2))
				)
			}
		}
	}
	*/
	
	/** uses a field with an empty name for the specific constructor */
	def caseClassSumFormat[T](summands:Summand[T,_<:T]*):Format[T]	=
			sumFormat(summands map (new InlinePartialFormat(_)))
		
	/** injects the type tag as a field with an empty name into an existing object */
	private class InlinePartialFormat[T,C<:T](summand:Summand[T,C]) extends PartialFormat[T] {
		import summand._
		val typeTag	= ""
		def write(value:T):Option[BSONValue]	=
				castValue(value) map { it =>
					BSONVarDocument(typeTag -> BSONString(identifier)) ++ 
					downcast[BSONDocument](format write it)
				}
		def read(bson:BSONValue):Option[T]	=
				downcast[BSONDocument](bson).value 
				.exists	{ _ == (typeTag, BSONString(identifier)) } 
				.guard	{ format read bson }
	}
		
	// BETTER cache results
	protected def fieldNamesFor[T:TypeTag]:Seq[String]	= {
		val typ	= typeOf[T]
		val names:Option[Seq[String]]	=
				for {
					primaryCtor	<- typ.declarations filter { _.isMethod } map { _.asMethod } filter { _.isPrimaryConstructor } singleOption;
					paramNames	<- primaryCtor.paramss.singleOption
				}
				yield paramNames map { _.name.decoded }
		names getOrError ("cannot get fields for type " + typ)
	}
	
	protected def forceMap(in:BSONValue):Map[String,BSONValue]	=
			downcast[BSONDocument](in).valueMap
}
