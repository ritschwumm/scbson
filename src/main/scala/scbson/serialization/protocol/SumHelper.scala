package scbson.serialization

import scutil.Implicits._

import scbson._

import BSONSerializationUtil._

private class SumHelper[T](summands:Seq[Summand[_<:T]]) {
	def write(out:T):(BSONString,BSONValue)	= 
			try {
				// NOTE as we don't specialize to T, out is boxed
				val summand	= summands find { _.clazz.boxed isInstance out } getOrElse fail("summand not found for " + out)
				(BSONString(summand.identifier)	-> (summand.asInstanceOf[Summand[T]].format write out))
			}
			catch {
				case e:ClassCastException	=> fail("cannot write generic sum", e)
			}
			
	def read(identifier:BSONString, in:BSONValue):T	=
			try {
				val summand	= summands find { _.identifier ==== identifier.value } getOrElse fail("summand not found for " + identifier)
				summand.format read in
			}
			catch {
				case e:ClassCastException	=> fail("cannot read generic sum", e)
			}
}
