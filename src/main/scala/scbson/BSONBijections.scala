package scbson

import scutil.lang._
import scutil.time.MilliInstant

object BSONBijections {
	val bsonDouble		= Bijection[BSONDouble,Double]							(_.value,	BSONDouble(_))
	val bsonString		= Bijection[BSONString,String]							(_.value,	BSONString(_))
	val bsonDocument	= Bijection[BSONDocument,ISeq[(String,BSONValue)]]		(_.value,	BSONDocument(_))
	val bsonArray		= Bijection[BSONArray,ISeq[BSONValue]]					(_.value,	BSONArray(_))
	val bsonBinary		= Bijection[BSONBinary,(Array[Byte], BSONBinaryType)]	(it => (BSONBinary unapply it).get, BSONBinary.tupled)
	val bsonObjectId	= Bijection[BSONObjectId,Array[Byte]]					(_.bytes,	BSONObjectId(_))
	val bsonBoolean		= Bijection[BSONBoolean,Boolean]						(_.value,	BSONBoolean(_))
	val bsonDate		= Bijection[BSONDate,MilliInstant]						(_.value,	BSONDate(_))
	val bsonNull		= Bijection[BSONNull.type,Null]							(_ => null, _ => BSONNull)
	val bsonRegex		= Bijection[BSONRegex,(String,Set[BSONRegexOption])]	(it => (BSONRegex unapply it).get, BSONRegex.tupled)
	val bsonCode		= Bijection[BSONCode,String]							(_.code,	BSONCode(_))
	val bsonSymbol		= Bijection[BSONSymbol,Symbol]							(_.value,	BSONSymbol(_))
	val bsonCodeInScope	= Bijection[BSONCodeInScope,(String,BSONDocument)]		(it => (BSONCodeInScope unapply it).get, BSONCodeInScope.tupled)
	val bsonInt			= Bijection[BSONInt,Int]								(_.value,	BSONInt(_))
	val bsonTimestamp	= Bijection[BSONTimestamp,(Int,Int)]					(it => (BSONTimestamp unapply it).get, BSONTimestamp.tupled)
	val bsonLong		= Bijection[BSONLong,Long]								(_.value,	BSONLong(_))
	val bsonMinKey		= Bijection[BSONMinKey.type,Unit]						(_ => (), _ => BSONMinKey)
	val bsonMaxKey		= Bijection[BSONMaxKey.type,Unit]						(_ => (), _ => BSONMaxKey)

}
