package scbson.js

sealed trait JSExpression

case class JSUnparsed(code:String)																extends JSExpression
case object JSNull 																				extends JSExpression
case class JSBoolean(value:Boolean) 															extends JSExpression
case class JSNumber(value:Number) 																extends JSExpression
case class JSString(value:String)																extends JSExpression
case class JSRegexp(pattern:String, options:String)												extends JSExpression
case class JSArray(items:Seq[JSExpression]) 													extends JSExpression
case class JSObject(items:Seq[(String,JSExpression)])											extends JSExpression
// case class JSPrefixOp(operator:String, argument:JSExpression)									extends JSExpression
// case class JSSuffixOp(operator:String, argument:JSExpression)									extends JSExpression
// case class JSInfixOp(operator:String, argument1:JSExpression, argument2:JSExpression)			extends JSExpression
// case class JSQuestion(condition:JSExpression, trueCase:JSExpression, falseCase:JSExpression)	extends JSExpression
case class JSNew(function:String, arguments:Seq[JSExpression])									extends JSExpression
case class JSCall(function:String, arguments:Seq[JSExpression])									extends JSExpression

//------------------------------------------------------------------------------

object JSVarArray {
	def apply(it:JSExpression*):JSArray						= JSArray(it)
	def unapplySeq(it:JSArray):Option[Seq[JSExpression]]	= Some(it.items)
}

object JSVarObject {
	def apply(it:(String,JSExpression)*):JSObject					= JSObject(it)
	def unapplySeq(it:JSObject):Option[Seq[(String,JSExpression)]]	= Some(it.items)
}
