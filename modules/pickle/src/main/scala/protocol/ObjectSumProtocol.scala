package scbson.pickle.protocol

import scutil.base.implicits._
import scutil.lang._

import scbson.ast._
import scbson.pickle._

@deprecated("0.147.0", "use Prism")
object ObjectSumProtocol extends ObjectSumProtocol

@deprecated("0.147.0", "use Prism")
trait ObjectSumProtocol extends SumProtocol {
	@deprecated("0.147.0", "use Prism")
	def objectSumFormat[T](summands:Summand[T,_<:T]*):Format[T]	=
			sumFormat(summands.toVector map (new ObjectPartialFormat(_).pf))
	
	/** uses an object with a single field where the identifier is the key */
	@deprecated("0.147.0", "use Prism")
	private class ObjectPartialFormat[T,C<:T](summand:Summand[T,C]) {
		import summand._
		def write(value:T):Option[BsonValue]	=
				castValue(value) map { it =>
					BsonDocument.Var(identifier	-> (format get it))
				}
		def read(bson:BsonValue):Option[T]	=
				bson matchOption {
					case BsonDocument.Var((`identifier`, data))	=> format set data
				}
				
		def pf:PartialFormat[T]	= PBijection(write, read)
	}
}
