package scbson.ast

import java.util.{ Arrays => JArrays }

import scutil.base.implicits._
import scutil.lang._
import scutil.time.MilliInstant

object BSONValue {
	val theNull:BSONValue												= BSONNull
	val theMinKey:BSONValue												= BSONMinKey
	val theMaxKey:BSONValue												= BSONMaxKey
	def mkNull(it:Unit):BSONValue										= theNull
	def mkMinKey(it:Unit):BSONValue										= theMinKey
	def mkMaxKey(it:Unit):BSONValue										= theMaxKey
	
	def mkBoolean(it:Boolean):BSONValue									= BSONBoolean(it)
	def mkInt(it:Int):BSONValue											= BSONInt(it)
	def mkLong(it:Long):BSONValue										= BSONLong(it)
	def mkDouble(it:Double):BSONValue									= BSONDouble(it)
	def mkString(it:String):BSONValue									= BSONString(it)
	def mkSymbol(it:Symbol):BSONValue									= BSONSymbol(it)
	def mkObjectId(it:Array[Byte]):BSONValue							= BSONObjectId(it)
	def mkDate(it:MilliInstant):BSONValue								= BSONDate(it)
	
	def mkTimestamp(stamp:Int, inc:Int):BSONValue						= BSONTimestamp(stamp, inc)
	def mkCode(code:String):BSONValue									= BSONCode(code)
	def mkCodeInScope(code:String, scope:BSONDocument):BSONValue		= BSONCodeInScope(code, scope)
	def mkBinary(value:Array[Byte], subtype:BSONBinaryType):BSONValue	= BSONBinary(value, subtype)
	def mkRegex(pattern:String, options:Set[BSONRegexOption]):BSONValue	= BSONRegex(pattern, options)
	
	def mkArray(it:ISeq[BSONValue]):BSONValue							= BSONArray(it)
	def mkDocument(it:ISeq[(String,BSONValue)]):BSONValue				= BSONDocument(it)

	//------------------------------------------------------------------------------
	
	def typeId[T<:BSONValue:Manifest]:Int	= {
		val clazz	= implicitly[Manifest[T]].getClass
			 if (clazz == classOf[BSONDouble])		1
		else if (clazz == classOf[BSONString])		2
		else if (clazz == classOf[BSONDocument])	3
		
		else if (clazz == classOf[BSONArray])		4
		else if (clazz == classOf[BSONBinary])		5
		// else if (clazz == classOf[BSONUndefined])	6
		else if (clazz == classOf[BSONObjectId])	7
		else if (clazz == classOf[BSONBoolean])		8
		else if (clazz == classOf[BSONDate])		9
		else if (clazz == BSONNull.getClass)		10
		else if (clazz == classOf[BSONRegex])		11
		// else if (clazz == classOf[BSONDBPointer])	12
		else if (clazz == classOf[BSONCode])		13
		else if (clazz == classOf[BSONSymbol])		14
		else if (clazz == classOf[BSONCodeInScope])	15
		else if (clazz == classOf[BSONInt])			16
		else if (clazz == classOf[BSONTimestamp])	17
		else if (clazz == classOf[BSONLong])		18
		else if (clazz == BSONMinKey.getClass)		255
		else if (clazz == BSONMaxKey.getClass)		127
		else sys error ("unexpected BSONValue: " + clazz.toString)
	}
}
sealed abstract class BSONValue {
	def asNull:Option[Unit]								= this matchOption { case BSONNull						=> ()	}
	def asMinKey:Option[Unit]							= this matchOption { case BSONMinKey					=> ()	}
	def asMaxKey:Option[Unit]							= this matchOption { case BSONMaxKey					=> ()	}
	
	def asBoolean:Option[Boolean]						= this matchOption { case BSONBoolean(x)				=> x	}
	def asInt:Option[Int]								= this matchOption { case BSONInt(x)					=> x	}
	def asLong:Option[Long]								= this matchOption { case BSONLong(x)					=> x	}
	def asDouble:Option[Double]							= this matchOption { case BSONDouble(x)					=> x	}
	def asString:Option[String]							= this matchOption { case BSONString(x)					=> x	}
	def asSymbol:Option[Symbol]							= this matchOption { case BSONSymbol(x)					=> x	}
	def asObjectId:Option[Array[Byte]]					= this matchOption { case BSONObjectId(x)				=> x	}
	def asDate:Option[MilliInstant]						= this matchOption { case BSONDate(x)					=> x	}
	
