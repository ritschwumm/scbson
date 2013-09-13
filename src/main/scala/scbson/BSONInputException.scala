package scbson

/** base class for BSON reading problems */
abstract class BSONInputException(
	message:String, cause:Exception=null
)
extends Exception(
	message, cause
)
