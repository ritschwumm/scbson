package scbson

/** helper to allow easy construction and pattern matching on BSONArray instances */
object BSONVarArray {
	def apply(it:BSONValue*):BSONArray					= BSONArray(it)
	def unapplySeq(it:BSONArray):Option[Seq[BSONValue]]	= Some(it.value)
}

/** helper to allow easy construction and pattern matching on BSONDocument instances */
object BSONVarDocument {
	def apply(it:(String,BSONValue)*):BSONDocument					= BSONDocument(it)
	def unapplySeq(it:BSONDocument):Option[Seq[(String,BSONValue)]]	= Some(it.value)
}
