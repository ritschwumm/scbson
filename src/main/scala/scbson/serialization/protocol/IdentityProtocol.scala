package scbson.serialization

import scbson._

import BSONSerializationUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of BSONValue as BSONValue */
trait IdentityProtocol {
	/*
	implicit val BSONValueBSONFormat:BSONFormat[BSONValue]	= 
			BSONFormat(identity, identity)
	*/
		
	implicit def BSONValueBSONFormat[T<:BSONValue]:BSONFormat[T]	= new BSONFormat[T] {
		def write(out:T):BSONValue	= out
		def read(in:BSONValue):T	= downcast(in)
	}
}
