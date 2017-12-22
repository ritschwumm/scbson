package scbson

import scutil.lang._

import scbson.ast._

/** typeclass-based, bidirectional Bson pickle */
package object pickle {
	import BsonPickleUtil._
	
	/** convert values to Bson and back */
	type Format[T]	= Bijection[T,BsonValue]
	
	/** create a Format from the two halves of a Bijection */
	def Format[T](writeFunc:T=>BsonValue, readFunc:BsonValue=>T):Format[T]	=
			Bijection(writeFunc, readFunc)
	
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def SubtypeFormat[T,U<:BsonValue](writeFunc:T=>BsonValue, readFunc:U=>T):Format[T]	=
			Format[T](writeFunc, it => readFunc(downcast(it)))
	
	/** delay the construction of an actual Format until it's used */
	def LazyFormat[T](sub: =>Format[T]):Format[T]	=
			Format(it => sub get it, it => sub put it)
	
	//------------------------------------------------------------------------------
	
	/** provide a Format for a specific value type */
	def format[T:Format]	= implicitly[Format[T]]
	
	/** encode a value into its Bson representation using an implicitly provided Format */
	def doWrite[T:Format](it:T):BsonValue		= format[T] get it
	/** decode a value from its Bson representation using an implicitly provided Format */
	def doReadUnsafe[T:Format](it:BsonValue):T	= format[T] put it
	
	/** decode a value from its Bson representation using an implicitly provided Format */
	def doRead[T:Format](in:BsonValue):Either[BsonUnpickleFailure,T]	=
			try {
				Right(doReadUnsafe[T](in))
			}
			catch { case e:BsonUnpickleException =>
				Left(e.failure)
			}
}
