package scbson.serialization

import scbson._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumBSONFormat[T](values:Seq[(String,T)]):BSONFormat[T]	=
			BSONFormat[T](
					values map { case (k,v) => (v, BSONString(k)) } toMap,
					values map { case (k,v) => (BSONString(k), v) } toMap)
}
