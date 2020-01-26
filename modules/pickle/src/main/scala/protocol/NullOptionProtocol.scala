package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object NullOptionProtocol extends NullOptionProtocol

trait LowPrioNullOptionProtocol {
	// alternative value or null
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
		Format[Option[T]](
			_ match {
				case None			=> BsonNull
				case Some(value)	=> doWrite(value)
			},
			_ match {
				case BsonNull	=> None
				case js			=> Some(doReadUnsafe[T](js))
			}
		)
}

trait NullOptionProtocol extends LowPrioNullOptionProtocol {
	private val someTag	= "some"
	private val noneTag	= "none"

	// alternative {some} or {none}
	implicit def OptionOptionFormat[T](implicit ev:Format[Option[T]]):Format[Option[Option[T]]]	=
		Format[Option[Option[T]]](
			_ match {
				case Some(value)	=> BsonDocument.Var(someTag -> doWrite(value))
				case None			=> BsonDocument.Var(noneTag -> BsonTrue)
			},
			(in:BsonValue)	=> {
				val map	= documentMap(in)
				(map get someTag, map get noneTag) match {
					case (Some(js),	None)		=> Some(doReadUnsafe[Option[T]](js))
					case (None,		Some(js))	=> None
					case _						=> fail("unexpected option")
				}
			}
		)
}
