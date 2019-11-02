package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object SeqProtocol extends SeqProtocol

trait SeqProtocol {
	implicit def SeqFormat[T:Format]:Format[Seq[T]] =
			Format[Seq[T]](
				(out:Seq[T])	=> BsonArray(out map doWrite[T]),
				(in:BsonValue)	=> arrayValue(in) map doReadUnsafe[T]
			)

	/*
	implicit def SeqFormat[T:Format]:Format[Seq[T]]	= {
		val sub	= format[T]
		SubtypeFormat[Seq[T],BsonArray](
			it	=> BsonArray(it map doWrite[T]),
			it	=> it.value map sub.read
		)
	}
	*/
}
