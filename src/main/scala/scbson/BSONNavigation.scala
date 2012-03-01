package scbson

object BSONNavigation {
	implicit def extendBSONNavigation(value:BSONValue):BSONNavigation			= new BSONNavigation(Some(value))
	implicit def extendBSONNavigation(value:Option[BSONValue]):BSONNavigation	= new BSONNavigation(value)
}

final class BSONNavigation(value:Option[BSONValue]) {
	def /(key:String):Option[BSONValue]	= bsonDocument	flatMap { _.value.toMap	get		key		}
	def /(index:Int):Option[BSONValue]	= bsonArray		flatMap { _.value.lift	apply	index	}
		
	def bsonDouble:Option[BSONDouble]			= value collect { case x:BSONDouble			=> x			}
	def bsonString:Option[BSONString]			= value collect { case x:BSONString			=> x			}
	def bsonDocument:Option[BSONDocument]		= value collect { case x:BSONDocument		=> x			}
	def bsonArray:Option[BSONArray]				= value collect { case x:BSONArray			=> x			}
	def bsonBinary:Option[BSONBinary]			= value collect { case x:BSONBinary			=> x			}
	def bsonObjectId:Option[BSONObjectId]		= value collect { case x:BSONObjectId		=> x			}
	def bsonBoolean:Option[BSONBoolean]			= value collect { case x:BSONBoolean		=> x			}
	def bsonDate:Option[BSONDate]				= value collect { case x:BSONDate			=> x			}
	def bsonNull:Option[BSONNull.type]			= value collect { case BSONNull				=> BSONNull		}
	def bsonRegex:Option[BSONRegex]				= value collect { case x:BSONRegex			=> x			}
	def bsonCode:Option[BSONCode]				= value collect { case x:BSONCode			=> x			}
	def bsonSymbol:Option[BSONSymbol]			= value collect { case x:BSONSymbol			=> x			}
	def bsonCodeInScope:Option[BSONCodeInScope]	= value collect { case x:BSONCodeInScope	=> x			}
	def bsonInt:Option[BSONInt]					= value collect { case x:BSONInt			=> x			}
	def bsonTimestamp:Option[BSONTimestamp]		= value collect { case x:BSONTimestamp		=> x			}
	def bsonLong:Option[BSONLong]				= value collect { case x:BSONLong			=> x 			}
	def bsonMinKey:Option[BSONMinKey.type]		= value collect { case BSONMinKey			=> BSONMinKey	}
	def bsonMaxKey:Option[BSONMaxKey.type]		= value collect { case BSONMaxKey			=> BSONMaxKey	}
}