	def asCode:Option[String]							= this matchOption { case BSONCode(code)				=> code 				}
	def asCodeInScope:Option[(String,BSONDocument)]		= this matchOption { case BSONCodeInScope(code, scope)	=> (code, scope) 		}
	def asTimestamp:Option[(Int,Int)]					= this matchOption { case BSONTimestamp(stamp, inc)		=> (stamp, inc) 		}
	def asBinary:Option[(Array[Byte], BSONBinaryType)]	= this matchOption { case BSONBinary(value, subtype)	=> (value, subtype)		}
	def asRegex:Option[(String, Set[BSONRegexOption])]	= this matchOption { case BSONRegex(pattern, options)	=> (pattern, options)	}
	
	def asArray:Option[ISeq[BSONValue]]					= this matchOption { case BSONArray(x)					=> x	}
	def asDocument:Option[ISeq[(String,BSONValue)]]		= this matchOption { case BSONDocument(x)				=> x	}

	/*
	def downcastHack[T<:BSONValue]:Option[T]	=
			try { Some(asInstanceOf[T]) }
			catch { case e:ClassCastException => None }
	*/
}

final case class BSONDouble(value:Double)									extends BSONValue

final case class BSONString(value:String)									extends BSONValue

object BSONDocument {
	val empty	= BSONDocument(Vector.empty)
	
	object Var {
		def apply(it:(String,BSONValue)*):BSONDocument						= BSONDocument(it.toVector)
		def unapplySeq(it:BSONDocument):Option[ISeq[(String,BSONValue)]]	= Some(it.value)
	}
}
final case class BSONDocument(value:ISeq[(String,BSONValue)])				extends BSONValue {
	def get(key:String):Option[BSONValue]	= value collectFirst { case (k,v) if (k == key) => v }
	def ++ (that:BSONDocument):BSONDocument	= BSONDocument(this.value ++ that.value)
	def valueMap:Map[String,BSONValue]		= value.toMap
}

object BSONArray {
	val empty	= BSONArray(Vector.empty)
	
	object Var {
		def apply(it:BSONValue*):BSONArray						= BSONArray(it.toVector)
		def unapplySeq(it:BSONArray):Option[ISeq[BSONValue]]	= Some(it.value)
	}
}
final case class BSONArray(value:ISeq[BSONValue])							extends BSONValue {
	def get(index:Int):Option[BSONValue]	= value lift index
	def ++ (that:BSONArray):BSONArray		= BSONArray(this.value ++ that.value)
}

final case class BSONBinary(value:Array[Byte], subtype:BSONBinaryType)		extends BSONValue {
	// Array doesn't have useful equals/hashCode implementations
	override def equals(that:Any):Boolean	= that match {
		case that:BSONBinary	if that canEqual this	=> (this.subtype == that.subtype) && (JArrays equals (this.value, that.value))
		case _											=> false
	}
	def canEqual(that:Any):Boolean	= that.isInstanceOf[BSONBinary]
	override def hashCode():Int		= subtype.hashCode + (JArrays hashCode value)
}

// deprecated
// case object BSONUndefined											extends BSONValue

final case class BSONObjectId(bytes:Array[Byte])							extends BSONValue {
	// Array doesn't have useful equals/hashCode implementations
	override def equals(that:Any):Boolean	= that match {
		case that:BSONObjectId	if that canEqual this	=> (JArrays equals (this.bytes, that.bytes))
		case _											=> false
	}
	def canEqual(that:Any):Boolean	= that.isInstanceOf[BSONObjectId]
	override def hashCode():Int		= (JArrays hashCode bytes)
}
/*
case class BSONObjectId(seconds:Int, machine:Tri, pid:Short, inc:Tri)	extends BSONValue
// 3 bytes, the highest one must always be 0
case class Tri(value:Int)
*/

object BSONBoolean {
	def apply(value:Boolean):BSONBoolean			= if (value) BSONTrue else BSONFalse
	def unapply(value:BSONBoolean):Option[Boolean]	= Some(value.value)
}
sealed abstract class BSONBoolean extends BSONValue {
	def value:Boolean	= this match {
		case BSONTrue	=> true
		case BSONFalse	=> false
	}
}
case object BSONTrue	extends BSONBoolean
case object BSONFalse	extends BSONBoolean

final case class BSONDate(value:MilliInstant)								extends BSONValue

case object BSONNull 														extends BSONValue

final case class BSONRegex(pattern:String, options:Set[BSONRegexOption])	extends BSONValue

// deprecated
// case class BSONDBPointer(collection:String, objectId:BSONObjectId)	extends BSONValue

// BETTER use JSExpression in here?
final case class BSONCode(code:String)										extends BSONValue

final case class BSONSymbol(value:Symbol)									extends BSONValue

final case class BSONCodeInScope(code:String, scope:BSONDocument)			extends BSONValue

final case class BSONInt(value:Int)											extends BSONValue

final case class BSONTimestamp(stamp:Int, inc:Int)							extends BSONValue

final case class BSONLong(value:Long)										extends BSONValue

final case object BSONMinKey												extends BSONValue

final case object BSONMaxKey												extends BSONValue
