package scbson.serialization

import scbson._

private object Summand {
	val typeTag	= ""
	
	implicit def fromClass[T:BSONFormat](clazz:Class[T]):Summand[T]	= 
			Summand(clazz.getName, clazz, bsonFormat[T])
	implicit def fromBSONFormat[T:Manifest](format:BSONFormat[T]):Summand[T]	= 
			Summand(manifest[T].erasure.getName, manifest[T].erasure, format)
		
	implicit def fromClassWithIdentifier[T:BSONFormat](id:(String,Class[T])):Summand[T]	= 
			Summand(id._1, id._2, bsonFormat[T])
	implicit def fromBSONFormatWithIdentifier[T:Manifest](id:(String, BSONFormat[T])):Summand[T]	=
			Summand(id._1, manifest[T].erasure, id._2)
}

private case class Summand[T](identifier:String, clazz:Class[_], format:BSONFormat[T]) {
	def this(clazz:Class[_], format:BSONFormat[T])	= this(clazz.getName, clazz, format)
}
