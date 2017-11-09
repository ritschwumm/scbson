package scbson.pickle

import scutil.lang._

import scbson.ast._

object BSONPickleUtil {
	def fail(message:String):Nothing =
			throw new BSONUnpickleException(BSONUnpickleFailure(message))
	
	// TODO add expected and actual ctor name
	def downcast[T<:BSONValue](it:BSONValue):T	=
			try { it.asInstanceOf[T] }
			catch { case e:ClassCastException => fail("unexpected bson value type") }
			
	//------------------------------------------------------------------------------
	
	def documentMap(it:BSONValue):Map[String,BSONValue]	=
			it match {
				case BSONDocument(value)	=> value.toMap
				case _						=> fail("expected a BSONDocument")
			}
	
	def documentValue(it:BSONValue):ISeq[(String,BSONValue)]	=
			it match {
				case BSONDocument(value)	=> value
				case _						=> fail("expected a BSONDocument")
			}
			
	def arrayValue(it:BSONValue):ISeq[BSONValue]	=
			it match {
				case BSONArray(value)	=> value
				case _					=> fail("expected a BSONArray")
			}
			
	//------------------------------------------------------------------------------
	
	def unapplyTotal[S,T](unapply:S=>Option[T], value:S):T	=
			unapply(value) match {
				case Some(x)	=> x
				case None		=> sys error "expected unapply to be total"
			}
}
