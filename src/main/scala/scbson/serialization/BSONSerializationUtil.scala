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
}
