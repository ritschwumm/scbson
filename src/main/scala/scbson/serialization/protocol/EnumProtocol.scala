package scbson.serialization

import scutil.lang._

import scbson._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:ISeq[(String,T)]):Format[T]	=
			Format[T](
				values map { case (k,v) => (v, BSONString(k)) } toMap,
				values map { case (k,v) => (BSONString(k), v) } toMap
			)
}
