package scbson.pickle.protocol

@deprecated("0.147.0", "use StandardProtocol2 without SumProtocol and ObjectSumProtocol")
object StandardProtocol extends StandardProtocol

@deprecated("0.147.0", "use StandardProtocol2 without SumProtocol and ObjectSumProtocol")
trait StandardProtocol
		extends	NativeProtocol
		with	ViaProtocol
		with	ISeqProtocol
		with	CollectionProtocol
		with	OptionProtocol
		with	EitherProtocol
		with	SumProtocol
		with	ObjectSumProtocol
		with	EnumProtocol
		with	TupleProtocol
		with	CaseClassProtocol
		with	IdentityProtocol
