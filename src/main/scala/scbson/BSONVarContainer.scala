package scbson

import scutil.lang._

/** helper to allow easy construction and pattern matching on BSONArray instances */
object BSONVarArray {
	def apply(it:BSONValue*):BSONArray						= BSONArray(it.toVector)
	def unapplySeq(it:BSONArray):Option[ISeq[BSONValue]]	= Some(it.value)
}

/** helper to allow easy construction and pattern matching on BSONDocument instances */
object BSONVarDocument {
	def apply(it:(String,BSONValue)*):BSONDocument						= BSONDocument(it.toVector)
	def unapplySeq(it:BSONDocument):Option[ISeq[(String,BSONValue)]]	= Some(it.value)
}
