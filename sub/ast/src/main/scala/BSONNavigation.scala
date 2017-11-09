package scbson.ast

import scutil.lang._
import scutil.time.MilliInstant

object BSONNavigation {
	implicit def extendBSONNavigation(value:BSONValue):BSONNavigation			= new BSONNavigation(Some(value))
	implicit def extendBSONNavigation(value:Option[BSONValue]):BSONNavigation	= new BSONNavigation(value)
}

final class BSONNavigation(peer:Option[BSONValue]) {
	def /(key:String):Option[BSONValue]	= toMap		flatMap { _ get		key		}
	def /(index:Int):Option[BSONValue]	= arraySeq	flatMap { _ lift	index	}
	
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
	def objectId:Option[Array[Byte]]					= peer	flatMap { _.asObjectId		}
	def date:Option[MilliInstant]						= peer	flatMap { _.asDate			}
	
	def code:Option[String]								= peer	flatMap { _.asCode			}
	def codeInScope:Option[(String,BSONDocument)]		= peer	flatMap { _.asCodeInScope	}
	def timestamp:Option[(Int,Int)]						= peer	flatMap { _.asTimestamp		}
	def binary:Option[(Array[Byte],BSONBinaryType)]		= peer	flatMap { _.asBinary		}
	def regex:Option[(String,Set[BSONRegexOption])]		= peer	flatMap { _.asRegex			}
	
	def arraySeq:Option[ISeq[BSONValue]]				= peer	flatMap { _.asArray			}
	def documentSeq:Option[ISeq[(String,BSONValue)]]	= peer	flatMap { _.asDocument		}
	
	//------------------------------------------------------------------------------

	def toMap:Option[Map[String,BSONValue]]	= documentSeq map { _.toMap		}
}
