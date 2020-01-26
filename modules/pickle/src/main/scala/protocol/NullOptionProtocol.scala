package scbson.pickle.protocol

import scbson.pickle._

object NullOptionProtocol extends NullOptionProtocol

trait NullOptionProtocol extends NullOptionProtocolLow {
	implicit def OptionOptionFormat[T](implicit ev:Format[Option[T]]):Format[Option[Option[T]]]	= OptionProtocols.adtFormat
}

trait NullOptionProtocolLow {
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= OptionProtocols.nullFormat
}
