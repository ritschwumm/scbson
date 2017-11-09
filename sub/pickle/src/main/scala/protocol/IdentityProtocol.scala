package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BSONPickleUtil._

object IdentityProtocol extends IdentityProtocol

/** allows pickle and depickle of BSONValue as BSONValue */
trait IdentityProtocol {
	implicit def PassThroughFormat[T<:BSONValue]:Format[T]	=
			Format[T](
				identity,
				downcast[T]
			)
}
