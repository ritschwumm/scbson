package scbson

import scutil.lang._
import scutil.implicits._

object BSONRegexOption {
	val prism	=
			Prism[Char,BSONRegexOption](
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
sealed abstract class BSONRegexOption
case object RegexCaseInsensitive	extends BSONRegexOption	// i	CASE_INSENSITIVE
case object RegexMultiline			extends BSONRegexOption	// m	MULTILINE
case object RegexDotall				extends BSONRegexOption	// s	DOTALL
case object RegexUnicode			extends BSONRegexOption	// u	UNICODE_CASE
case object RegexLocalized			extends BSONRegexOption	// l	LOCALIZED
case object RegexExtended			extends BSONRegexOption	// x	COMMENTS
