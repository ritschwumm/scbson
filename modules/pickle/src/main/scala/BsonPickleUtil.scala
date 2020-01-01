package scbson.pickle

import scbson.ast._

object BsonPickleUtil {
	def fail(message:String):Nothing =
		throw new BsonUnpickleException(BsonUnpickleFailure(message))

	// TODO add expected and actual ctor name
	@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
	def downcast[T<:BsonValue](it:BsonValue):T	=
		try { it.asInstanceOf[T] }
		catch { case e:ClassCastException => fail("unexpected bson value type") }

	//------------------------------------------------------------------------------

	def documentMap(it:BsonValue):Map[String,BsonValue]	=
		it match {
			case BsonDocument(value)	=> value.toMap
			case _						=> fail("expected a BsonDocument")
		}

	def documentValue(it:BsonValue):Seq[(String,BsonValue)]	=
		it match {
			case BsonDocument(value)	=> value
			case _						=> fail("expected a BsonDocument")
		}

	def arrayValue(it:BsonValue):Seq[BsonValue]	=
		it match {
			case BsonArray(value)	=> value
			case _					=> fail("expected a BsonArray")
		}

	//------------------------------------------------------------------------------

	def requireField(map:Map[String,BsonValue], key:String):BsonValue	=
		map get key match {
			case Some(x)	=> x
			case None		=> fail("expected a field " + key)
		}

	//------------------------------------------------------------------------------

	def unapplyTotal[S,T](unapply:S=>Option[T], value:S):T	=
		unapply(value) match {
			case Some(x)	=> x
			case None		=> sys error "expected unapply to be total"
		}
}
