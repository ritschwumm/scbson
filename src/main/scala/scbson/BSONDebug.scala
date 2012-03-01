package scbson

import java.util.TimeZone
import java.util.Date

import scutil.Implicits._
import scutil.time._

import scbson.js._

// @see http://www.mongodb.org/display/DOCS/Mongo+Extended+JSON
// @see mongo/jstests/shelltypes.js
// NOTE BSONTimestamp is { "t" : 1293765911000, "i" : 1 }

/** convert BSONValue into a JSON-like string  */
object BSONDebug {
	// TODO some JSCall should be JSNew
	def stringify(bson:BSONValue):String	= JSUnparser unparse jsify(bson)
	
	def jsify(bson:BSONValue):JSExpression	= bson match {
		case BSONDouble(value)				=> JSNumber(value)
		case BSONString(value)				=> JSString(value)
		case BSONDocument(value)			=> JSObject(value map { case (k,v) => (k,jsify(v)) })
		case BSONArray(value)				=> JSArray(value map jsify)
		case BSONBinary(value, subtype)		=> JSCall("BinData",		Seq(JSNumber(BSONBinaryType toId subtype),	 JSString("...")))	// TODO base64
		// case BSONUndefined						=> sys error "deprecated"
		case BSONObjectId(bytes)			=> JSCall("ObjectId",		Seq(JSString(hexBytes(bytes))))
		case BSONBoolean(value)				=> JSBoolean(value)
		case BSONDate(value)				=> JSCall("ISODate",		Seq(JSString(isoDate(value))))
		case BSONNull						=> JSNull
		case BSONRegex(pattern, options)	=> JSRegexp(pattern, regexpOptions(options))											// TODO irl only i and m are allowed
		// case BSONDBPointer(collection, objectId)	=> sys error "deprecated"	// com.mongodb.DBRef
		case BSONCode(value)				=> JSCall("Code",			Seq(JSString(value)))
		case BSONSymbol(value)				=> JSCall("Symbol",			Seq(JSString(value.toString)))				// TODO check
		case BSONCodeInScope(value, scope)	=> JSCall("Code",			Seq(JSString(value), jsify(scope)))
		case BSONInt(value)					=> JSCall("NumberInt",		Seq(JSString(value.toString)))
		// alternative "{ " + writeString("t") + ": " + stamp.toString + ", " + writeString("i") + ": " + inc + " }"
		case BSONTimestamp(stamp,inc)		=> JSCall("Timestamp",		Seq(JSNumber(stamp), JSNumber(inc)))
		case BSONLong(value)				=> JSCall("NumberLong",		Seq(JSString(value.toString)))
		case BSONMinKey						=> JSCall("MinKey",			Seq.empty)
		case BSONMaxKey						=> JSCall("MaxKey",			Seq.empty)
	}
	
	private def regexpOptions(options:Set[BSONRegexOption]):String	=
			//### only im are allowed in regex literals!
			options map {
				case RegexCaseInsensitive	=> "i"
				case RegexMultiline			=> "m"
				case RegexExtended			=> "x"
				case RegexDotall			=> "s"
			} mkString ""
			
	private def hexBytes(bytes:Array[Byte]):String	= 
			bytes map { it => "%02x" format (it.toInt & 0xff) } mkString ""
		
	private def isoDate(instant:Instant):String	=
			new Date(instant.millis) format ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone getTimeZone "GMT")
		
	/*
	private def binaryType(typ:BSONBinaryType):String	=
			typ match {
				case BinaryGeneric	=> "generic"
				case BinaryFunction	=> "function"
				case BinaryOld		=> "old"
				case BinaryUUID		=> "uuid"
				case BinaryMD5		=> "md5"
				case BinaryCustom	=> "custom"
			}
	*/
}
