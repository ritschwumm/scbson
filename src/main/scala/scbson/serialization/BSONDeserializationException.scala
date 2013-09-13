package scbson.serialization

import scbson.BSONInputException

final class BSONDeserializationException(
	message:String, cause:Exception = null
)
extends BSONInputException(
	message, cause
)
