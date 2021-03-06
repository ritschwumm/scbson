package scbson.pickle.protocol

import scutil.lang._

import scbson.pickle._

object ViaProtocol extends ViaProtocol

trait ViaProtocol {
	def viaFormat[S,T:Format](adapter:Bijection[S,T]):Format[S]	=
		adapter andThen format[T]

	def viaFunctionsFormat[S,T:Format](writeFunc:S=>T, readFunc:T=>S):Format[S]	=
		viaFormat(Bijection(writeFunc, readFunc))
}
