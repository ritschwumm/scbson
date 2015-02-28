package scbson.serialization

import scbson._

import BSONSerializationUtil._

object EitherProtocol extends EitherProtocol

trait EitherProtocol {
	private val rightTag	= "right"
	private val leftTag		= "left"
	
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	=
			Format[Either[L,R]](
				_ match {
					case Right(value)	=>  BSONVarDocument(
						rightTag	-> doWrite[R](value)
					)
					case Left(value)	=>  BSONVarDocument(
						leftTag		-> doWrite[L](value)
					)
				},
				(in:BSONValue)	=> {
					val map	= documentMap(in)
					(map get leftTag, map get rightTag) match {
						case (None, Some(js))	=> Right(doRead[R](js))
						case (Some(js), None)	=> Left(doRead[L](js))
						case _					=> fail("unexpected either")
					}
				}
			)
}
