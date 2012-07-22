package scbson

import scutil.lang._

/** typeclass-bases, bidirectional BSON serialization */
package object serialization {
	import BSONSerializationUtil._
	
	type BSONFormat[T]	= Bijection[T,BSONValue]
	
	def BSONFormat[T](writeFunc:T=>BSONValue, readFunc:BSONValue=>T):BSONFormat[T]	= 
			Bijection(writeFunc, readFunc)
	
	// TODO ugly hack
	def BSONFormatSubtype[T,U<:BSONValue](writeFunc:T=>BSONValue, readFunc:U=>T):BSONFormat[T]	= 
			BSONFormat[T](writeFunc, it => readFunc(downcast(it)))
		
	def BSONFormatLazy[T](sub: =>BSONFormat[T]):BSONFormat[T]	= new Bijection[T,BSONValue] {
		lazy val delegate = sub
		def write(t:T):BSONValue	= delegate write t
		def read(s:BSONValue):T		= delegate read s
	}
	
	//------------------------------------------------------------------------------
	
	def bsonFormat[T:BSONFormat]	= implicitly[BSONFormat[T]]
	
	def doWrite[T:BSONFormat](it:T):BSONValue	= bsonFormat[T] write it
	def doRead[T:BSONFormat](it:BSONValue):T	= bsonFormat[T] read it
}
