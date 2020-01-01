package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object IdentityProtocol extends IdentityProtocol

/** allows pickle and depickle of BsonValue as BsonValue */
trait IdentityProtocol {
	implicit def PassThroughFormat[T<:BsonValue]:Format[T]	=
		Format[T](
			identity,
			downcast[T]
		)
}
