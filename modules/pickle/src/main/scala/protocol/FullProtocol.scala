package scbson.pickle.protocol

@deprecated("0.147.0", "use StandardProtocol")
object FullProtocol extends FullProtocol

@deprecated("0.147.0", "use StandardProtocol")
trait FullProtocol
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
