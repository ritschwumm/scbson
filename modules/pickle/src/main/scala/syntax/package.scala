package scbson.pickle.syntax

import scbson.ast._
import scbson.pickle._

package object syntax {
	// NOTE must not be passed null values

	def bsonArray(values:BsonWrapper*):BsonArray	=
		BsonArray(values.toVector map { _.unwrap })

	def bsonDocument(values:(String,BsonWrapper)*):BsonDocument	=
		BsonDocument(values.toVector map { case (k, v) => (k, v.unwrap) })

	def bsonSimple(value:BsonWrapper):BsonValue	=
		value.unwrap

	//------------------------------------------------------------------------------

	implicit def toBsonWrapper[T:Format](it:T):BsonWrapper	=
		new BsonWrapper(format[T] get it)
}
