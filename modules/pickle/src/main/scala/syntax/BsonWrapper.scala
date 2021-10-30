package scbson.pickle.syntax

import scala.language.implicitConversions

import scbson.ast._
import scbson.pickle._

object BsonWrapper {
	implicit def toBsonWrapper[T:Format](it:T):BsonWrapper	=
		new BsonWrapper(format[T] get it)
}

final case class BsonWrapper(unwrap:BsonValue)
