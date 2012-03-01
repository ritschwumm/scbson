package scbson.js

object JSUtil {
	def quoteRegexp(s:String):String	= s flatMap quoteRegexpChar
		
	private def quoteRegexpChar(char:Char):String	= char match {
		case	'(' | ')' | 
				'{' | '}' | 
				'[' | ']' | 
				'|' | '+' |
				'*' | '?' | 
				'\\' | '/'	=> "\\" + char.toString
		case '\r'			=> "\\r"
		case '\n'			=> "\\n"
		case '\t'			=> "\\t"
		case '\f'			=> "\\f"
		case 11				=> "\\v"
		case x if x < 32	=> "\\u%04x" format x.toInt
		case x				=> x.toString
	}
}
