package scbson.serialization

import scbson._

import BSONSerializationUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of BSONValue as BSONValue */
trait IdentityProtocol {
	/*
	implicit val BSONValueFormat:Format[BSONValue]	= 
			Format(identity, identity)
	*/
		
	implicit def BSONValueFormat[T<:BSONValue]:Format[T]	= new Format[T] {
		def write(out:T):BSONValue	= out
		def read(in:BSONValue):T	= downcast(in)
	}
}
