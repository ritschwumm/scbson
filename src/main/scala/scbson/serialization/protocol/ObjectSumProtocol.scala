package scbson.serialization

import scutil.base.implicits._
import scutil.lang._

import scbson._

object ObjectSumProtocol extends ObjectSumProtocol

trait ObjectSumProtocol extends SumProtocol {
	def objectSumFormat[T](summands:Summand[T,_<:T]*):Format[T]	=
			sumFormat(summands.toVector map (new ObjectPartialFormat(_).pf))
	
	/** uses an object with a single field where the identifier is the key */
	private class ObjectPartialFormat[T,C<:T](summand:Summand[T,C]) {
		import summand._
		def write(value:T):Option[BSONValue]	=
				castValue(value) map { it =>
					BSONVarDocument(identifier	-> (format write it))
				}
		def read(bson:BSONValue):Option[T]	=
				bson matchOption {
					case BSONVarDocument((`identifier`, data))	=> format read data
				}
				
		def pf:PartialFormat[T]	= PBijection(write, read)
	}
}
