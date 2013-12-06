package scbson.serialization

import scbson._

import BSONSerializationUtil._

object IdentityProtocol extends IdentityProtocol

/** allows serialization and deserialization of BSONValue as BSONValue */
trait IdentityProtocol {
	implicit def PassThroughFormat[T<:BSONValue]:Format[T]	=
			Format[T](
				identity,
				downcast[T]
			)
}
