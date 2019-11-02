package scbson.pickle

import scutil.lang._
import scutil.time.MilliInstant

import scbson.ast._

import BsonPickleUtil.unapplyTotal

object BsonBijections {
	val bsonDouble		= Bijection[BsonDouble,Double]							(_.value,	BsonDouble(_))
	val bsonString		= Bijection[BsonString,String]							(_.value,	BsonString(_))
	val bsonDocument	= Bijection[BsonDocument,Seq[(String,BsonValue)]]		(_.value,	BsonDocument(_))
	val bsonArray		= Bijection[BsonArray,Seq[BsonValue]]					(_.value,	BsonArray(_))
	val bsonBinary		= Bijection[BsonBinary,(ByteString, BsonBinaryType)]	(unapplyTotal(BsonBinary.unapply,		_), BsonBinary.tupled)
	val bsonObjectId	= Bijection[BsonObjectId,ByteString]					(_.bytes,	BsonObjectId(_))
	val bsonBoolean		= Bijection[BsonBoolean,Boolean]						(_.value,	BsonBoolean(_))
	val bsonDate		= Bijection[BsonDate,MilliInstant]						(_.value,	BsonDate(_))
	val bsonNull		= Bijection[BsonNull.type,Null]							(_ => null, _ => BsonNull)
	val bsonRegex		= Bijection[BsonRegex,(String,Set[BsonRegexOption])]	(unapplyTotal(BsonRegex.unapply,		_), BsonRegex.tupled)
	val bsonCode		= Bijection[BsonCode,String]							(_.code,	BsonCode(_))
	val bsonSymbol		= Bijection[BsonSymbol,Symbol]							(_.value,	BsonSymbol(_))
	val bsonCodeInScope	= Bijection[BsonCodeInScope,(String,BsonDocument)]		(unapplyTotal(BsonCodeInScope.unapply,	_), BsonCodeInScope.tupled)
	val bsonInt			= Bijection[BsonInt,Int]								(_.value,	BsonInt(_))
	val bsonTimestamp	= Bijection[BsonTimestamp,(Int,Int)]					(unapplyTotal(BsonTimestamp.unapply,	_), BsonTimestamp.tupled)
	val bsonLong		= Bijection[BsonLong,Long]								(_.value,	BsonLong(_))
	val bsonMinKey		= Bijection[BsonMinKey.type,Unit]						(_ => (), _ => BsonMinKey)
	val bsonMaxKey		= Bijection[BsonMaxKey.type,Unit]						(_ => (), _ => BsonMaxKey)
}
