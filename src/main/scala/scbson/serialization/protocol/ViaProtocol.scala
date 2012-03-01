package scbson.serialization

import scutil.Bijection
import scutil.Marshaller

import scbson._

object ViaProtocol extends ViaProtocol

trait ViaProtocol {
	def via[S,T:BSONFormat](adapter:Bijection[S,T]):BSONFormat[S]	= 
			adapter andThen bsonFormat[T]
			
	def viaFunctions[S,T:BSONFormat](writeFunc:S=>T, readFunc:T=>S):BSONFormat[S]	= 
			via(Bijection(writeFunc, readFunc))
		
	def newType[S,T:BSONFormat](marshaller:Marshaller[S,T]):BSONFormat[S]	=
			via(marshaller.asBijectionFailing)

	def newTypeFunctions[S,T:BSONFormat](applyFunc:T=>S, unapplyFunc:S=>Option[T]):BSONFormat[S]	=
			via(Bijection marshallerFailing (applyFunc, unapplyFunc) inverse)
}
