package scbson.serialization

import scutil.Functions._
import scutil.time._

import scbson._

object NativeProtocol extends NativeProtocol

trait NativeProtocol {
	implicit lazy val NullBSONFormat	= BSONFormat[Null](constant(BSONNull),	constant(null))
	implicit lazy val BooleanBSONFormat	= BSONFormatSubtype[Boolean,	BSONBoolean](BSONBoolean.apply,	_.value)
	implicit lazy val IntBSONFormat		= BSONFormatSubtype[Int,		BSONInt]	(BSONInt.apply, 	_.value)
	implicit lazy val LongBSONFormat	= BSONFormatSubtype[Long,		BSONLong]	(BSONLong.apply,	_.value)
	implicit lazy val DoubleBSONFormat	= BSONFormatSubtype[Double,		BSONDouble]	(BSONDouble.apply,	_.value)
	implicit lazy val StringBSONFormat	= BSONFormatSubtype[String,		BSONString]	(BSONString.apply,	_.value)
	implicit lazy val SymbolBSONFormat	= BSONFormatSubtype[Symbol,		BSONSymbol]	(BSONSymbol.apply,	_.value)
	implicit lazy val InstantBSONFormat	= BSONFormatSubtype[Instant,	BSONDate]	(BSONDate.apply,	_.value)
}
