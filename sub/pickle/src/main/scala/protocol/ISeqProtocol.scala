package scbson.pickle.protocol

import scutil.lang._

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BSONPickleUtil._

object ISeqProtocol extends ISeqProtocol

trait ISeqProtocol {
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]] =
			Format[ISeq[T]](
				(out:ISeq[T])	=> BSONArray(out map doWrite[T]),
				(in:BSONValue)	=> arrayValue(in) map doReadUnsafe[T]
			)
			
	/*
	implicit def ISeqFormat[T:Format]:Format[ISeq[T]]	= {
		val sub	= format[T]
		SubtypeFormat[ISeq[T],BSONArray](
			it	=> BSONArray(it map doWrite[T]),
			it	=> it.value map sub.read
		)
	}
	*/
}
