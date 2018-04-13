package scbson.pickle.protocol

object FullProtocol extends FullProtocol

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