package scbson.pickle.protocol

import scbson.ast._
import scbson.pickle._
import scbson.pickle.BsonPickleUtil._

object EitherProtocol extends EitherProtocol

trait EitherProtocol {
	private val rightTag	= "right"
	private val leftTag		= "left"

	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	=
		Format[Either[L,R]](
			_ match {
				case Right(value)	=>  BsonDocument.Var(
					rightTag	-> doWrite[R](value)
				)
				case Left(value)	=>  BsonDocument.Var(
					leftTag		-> doWrite[L](value)
				)
			},
			(in:BsonValue)	=> {
				val map	= documentMap(in)
				(map get leftTag, map get rightTag) match {
					case (None, Some(js))	=> Right(doReadUnsafe[R](js))
					case (Some(js), None)	=> Left(doReadUnsafe[L](js))
					case _					=> fail("unexpected either")
				}
			}
		)
}
