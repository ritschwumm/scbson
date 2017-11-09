package scbson.pickle.protocol

import scutil.lang._
import scutil.time._

import scbson.ast._
import scbson.pickle._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit lazy val UnitFormat			= Format[Unit](constant(BSONDocument.empty),	constant(()))
	implicit lazy val NullFormat			= Format[Null](constant(BSONNull),				constant(null))
	implicit lazy val BooleanFormat			= SubtypeFormat[Boolean,		BSONBoolean](BSONBoolean.apply,	_.value)
	implicit lazy val IntFormat				= SubtypeFormat[Int,			BSONInt]	(BSONInt.apply, 	_.value)
	implicit lazy val LongFormat			= SubtypeFormat[Long,			BSONLong]	(BSONLong.apply,	_.value)
	implicit lazy val DoubleFormat			= SubtypeFormat[Double,			BSONDouble]	(BSONDouble.apply,	_.value)
	implicit lazy val StringFormat			= SubtypeFormat[String,			BSONString]	(BSONString.apply,	_.value)
	implicit lazy val SymbolFormat			= SubtypeFormat[Symbol,			BSONSymbol]	(BSONSymbol.apply,	_.value)
	implicit lazy val MilliInstantFormat	= SubtypeFormat[MilliInstant,	BSONDate]	(BSONDate.apply,	_.value)
}
