package scbson.serialization

import scbson._

import BSONSerializationUtil._

object OptionProtocol extends OptionProtocol

trait OptionProtocol {
	private val someTag	= "some"
	private val noneTag	= "none"
	
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				_ match {
					case Some(value)	=> BSONDocument.Var(someTag -> doWrite(value))
					case None			=> BSONDocument.Var(noneTag -> BSONBoolean(true))
				},
				(in:BSONValue)	=> {
					val map	= documentMap(in)
					(map get someTag, map get noneTag) match {
						case (Some(js), None)	=> Some(doRead[T](js))
						case (None, Some(js))	=> None
						case _					=> fail("unexpected option")
					}
				}
			)
}
