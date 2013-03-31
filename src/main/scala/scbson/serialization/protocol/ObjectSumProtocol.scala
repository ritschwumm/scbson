package scbson.serialization

import scutil.Implicits._

import scbson._

object ObjectSumProtocol extends ObjectSumProtocol

trait ObjectSumProtocol extends SumProtocol {
	def objectSumBSONFormat[T](summands:Summand[T,_<:T]*):BSONFormat[T]	=
			sumBSONFormat(summands map (new ObjectPartialBSONFormat(_)))
	
	/** uses an object with a single field where the identifier is the key */
	private class ObjectPartialBSONFormat[T,C<:T](summand:Summand[T,C]) extends PartialBSONFormat[T] {
		import summand._
		def write(value:T):Option[BSONValue]	=
				castValue(value) map { it =>
					BSONVarDocument(identifier	-> (format write it))
				}
		def read(bson:BSONValue):Option[T]	=
				bson matchOption {
					case BSONDocument(Seq((`identifier`, data)))	=> format read data
				}
	}
}
