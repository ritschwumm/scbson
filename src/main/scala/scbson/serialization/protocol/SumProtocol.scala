package scbson.serialization

import scala.reflect._

import scutil.Implicits._

import scbson._

import BSONSerializationUtil._

object SumProtocol extends SumProtocol

trait SumProtocol {
	trait PartialBSONFormat[T] {
		def write(value:T):Option[BSONValue]
		def read(bson:BSONValue):Option[T]
	}

	def sumBSONFormat[T](partials:Seq[PartialBSONFormat[T]]):BSONFormat[T]	=
			BSONFormat[T](
				(it:T)			=> partials flatMapFirst { _ write it } getOrElse fail("no matching constructor found"),
				(it:BSONValue)	=> partials flatMapFirst { _ read  it } getOrElse fail("no matching constructor found")
			)
			
	//------------------------------------------------------------------------------
	
	object Summand {
		/** DSL for name -> format construction */
		implicit def namedSummand[T,C<:T:ClassTag](pair:(String, BSONFormat[C])):Summand[T,C]	=
				Summand(pair._1, pair._2)
			
		/** identified by runtime class */
		implicit def classTagSummand[T,C<:T:ClassTag](format:BSONFormat[C]):Summand[T,C]	=
				Summand(classTag[C].runtimeClass.getName, format)
	
		/** identified by runtime class */
		implicit def classSummand[T,C<:T:ClassTag:BSONFormat](clazz:Class[C]):Summand[T,C]	=
				Summand(clazz.getName, implicitly[BSONFormat[C]])
	}
	
	/** NOTE this is not erasure-safe */
	case class Summand[T,C<:T:ClassTag](identifier:String, format:BSONFormat[C]) {
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
