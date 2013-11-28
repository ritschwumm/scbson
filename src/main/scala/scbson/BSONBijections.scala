package scbson

import scutil.lang.Bijection
import scutil.time.MilliInstant

object BSONBijections {
	val bsonDouble		= Bijection[BSONDouble,Double]							(_.value,	BSONDouble(_))
	val bsonString		= Bijection[BSONString,String]							(_.value,	BSONString(_))
	val bsonDocument	= Bijection[BSONDocument,Seq[(String,BSONValue)]]		(_.value,	BSONDocument(_))
	val bsonArray		= Bijection[BSONArray,Seq[BSONValue]]					(_.value,	BSONArray(_))
	val bsonBinary		= Bijection[BSONBinary,(Array[Byte], BSONBinaryType)]	(BSONBinary			unapply _ get, BSONBinary.tupled)
	val bsonObjectId	= Bijection[BSONObjectId,Array[Byte]]					(_.bytes,	BSONObjectId(_))
	val bsonBoolean		= Bijection[BSONBoolean,Boolean]						(_.value,	BSONBoolean(_))
	val bsonDate		= Bijection[BSONDate,MilliInstant]						(_.value,	BSONDate(_))
	val bsonNull		= Bijection[BSONNull.type,Null]							(_ => null, _ => BSONNull)
	val bsonRegex		= Bijection[BSONRegex,(String,Set[BSONRegexOption])]	(BSONRegex			unapply _ get, BSONRegex.tupled)
	val bsonCode		= Bijection[BSONCode,String]							(_.code,	BSONCode(_))
	val bsonSymbol		= Bijection[BSONSymbol,Symbol]							(_.value,	BSONSymbol(_))
	val bsonCodeInScope	= Bijection[BSONCodeInScope,(String,BSONDocument)]		(BSONCodeInScope	unapply _ get, BSONCodeInScope.tupled)
	val bsonInt			= Bijection[BSONInt,Int]								(_.value,	BSONInt(_))
	val bsonTimestamp	= Bijection[BSONTimestamp,(Int,Int)]					(BSONTimestamp		unapply _ get, BSONTimestamp.tupled)
	val bsonLong		= Bijection[BSONLong,Long]								(_.value,	BSONLong(_))
	val bsonMinKey		= Bijection[BSONMinKey.type,Unit]						(_ => (), _ => BSONMinKey)
	val bsonMaxKey		= Bijection[BSONMaxKey.type,Unit]						(_ => (), _ => BSONMaxKey)

}
