package scbson.ast

import scutil.core.implicits._
import scutil.lang._

object BsonBinaryType {
	val prism	=
		Prism[Byte,BsonBinaryType](
			_ matchOption {
				case 0x00	=> BinaryGeneric
				case 0x01	=> BinaryFunction
				case 0x02	=> BinaryOld
				case 0x03	=> BinaryUUID
				case 0x05	=> BinaryMD5
				case 0x80	=> BinaryCustom
			},
			_ match {
				case BinaryGeneric	=> 0x00
				case BinaryFunction	=> 0x01
				case BinaryOld		=> 0x02
				case BinaryUUID		=> 0x03
				case BinaryMD5		=> 0x05
				case BinaryCustom	=> 0x80.toByte
			}
		)
}

sealed abstract class BsonBinaryType
case object BinaryGeneric	extends BsonBinaryType
case object BinaryFunction	extends BsonBinaryType
case object BinaryOld		extends BsonBinaryType
case object BinaryUUID		extends BsonBinaryType
case object BinaryMD5		extends BsonBinaryType
case object BinaryCustom	extends BsonBinaryType
