package scbson.js

object JSUnparser {
	def unparse(expr:JSExpression):String	= expr match {
		case JSUnparsed(code)							=> code
		case JSNull										=> "null"
		case JSString(value)							=> value map { escapeStringChar(_,true,false) } mkString("\"","","\"")
		case JSRegexp(pattern, options)					=> "/" + pattern + "/" + options
		case JSBoolean(value:Boolean)					=> value.toString
		case JSNumber(value:Number)						=> value.toString
		case JSArray(items)								=> items map unparse mkString ("[", ", ", "]")
		case JSObject(items)							=> items map { case (k,v) => unparse(JSString(k)) + ": " + unparse(v) } mkString ("{", ", ", "}")
		// case JSPrefixOp(operator, argument)				=> operator + unparse(argument)
		// case JSSuffixOp(operator, argument)				=> unparse(argument) + operator
		// case JSInfixOp(operator, argument1, argument2)	=> unparse(argument1) + operator + unparse(argument2)
		// case JSQuestion(condition, trueCase, falseCase)	=> unparse(condition) + "?" + unparse(trueCase) + ":" + falseCase
		case JSNew(function, arguments)					=> "new " + unparse(JSCall(function, arguments))
		case JSCall(function, arguments)				=> function + (arguments map unparse mkString ("(", ", ", ")"))
	}

	private def escapeStringChar(char:Char, doubleQuote:Boolean=true, singleQuote:Boolean=true):String	= char match {
		case '"'	if doubleQuote	=> "\\\""
		case '\''	if singleQuote	=> "\\\""
		case '\\'					=>	"\\\\"
		case '\b'					=> "\\b"
		case '\f'					=> "\\f"
		case '\n'					=> "\\n"
		case '\r'					=> "\\r"
		case '\t'					=> "\\t"
		case c		if c < 32		=> "\\u%04x" format c.toInt
		case c 						=> c.toString
	}
	
	// private def indent(s:String):String	= 
	// 		s replaceAll ("(?m)^", "\t")
}
