package scbson.serialization

import scutil.Implicits._
import scutil.Bijection

import scbson._

import BSONSerializationUtil._

object SumProtocol extends SumProtocol

trait SumProtocol {
	def sumBSONFormat1[T](summands:Summand[_<:T]*):BSONFormat[T]	= {
		val helper	= new SumHelper[T](summands)
		BSONFormat[T](
			out	=> {
				val (identifier,formatted)	= helper write out
				BSONArray(Seq(identifier, formatted))
			},
			in	=> {
				val BSONArray(Seq(identifier:BSONString,formatted))	= downcast[BSONArray](in)
				helper read (identifier, formatted)
			}
		)
	}
	
	def enumBSONFormat[T](values:Seq[(String,T)]):BSONFormat[T]	=
			BSONFormat[T](
					values map { case (k,v) => (v, BSONString(k)) } toMap,
					values map { case (k,v) => (BSONString(k), v) } toMap)
}
