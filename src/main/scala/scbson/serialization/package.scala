package scbson

import scutil.lang._

/** typeclass-bases, bidirectional BSON serialization */
package object serialization {
	import BSONSerializationUtil._
	
	/** convert values to BSON and back */
	type Format[T]	= Bijection[T,BSONValue]
	
	/** create a Format from the two halves of a Bijection */
	def Format[T](writeFunc:T=>BSONValue, readFunc:BSONValue=>T):Format[T]	= 
			Bijection(writeFunc, readFunc)
	
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def SubtypeFormat[T,U<:BSONValue](writeFunc:T=>BSONValue, readFunc:U=>T):Format[T]	= 
			Format[T](writeFunc, it => readFunc(downcast(it)))
	
	/** delay the construction of an actual Format until it's used */
	def LazyFormat[T](sub: =>Format[T]):Format[T]	= new Bijection[T,BSONValue] {
		lazy val delegate = sub
		def write(t:T):BSONValue	= delegate write t
		def read(s:BSONValue):T		= delegate read s
	}
	
	//------------------------------------------------------------------------------
	
	/** provide a Format for a specific value type */
	def format[T:Format]	= implicitly[Format[T]]
	
	/** encode a value into its BSON representation using an implicitly provided Format */
	def doWrite[T:Format](it:T):BSONValue	= format[T] write it
	/** decode a value from its BSON representation using an implicitly provided Format */
	def doRead[T:Format](it:BSONValue):T	= format[T] read it
}
