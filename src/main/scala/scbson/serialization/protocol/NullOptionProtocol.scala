package scbson.serialization

import scbson._

import BSONSerializationUtil._

object NullOptionProtocoll extends NullOptionProtocoll

trait LowPrioNullOptionProtocoll {
	// alternative value or null
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				_ match {
					case None			=> BSONNull
					case Some(value)	=> doWrite(value)
				},
				_ match {
					case BSONNull	=> None
					case js			=> Some(doRead[T](js))
				}
			)
}

trait NullOptionProtocoll extends LowPrioNullOptionProtocoll {
	private val someTag	= "some"
	private val noneTag	= "none"
	
	// alternative {some} or {none}
	implicit def OptionOptionFormat[T](implicit ev:Format[Option[T]]):Format[Option[Option[T]]]	=
			Format[Option[Option[T]]](
				_ match {
					case Some(value)	=> BSONVarDocument(someTag -> doWrite(value))
					case None			=> BSONVarDocument(noneTag -> BSONTrue)
				},
				(in:BSONValue)	=> {
					val map	= documentMap(in)
					(map get someTag, map get noneTag) match {
						case (Some(js), None)	=> Some(doRead[Option[T]](js))
						case (None, Some(js))	=> None
						case _					=> fail("unexpected option")
					}
				}
			)
}
