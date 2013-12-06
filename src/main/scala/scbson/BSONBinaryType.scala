package scbson

import scutil.lang._
import scutil.implicits._

object BSONBinaryType {
	val marshaller	= Marshaller[BSONBinaryType,Byte](
			_ match {
				case BinaryGeneric	=> 0x00
				case BinaryFunction	=> 0x01
				case BinaryOld		=> 0x02
				case BinaryUUID		=> 0x03
				case BinaryMD5		=> 0x05
				case BinaryCustom	=> 0x80.toByte
			},
			_ matchOption {
				case 0x00	=> BinaryGeneric
				case 0x01	=> BinaryFunction
				case 0x02	=> BinaryOld
				case 0x03	=> BinaryUUID
				case 0x05	=> BinaryMD5
				case 0x80	=> BinaryCustom
			})
}

sealed abstract class BSONBinaryType
case object BinaryGeneric	extends BSONBinaryType
case object BinaryFunction	extends BSONBinaryType
case object BinaryOld		extends BSONBinaryType
case object BinaryUUID		extends BSONBinaryType
case object BinaryMD5		extends BSONBinaryType
case object BinaryCustom	extends BSONBinaryType
