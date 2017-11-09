package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BSONPickleUtil._

object NullOptionProtocol extends NullOptionProtocol

trait LowPrioNullOptionProtocol {
	// alternative value or null
	implicit def OptionFormat[T:Format]:Format[Option[T]]	=
			Format[Option[T]](
				_ match {
					case None			=> BSONNull
					case Some(value)	=> doWrite(value)
				},
				_ match {
					case BSONNull	=> None
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
					case Some(value)	=> BSONDocument.Var(someTag -> doWrite(value))
					case None			=> BSONDocument.Var(noneTag -> BSONTrue)
				},
				(in:BSONValue)	=> {
					val map	= documentMap(in)
					(map get someTag, map get noneTag) match {
						case (Some(js), None)	=> Some(doReadUnsafe[Option[T]](js))
						case (None, Some(js))	=> None
						case _					=> fail("unexpected option")
					}
				}
			)
}
