package scbson.ast

import scutil.lang._
import scutil.time.MilliInstant

object BsonPrisms {
	val nullRef		:Prism[BsonValue,Unit]							= Prism(_.asNull,			BsonValue.mkNull)
	val minKey		:Prism[BsonValue,Unit]							= Prism(_.asMinKey,			BsonValue.mkMinKey)
	val maxKex		:Prism[BsonValue,Unit]							= Prism(_.asMaxKey,			BsonValue.mkMaxKey)
	
	val boolean		:Prism[BsonValue,Boolean]						= Prism(_.asBoolean,		BsonValue.mkBoolean)
	val int			:Prism[BsonValue,Int]							= Prism(_.asInt,			BsonValue.mkInt)
	val long		:Prism[BsonValue,Long]							= Prism(_.asLong,			BsonValue.mkLong)
	val double		:Prism[BsonValue,Double]						= Prism(_.asDouble,			BsonValue.mkDouble)
	val string		:Prism[BsonValue,String]						= Prism(_.asString,			BsonValue.mkString)
	val symbol		:Prism[BsonValue,Symbol]						= Prism(_.asSymbol,			BsonValue.mkSymbol)
	val objectId	:Prism[BsonValue,Array[Byte]]					= Prism(_.asObjectId,		BsonValue.mkObjectId)
	val date		:Prism[BsonValue,MilliInstant]					= Prism(_.asDate,			BsonValue.mkDate)
	
	val code		:Prism[BsonValue,String]						= Prism(_.asCode,			BsonValue.mkCode)
	val codeInScope	:Prism[BsonValue,(String,BsonDocument)]			= Prism(_.asCodeInScope,	(BsonValue.mkCodeInScope	_).tupled)
	val timestamp	:Prism[BsonValue,(Int,Int)]						= Prism(_.asTimestamp,		(BsonValue.mkTimestamp		_).tupled)
	val binary		:Prism[BsonValue,(Array[Byte],BsonBinaryType)]	= Prism(_.asBinary,			(BsonValue.mkBinary			_).tupled)
	val regex		:Prism[BsonValue,(String,Set[BsonRegexOption])]	= Prism(_.asRegex,			(BsonValue.mkRegex			_).tupled)
	
	val arraySeq	:Prism[BsonValue,ISeq[BsonValue]]				= Prism(_.asArray,			BsonValue.mkArray)
	val documentSeq	:Prism[BsonValue,ISeq[(String,BsonValue)]]		= Prism(_.asDocument,		BsonValue.mkDocument)
}
