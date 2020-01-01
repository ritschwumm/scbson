package scbson.ast

import scutil.base.implicits._
import scutil.lang._

object BsonRegexOption {
	val prism	=
		Prism[Char,BsonRegexOption](
			_ matchOption {
				case 'i'	=> RegexCaseInsensitive
				case 'm'	=> RegexMultiline
				case 's'	=> RegexDotall
				case 'u'	=> RegexUnicode
				case 'l'	=> RegexLocalized
				case 'x'	=> RegexExtended
			},
			_ match {
				case RegexCaseInsensitive	=> 'i'
				case RegexMultiline			=> 'm'
				case RegexDotall			=> 's'
				case RegexUnicode			=> 'u'
				case RegexLocalized			=> 'l'
				case RegexExtended			=> 'x'
			}
		)
}

// TODO maybe add
//	'c',	Pattern.CANON_EQ
//	't',	Pattern.LITERAL
//	'd',	Pattern.UNIX_LINES
//	'g'		GLOBAL
sealed abstract class BsonRegexOption
case object RegexCaseInsensitive	extends BsonRegexOption	// i	CASE_INSENSITIVE
case object RegexMultiline			extends BsonRegexOption	// m	MULTILINE
case object RegexDotall				extends BsonRegexOption	// s	DOTALL
case object RegexUnicode			extends BsonRegexOption	// u	UNICODE_CASE
case object RegexLocalized			extends BsonRegexOption	// l	LOCALIZED
case object RegexExtended			extends BsonRegexOption	// x	COMMENTS
