package scbson.serialization

import scbson._

object BSONSerializationUtil {
	def fail(message:String):Nothing =
			throw new BSONDeserializationException(message)
	
	def fail(message:String, e:Exception):Nothing =
			throw new BSONDeserializationException(message, e)
	
	def downcast[T<:BSONValue](it:BSONValue):T	=
			try { it.asInstanceOf[T] }
			catch { case e:ClassCastException => fail("unexpected bson value type", e) }
			
	//------------------------------------------------------------------------------
	
	def documentMap(it:BSONValue):Map[String,BSONValue]	=
			it match {
				case BSONDocument(value)	=> value.toMap
				case _						=> fail("expected a BSONDocument")
			}
	
	def documentValue(it:BSONValue):Seq[(String,BSONValue)]	=
			it match {
				case BSONDocument(value)	=> value
				case _						=> fail("expected a BSONDocument")
			}
			
	def arrayValue(it:BSONValue):Seq[BSONValue]	=
			it match {
				case BSONArray(value)	=> value
				case _					=> fail("expected a BSONArray")
			}
}
