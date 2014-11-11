package scbson

import scbson._
import scbson.serialization._

package object syntax {
	// NOTE must no be passed null values
	
	def bsonArray(values:BSONWrapper*):BSONArray	=
			BSONArray(values.toVector map { _.unwrap })
		
	def bsonDocument(values:(String,BSONWrapper)*):BSONDocument	=
			BSONDocument(values.toVector map { case (k, v) => (k, v.unwrap) })
		
	def bsonSimple(value:BSONWrapper):BSONValue	=
			value.unwrap
		
	//------------------------------------------------------------------------------
		
	final case class BSONWrapper(unwrap:BSONValue)
	
	implicit def toBSONWrapper[T:Format](it:T):BSONWrapper	=
			new BSONWrapper(format[T] write it)
}
