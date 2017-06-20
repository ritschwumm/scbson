package scbson.serialization

import scutil.base.implicits._
import scutil.lang._

import scbson._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:ISeq[(String,T)]):Format[T]	=
			Format[T](
				values mapToMap { case (k,v) => (v, BSONString(k)) },
				values mapToMap { case (k,v) => (BSONString(k), v) }
			)
}
