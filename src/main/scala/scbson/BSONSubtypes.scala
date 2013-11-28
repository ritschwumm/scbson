package scbson

import scutil.lang.Subtype

object BSONSubtypes {
	val bsonDouble		= Subtype.partial[BSONValue,BSONDouble]			{ case x@BSONDouble(_)			=> x }
	val bsonString		= Subtype.partial[BSONValue,BSONString]			{ case x@BSONString(_)			=> x }
	val bsonDocument	= Subtype.partial[BSONValue,BSONDocument]		{ case x@BSONDocument(_)		=> x }
	val bsonArray		= Subtype.partial[BSONValue,BSONArray]			{ case x@BSONArray(_)			=> x }
	val bsonBinary		= Subtype.partial[BSONValue,BSONBinary]			{ case x@BSONBinary(_, _)		=> x }
	val bsonObjectId	= Subtype.partial[BSONValue,BSONObjectId]		{ case x@BSONObjectId(_)		=> x }
	val bsonBoolean		= Subtype.partial[BSONValue,BSONBoolean]		{ case x@BSONBoolean(_)			=> x }
	val bsonDate		= Subtype.partial[BSONValue,BSONDate]			{ case x@BSONDate(_)			=> x }
	val bsonNull		= Subtype.partial[BSONValue,BSONNull.type]		{ case x@BSONNull				=> x }
	val bsonRegex		= Subtype.partial[BSONValue,BSONRegex]			{ case x@BSONRegex(_, _)		=> x }
	val bsonCode		= Subtype.partial[BSONValue,BSONCode]			{ case x@BSONCode(_)			=> x }
	val bsonSymbol		= Subtype.partial[BSONValue,BSONSymbol]			{ case x@BSONSymbol(_)			=> x }
	val bsonCodeInScope	= Subtype.partial[BSONValue,BSONCodeInScope]	{ case x@BSONCodeInScope(_, _)	=> x }
	val bsonInt			= Subtype.partial[BSONValue,BSONInt]			{ case x@BSONInt(_)				=> x }
	val bsonTimestamp	= Subtype.partial[BSONValue,BSONTimestamp]		{ case x@BSONTimestamp(_, _)	=> x }
	val bsonLong		= Subtype.partial[BSONValue,BSONLong]			{ case x@BSONLong(_)			=> x }
	val bsonMinKey		= Subtype.partial[BSONValue,BSONMinKey.type]	{ case x@BSONMinKey				=> x }
	val bsonMaxKey		= Subtype.partial[BSONValue,BSONMaxKey.type]	{ case x@BSONMaxKey				=> x }
}
