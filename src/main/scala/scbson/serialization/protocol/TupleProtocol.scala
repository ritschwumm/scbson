package scbson.serialization

import scbson._

import BSONSerializationUtil._

object TupleProtocol extends TupleProtocol

trait TupleProtocol extends TupleProtocolGenerated {
	/*
	implicit def Tuple2Format[T1:Format,T2:Format]:Format[(T1,T2)]	= new Format[(T1,T2)] {
		def write(out:(T1,T2)):BSONValue	= {
			BSONArray(Seq(
				doWrite[T1](out._1), 
				doWrite[T2](out._2)
			))
		}
		def read(in:BSONValue):(T1,T2)	= {
			val	arr	= forceArray(in)
			(
				doRead[T1](arr(0)), 
				doRead[T2](arr(1))
			)
		}
	}
	*/
	
	protected def forceArray(in:BSONValue):Seq[BSONValue]	=
			downcast[BSONArray](in).value
}
