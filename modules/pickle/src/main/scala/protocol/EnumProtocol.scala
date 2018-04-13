package scbson.pickle.protocol

import scutil.base.implicits._
import scutil.lang._

import scbson.ast._
import scbson.pickle._

object EnumProtocol extends EnumProtocol

trait EnumProtocol {
	def enumFormat[T](values:ISeq[(String,T)]):Format[T]	=
			Format[T](
				values mapToMap { case (k,v) => (v, BsonString(k)) },
				values mapToMap { case (k,v) => (BsonString(k), v) }
			)
}