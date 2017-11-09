package scbson.ast

import java.util.{ Arrays => JArrays }

import scutil.base.implicits._
import scutil.lang._
import scutil.time.MilliInstant

object BsonValue {
	val theNull:BsonValue												= BsonNull
	val theMinKey:BsonValue												= BsonMinKey
	val theMaxKey:BsonValue												= BsonMaxKey
	def mkNull(it:Unit):BsonValue										= theNull
	def mkMinKey(it:Unit):BsonValue										= theMinKey
	def mkMaxKey(it:Unit):BsonValue										= theMaxKey
	
	def mkBoolean(it:Boolean):BsonValue									= BsonBoolean(it)
	def mkInt(it:Int):BsonValue											= BsonInt(it)
	def mkLong(it:Long):BsonValue										= BsonLong(it)
	def mkDouble(it:Double):BsonValue									= BsonDouble(it)
	def mkString(it:String):BsonValue									= BsonString(it)
	def mkSymbol(it:Symbol):BsonValue									= BsonSymbol(it)
	def mkObjectId(it:Array[Byte]):BsonValue							= BsonObjectId(it)
	def mkDate(it:MilliInstant):BsonValue								= BsonDate(it)
	
	def mkTimestamp(stamp:Int, inc:Int):BsonValue						= BsonTimestamp(stamp, inc)
	def mkCode(code:String):BsonValue									= BsonCode(code)
	def mkCodeInScope(code:String, scope:BsonDocument):BsonValue		= BsonCodeInScope(code, scope)
	def mkBinary(value:Array[Byte], subtype:BsonBinaryType):BsonValue	= BsonBinary(value, subtype)
	def mkRegex(pattern:String, options:Set[BsonRegexOption]):BsonValue	= BsonRegex(pattern, options)
	
	def mkArray(it:ISeq[BsonValue]):BsonValue							= BsonArray(it)
	def mkDocument(it:ISeq[(String,BsonValue)]):BsonValue				= BsonDocument(it)

	//------------------------------------------------------------------------------
	
	def typeId[T<:BsonValue:Manifest]:Int	= {
		val clazz	= implicitly[Manifest[T]].getClass
			 if (clazz == classOf[BsonDouble])		1
		else if (clazz == classOf[BsonString])		2
		else if (clazz == classOf[BsonDocument])	3
		
		else if (clazz == classOf[BsonArray])		4
		else if (clazz == classOf[BsonBinary])		5
		// else if (clazz == classOf[BsonUndefined])	6
		else if (clazz == classOf[BsonObjectId])	7
		else if (clazz == classOf[BsonBoolean])		8
		else if (clazz == classOf[BsonDate])		9
		else if (clazz == BsonNull.getClass)		10
		else if (clazz == classOf[BsonRegex])		11
		// else if (clazz == classOf[BsonDBPointer])	12
		else if (clazz == classOf[BsonCode])		13
		else if (clazz == classOf[BsonSymbol])		14
		else if (clazz == classOf[BsonCodeInScope])	15
		else if (clazz == classOf[BsonInt])			16
		else if (clazz == classOf[BsonTimestamp])	17
		else if (clazz == classOf[BsonLong])		18
		else if (clazz == BsonMinKey.getClass)		255
		else if (clazz == BsonMaxKey.getClass)		127
		else sys error ("unexpected BsonValue: " + clazz.toString)
	}
}
sealed abstract class BsonValue {
	def asNull:Option[Unit]								= this matchOption { case BsonNull						=> ()	}
	def asMinKey:Option[Unit]							= this matchOption { case BsonMinKey					=> ()	}
	def asMaxKey:Option[Unit]							= this matchOption { case BsonMaxKey					=> ()	}
	
	def asBoolean:Option[Boolean]						= this matchOption { case BsonBoolean(x)				=> x	}
	def asInt:Option[Int]								= this matchOption { case BsonInt(x)					=> x	}
	def asLong:Option[Long]								= this matchOption { case BsonLong(x)					=> x	}
	def asDouble:Option[Double]							= this matchOption { case BsonDouble(x)					=> x	}
	def asString:Option[String]							= this matchOption { case BsonString(x)					=> x	}
	def asSymbol:Option[Symbol]							= this matchOption { case BsonSymbol(x)					=> x	}
	def asObjectId:Option[Array[Byte]]					= this matchOption { case BsonObjectId(x)				=> x	}
	def asDate:Option[MilliInstant]						= this matchOption { case BsonDate(x)					=> x	}
	
