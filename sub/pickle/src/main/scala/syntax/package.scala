package scbson.pickle.syntax

import scbson.ast._
import scbson.pickle._

package object syntax {
	// NOTE must not be passed null values
	
	def bsonArray(values:BSONWrapper*):BSONArray	=
			BSONArray(values.toVector map { _.unwrap })
		
	def bsonDocument(values:(String,BSONWrapper)*):BSONDocument	=
			BSONDocument(values.toVector map { case (k, v) => (k, v.unwrap) })
		
	def bsonSimple(value:BSONWrapper):BSONValue	=
			value.unwrap
		
	//------------------------------------------------------------------------------
	
	implicit def toBSONWrapper[T:Format](it:T):BSONWrapper	=
			new BSONWrapper(format[T] write it)
}
