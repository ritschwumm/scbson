package scbson

import scutil.lang._

/** typeclass-bases, bidirectional BSON serialization */
package object serialization {
	import BSONSerializationUtil._
	
	/** convert values to BSON and back */
	type BSONFormat[T]	= Bijection[T,BSONValue]
	
	/** create a BSONFormat from the two halves of a Bijection */
	def BSONFormat[T](writeFunc:T=>BSONValue, readFunc:BSONValue=>T):BSONFormat[T]	= 
			Bijection(writeFunc, readFunc)
	
	/** this is a bit of a hack to force a specific constructor to be used for decoding */
	def BSONFormatSubtype[T,U<:BSONValue](writeFunc:T=>BSONValue, readFunc:U=>T):BSONFormat[T]	= 
			BSONFormat[T](writeFunc, it => readFunc(downcast(it)))
	
	/** delay the construction of an actual BSONFormat until it's used */
	def BSONFormatLazy[T](sub: =>BSONFormat[T]):BSONFormat[T]	= new Bijection[T,BSONValue] {
		lazy val delegate = sub
		def write(t:T):BSONValue	= delegate write t
		def read(s:BSONValue):T		= delegate read s
	}
	
	//------------------------------------------------------------------------------
	
	/** provide a BSONFormat for a specific value type */
	def bsonFormat[T:BSONFormat]	= implicitly[BSONFormat[T]]
	
	/** encode a value into its BSON representation using an implicitly provided BSONFormat */
	def doWrite[T:BSONFormat](it:T):BSONValue	= bsonFormat[T] write it
	/** decode a value from its BSON representation using an implicitly provided BSONFormat */
	def doRead[T:BSONFormat](it:BSONValue):T	= bsonFormat[T] read it
}
