package scbson

import java.util.regex.Pattern

import scutil.Functions._
import scutil.Implicits._
import scutil.time._

object BSONValue {
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
		else sys error ("unexpected BSONValue: " + clazz)
	}
}
sealed abstract class BSONValue

case class BSONDouble(value:Double)									extends BSONValue

case class BSONString(value:String)									extends BSONValue

object BSONDocument {
	val empty	= BSONDocument(Seq.empty)
}
case class BSONDocument(value:Seq[(String,BSONValue)])				extends BSONValue {
	def valueMap:Map[String,BSONValue]		= value.toMap
	def ++ (that:BSONDocument):BSONDocument	= BSONDocument(this.value ++ that.value)
}

object BSONArray {
	val empty	= BSONArray(Seq.empty)
}
case class BSONArray(value:Seq[BSONValue])							extends BSONValue {
	def ++ (that:BSONArray):BSONArray	= BSONArray(this.value ++ that.value)
}

case class BSONBinary(value:Array[Byte], subtype:BSONBinaryType)	extends BSONValue

// deprecated
// case object BSONUndefined											extends BSONValue

case class BSONObjectId(bytes:Array[Byte])							extends BSONValue

case class BSONBoolean(value:Boolean)								extends BSONValue

case class BSONDate(value:Instant)									extends BSONValue

case object BSONNull 												extends BSONValue

case class BSONRegex(pattern:String, options:Set[BSONRegexOption])	extends BSONValue

// deprecated
// case class BSONDBPointer(collection:String, objectId:BSONObjectId)	extends BSONValue

// TODO use JSExpression in here?
case class BSONCode(code:String)									extends BSONValue

case class BSONSymbol(value:Symbol)									extends BSONValue

case class BSONCodeInScope(code:String, scope:BSONDocument)			extends BSONValue

case class BSONInt(value:Int)										extends BSONValue

case class BSONTimestamp(stamp:Int, inc:Int)						extends BSONValue

case class BSONLong(value:Long)										extends BSONValue

case object BSONMinKey												extends BSONValue

case object BSONMaxKey												extends BSONValue

//------------------------------------------------------------------------------

object BSONVarArray {
	def apply(it:BSONValue*):BSONArray					= BSONArray(it)
	def unapplySeq(it:BSONArray):Option[Seq[BSONValue]]	= Some(it.value)
}

object BSONVarDocument {
	def apply(it:(String,BSONValue)*):BSONDocument					= BSONDocument(it)
	def unapplySeq(it:BSONDocument):Option[Seq[(String,BSONValue)]]	= Some(it.value)
}

//------------------------------------------------------------------------------

object BSONBinaryType {
	def fromId(id:Byte):BSONBinaryType	= id match {
		case 0x00	=> BinaryGeneric
		case 0x01	=> BinaryFunction
		case 0x02	=> BinaryOld
		case 0x03	=> BinaryUUID
		case 0x05	=> BinaryMD5
		case 0x80	=> BinaryCustom
	}
	def toId(typ:BSONBinaryType):Byte	= typ match {
		case BinaryGeneric	=> 0x00
		case BinaryFunction	=> 0x01
		case BinaryOld		=> 0x02
		case BinaryUUID		=> 0x03
		case BinaryMD5		=> 0x05
		case BinaryCustom	=> 0x80.toByte
	}
}
sealed abstract class BSONBinaryType
case object BinaryGeneric	extends BSONBinaryType
case object BinaryFunction	extends BSONBinaryType
case object BinaryOld		extends BSONBinaryType
case object BinaryUUID		extends BSONBinaryType
case object BinaryMD5		extends BSONBinaryType
case object BinaryCustom	extends BSONBinaryType

sealed abstract class BSONRegexOption
case object RegexCaseInsensitive	extends BSONRegexOption	// i	CASE_INSENSITIVE
case object RegexMultiline			extends BSONRegexOption	// m	MULTILINE
case object RegexExtended			extends BSONRegexOption	// x	COMMENTS
case object RegexDotall				extends BSONRegexOption	// s	DOTALL

//case class BSONObjectId(seconds:Int, machine:Tri, pid:Short, inc:Tri)	extends BSONValue
// 3 bytes, the highest one must always be 0
// case class Tri(value:Int)
