package scbson

import scutil.lang.Subtype

object BSONNavigation {
	implicit def extendBSONNavigation(value:BSONValue):BSONNavigation			= new BSONNavigation(Some(value))
	implicit def extendBSONNavigation(value:Option[BSONValue]):BSONNavigation	= new BSONNavigation(value)
}

final class BSONNavigation(peer:Option[BSONValue]) {
	def /(key:String):Option[BSONValue]	= bsonDocument	flatMap { _.value.toMap	get		key		}
	def /(index:Int):Option[BSONValue]	= bsonArray		flatMap { _.value.lift	apply	index	}
		
	def downcast[T<:BSONValue](subtype:Subtype[BSONValue,T]):Option[T]	= peer flatMap subtype.unapply
	
	//------------------------------------------------------------------------------
	
	def bsonDouble:Option[BSONDouble]			= downcast(BSONSubtypes.bsonDouble)
	def bsonString:Option[BSONString]			= downcast(BSONSubtypes.bsonString)
	def bsonDocument:Option[BSONDocument]		= downcast(BSONSubtypes.bsonDocument)
	def bsonArray:Option[BSONArray]				= downcast(BSONSubtypes.bsonArray)
	def bsonBinary:Option[BSONBinary]			= downcast(BSONSubtypes.bsonBinary)
	def bsonObjectId:Option[BSONObjectId]		= downcast(BSONSubtypes.bsonObjectId)
	def bsonBoolean:Option[BSONBoolean]			= downcast(BSONSubtypes.bsonBoolean)
	def bsonDate:Option[BSONDate]				= downcast(BSONSubtypes.bsonDate)
	def bsonNull:Option[BSONNull.type]			= downcast(BSONSubtypes.bsonNull)
	def bsonRegex:Option[BSONRegex]				= downcast(BSONSubtypes.bsonRegex)
	def bsonCode:Option[BSONCode]				= downcast(BSONSubtypes.bsonCode)
	def bsonSymbol:Option[BSONSymbol]			= downcast(BSONSubtypes.bsonSymbol)
	def bsonCodeInScope:Option[BSONCodeInScope]	= downcast(BSONSubtypes.bsonCodeInScope)
	def bsonInt:Option[BSONInt]					= downcast(BSONSubtypes.bsonInt)
	def bsonTimestamp:Option[BSONTimestamp]		= downcast(BSONSubtypes.bsonTimestamp)
	def bsonLong:Option[BSONLong]				= downcast(BSONSubtypes.bsonLong)
	def bsonMinKey:Option[BSONMinKey.type]		= downcast(BSONSubtypes.bsonMinKey)
	def bsonMaxKey:Option[BSONMaxKey.type]		= downcast(BSONSubtypes.bsonMaxKey)
}
