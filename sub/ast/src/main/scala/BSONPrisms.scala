package scbson.ast

import scutil.lang._
import scutil.time.MilliInstant

object BSONPrisms {
	val nullRef		:Prism[BSONValue,Unit]							= Prism(_.asNull,			BSONValue.mkNull)
	val minKey		:Prism[BSONValue,Unit]							= Prism(_.asMinKey,			BSONValue.mkMinKey)
	val maxKex		:Prism[BSONValue,Unit]							= Prism(_.asMaxKey,			BSONValue.mkMaxKey)
	
	val boolean		:Prism[BSONValue,Boolean]						= Prism(_.asBoolean,		BSONValue.mkBoolean)
	val int			:Prism[BSONValue,Int]							= Prism(_.asInt,			BSONValue.mkInt)
	val long		:Prism[BSONValue,Long]							= Prism(_.asLong,			BSONValue.mkLong)
	val double		:Prism[BSONValue,Double]						= Prism(_.asDouble,			BSONValue.mkDouble)
	val string		:Prism[BSONValue,String]						= Prism(_.asString,			BSONValue.mkString)
	val symbol		:Prism[BSONValue,Symbol]						= Prism(_.asSymbol,			BSONValue.mkSymbol)
	val objectId	:Prism[BSONValue,Array[Byte]]					= Prism(_.asObjectId,		BSONValue.mkObjectId)
	val date		:Prism[BSONValue,MilliInstant]					= Prism(_.asDate,			BSONValue.mkDate)
	
	val code		:Prism[BSONValue,String]						= Prism(_.asCode,			BSONValue.mkCode)
	val codeInScope	:Prism[BSONValue,(String,BSONDocument)]			= Prism(_.asCodeInScope,	(BSONValue.mkCodeInScope	_).tupled)
	val timestamp	:Prism[BSONValue,(Int,Int)]						= Prism(_.asTimestamp,		(BSONValue.mkTimestamp		_).tupled)
	val binary		:Prism[BSONValue,(Array[Byte],BSONBinaryType)]	= Prism(_.asBinary,			(BSONValue.mkBinary			_).tupled)
	val regex		:Prism[BSONValue,(String,Set[BSONRegexOption])]	= Prism(_.asRegex,			(BSONValue.mkRegex			_).tupled)
	
	val arraySeq	:Prism[BSONValue,ISeq[BSONValue]]				= Prism(_.asArray,			BSONValue.mkArray)
	val documentSeq	:Prism[BSONValue,ISeq[(String,BSONValue)]]		= Prism(_.asDocument,		BSONValue.mkDocument)
}
