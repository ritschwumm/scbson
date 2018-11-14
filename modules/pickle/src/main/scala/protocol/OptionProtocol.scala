package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object OptionProtocol extends OptionProtocol

trait OptionProtocol {
	private val someTag	= "some"
	private val noneTag	= "none"

	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				_ match {
					case Some(value)	=> BsonDocument.Var(someTag -> doWrite(value))
					case None			=> BsonDocument.Var(noneTag -> BsonBoolean(true))
				},
				(in:BsonValue)	=> {
					val map	= documentMap(in)
					(map get someTag, map get noneTag) match {
						case (Some(js), None)	=> Some(doReadUnsafe[T](js))
						case (None, Some(js))	=> None
						case _					=> fail("unexpected option")
					}
				}
			)
}
