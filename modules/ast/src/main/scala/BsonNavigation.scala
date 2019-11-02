package scbson.ast

import scutil.lang._
import scutil.time.MilliInstant

object BsonNavigation {
	implicit def extendBsonNavigation(value:BsonValue):BsonNavigation				= new BsonNavigation(Some(value))
	implicit def extendBsonNavigationOption(value:Option[BsonValue]):BsonNavigation	= new BsonNavigation(value)
}

@SuppressWarnings(Array("org.wartremover.warts.Overloading"))
final class BsonNavigation(peer:Option[BsonValue]) {
	def /(key:String):Option[BsonValue]	= toMap		flatMap { _ get		key		}
	def /(index:Int):Option[BsonValue]	= arraySeq	flatMap { _ lift	index	}

	//------------------------------------------------------------------------------

	def nullRef:Option[Unit]							= peer	flatMap { _.asNull			}
	def minKeyRef:Option[Unit]							= peer	flatMap { _.asMinKey		}
	def maxKeyRef:Option[Unit]							= peer	flatMap { _.asMaxKey		}

	def boolean:Option[Boolean]							= peer	flatMap { _.asBoolean		}
	def int:Option[Int]									= peer	flatMap { _.asInt			}
	def long:Option[Long]								= peer	flatMap { _.asLong			}
	def double:Option[Double]							= peer	flatMap { _.asDouble		}
	def string:Option[String]							= peer	flatMap { _.asString		}
	def symbol:Option[Symbol]							= peer	flatMap { _.asSymbol		}
	def objectId:Option[ByteString]						= peer	flatMap { _.asObjectId		}
	def date:Option[MilliInstant]						= peer	flatMap { _.asDate			}

	def code:Option[String]								= peer	flatMap { _.asCode			}
	def codeInScope:Option[(String,BsonDocument)]		= peer	flatMap { _.asCodeInScope	}
	def timestamp:Option[(Int,Int)]						= peer	flatMap { _.asTimestamp		}
	def binary:Option[(ByteString,BsonBinaryType)]		= peer	flatMap { _.asBinary		}
	def regex:Option[(String,Set[BsonRegexOption])]		= peer	flatMap { _.asRegex			}

	def arraySeq:Option[ISeq[BsonValue]]				= peer	flatMap { _.asArray			}
	def documentSeq:Option[ISeq[(String,BsonValue)]]	= peer	flatMap { _.asDocument		}

	//------------------------------------------------------------------------------

	def toMap:Option[Map[String,BsonValue]]	= documentSeq map { _.toMap		}
}
