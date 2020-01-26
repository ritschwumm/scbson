package scbson.pickle.syntax

import scbson.ast._

package object syntax {
	def bsonValue(value:BsonWrapper):BsonValue	=
		value.unwrap

	def bsonArray(values:BsonWrapper*):BsonArray	=
		BsonArray(values.toVector map { _.unwrap })

	def bsonDocument(values:(String,BsonWrapper)*):BsonDocument	=
		BsonDocument(values.toVector map { case (k, v) => (k, v.unwrap) })

	@deprecated("use bsonValue", "0.187.0")
	def bsonSimple(value:BsonWrapper):BsonValue	= bsonValue(value)
}