	def asCode:Option[String]							= this matchOption { case BsonCode(code)				=> code 				}
	def asCodeInScope:Option[(String,BsonDocument)]		= this matchOption { case BsonCodeInScope(code, scope)	=> (code, scope) 		}
	def asTimestamp:Option[(Int,Int)]					= this matchOption { case BsonTimestamp(stamp, inc)		=> (stamp, inc) 		}
	def asBinary:Option[(Array[Byte], BsonBinaryType)]	= this matchOption { case BsonBinary(value, subtype)	=> (value, subtype)		}
	def asRegex:Option[(String, Set[BsonRegexOption])]	= this matchOption { case BsonRegex(pattern, options)	=> (pattern, options)	}
	
	def asArray:Option[ISeq[BsonValue]]					= this matchOption { case BsonArray(x)					=> x	}
	def asDocument:Option[ISeq[(String,BsonValue)]]		= this matchOption { case BsonDocument(x)				=> x	}

	/*
	def downcastHack[T<:BsonValue]:Option[T]	=
			try { Some(asInstanceOf[T]) }
			catch { case e:ClassCastException => None }
	*/
}

final case class BsonDouble(value:Double)									extends BsonValue

final case class BsonString(value:String)									extends BsonValue

object BsonDocument {
	val empty	= BsonDocument(Vector.empty)
	
	object Var {
		def apply(it:(String,BsonValue)*):BsonDocument						= BsonDocument(it.toVector)
		def unapplySeq(it:BsonDocument):Option[ISeq[(String,BsonValue)]]	= Some(it.value)
	}
}
final case class BsonDocument(value:ISeq[(String,BsonValue)])				extends BsonValue {
	def get(key:String):Option[BsonValue]	= value collectFirst { case (k,v) if (k == key) => v }
	def ++ (that:BsonDocument):BsonDocument	= BsonDocument(this.value ++ that.value)
	def valueMap:Map[String,BsonValue]		= value.toMap
}

object BsonArray {
	val empty	= BsonArray(Vector.empty)
	
	object Var {
		def apply(it:BsonValue*):BsonArray						= BsonArray(it.toVector)
		def unapplySeq(it:BsonArray):Option[ISeq[BsonValue]]	= Some(it.value)
	}
}
final case class BsonArray(value:ISeq[BsonValue])							extends BsonValue {
	def get(index:Int):Option[BsonValue]	= value lift index
	def ++ (that:BsonArray):BsonArray		= BsonArray(this.value ++ that.value)
}

final case class BsonBinary(value:Array[Byte], subtype:BsonBinaryType)		extends BsonValue {
	// Array doesn't have useful equals/hashCode implementations
	override def equals(that:Any):Boolean	= that match {
		case that:BsonBinary	if that canEqual this	=> (this.subtype == that.subtype) && (JArrays equals (this.value, that.value))
		case _											=> false
	}
	def canEqual(that:Any):Boolean	= that.isInstanceOf[BsonBinary]
	override def hashCode():Int		= subtype.hashCode + (JArrays hashCode value)
}

// deprecated
// case object BsonUndefined											extends BsonValue

final case class BsonObjectId(bytes:Array[Byte])							extends BsonValue {
	// Array doesn't have useful equals/hashCode implementations
	override def equals(that:Any):Boolean	= that match {
		case that:BsonObjectId	if that canEqual this	=> (JArrays equals (this.bytes, that.bytes))
		case _											=> false
	}
	def canEqual(that:Any):Boolean	= that.isInstanceOf[BsonObjectId]
	override def hashCode():Int		= (JArrays hashCode bytes)
}
/*
case class BsonObjectId(seconds:Int, machine:Tri, pid:Short, inc:Tri)	extends BsonValue
// 3 bytes, the highest one must always be 0
case class Tri(value:Int)
*/

object BsonBoolean {
	def apply(value:Boolean):BsonBoolean			= if (value) BsonTrue else BsonFalse
	def unapply(value:BsonBoolean):Option[Boolean]	= Some(value.value)
}
sealed abstract class BsonBoolean extends BsonValue {
	def value:Boolean	= this match {
		case BsonTrue	=> true
		case BsonFalse	=> false
	}
}
case object BsonTrue	extends BsonBoolean
case object BsonFalse	extends BsonBoolean

final case class BsonDate(value:MilliInstant)								extends BsonValue

case object BsonNull 														extends BsonValue

final case class BsonRegex(pattern:String, options:Set[BsonRegexOption])	extends BsonValue

// deprecated
// case class BsonDBPointer(collection:String, objectId:BsonObjectId)	extends BsonValue

// BETTER use JSExpression in here?
final case class BsonCode(code:String)										extends BsonValue

final case class BsonSymbol(value:Symbol)									extends BsonValue

final case class BsonCodeInScope(code:String, scope:BsonDocument)			extends BsonValue

final case class BsonInt(value:Int)											extends BsonValue

final case class BsonTimestamp(stamp:Int, inc:Int)							extends BsonValue

final case class BsonLong(value:Long)										extends BsonValue

final case object BsonMinKey												extends BsonValue

final case object BsonMaxKey												extends BsonValue
