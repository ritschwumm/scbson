package scbson.pickle.protocol

import scutil.lang._
import scutil.time._

import scbson.ast._
import scbson.pickle._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit lazy val UnitFormat			= Format[Unit](constant(BsonDocument.empty),	constant(()))
	implicit lazy val NullFormat			= Format[Null](constant(BsonNull),				constant(null))
	implicit lazy val BooleanFormat			= SubtypeFormat[Boolean,		BsonBoolean](BsonBoolean.apply,	_.value)
	implicit lazy val IntFormat				= SubtypeFormat[Int,			BsonInt]	(BsonInt.apply, 	_.value)
	implicit lazy val LongFormat			= SubtypeFormat[Long,			BsonLong]	(BsonLong.apply,	_.value)
	implicit lazy val DoubleFormat			= SubtypeFormat[Double,			BsonDouble]	(BsonDouble.apply,	_.value)
	implicit lazy val StringFormat			= SubtypeFormat[String,			BsonString]	(BsonString.apply,	_.value)
	implicit lazy val SymbolFormat			= SubtypeFormat[Symbol,			BsonSymbol]	(BsonSymbol.apply,	_.value)
	implicit lazy val MilliInstantFormat	= SubtypeFormat[MilliInstant,	BsonDate]	(BsonDate.apply,	_.value)
}
