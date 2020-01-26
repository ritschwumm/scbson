package scbson.pickle.protocol

import scbson.pickle._

object OldOptionProtocol extends OldOptionProtocol

trait OldOptionProtocol {
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= OptionProtocols.adtFormat
}
