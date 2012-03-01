import sbt._

object Boilerplate {
	def generate(srcDir:File):Seq[File]	= {
		val	outDir	= srcDir / "boilerplate"
		outDir.mkdirs()
		Seq(
			genTupleFile(outDir), 
			genCaseClassFile(outDir)
		)
	}	
	
	//------------------------------------------------------------------------------
	
	def genTupleFile(outDir:File):File	= {
		val outFile		= outDir / "TupleProtocolGenerated.scala"
		IO write (outFile,	genTupleTrait)
		outFile
	}
	
	def genTupleTrait:String	= {
		"""
		|package scbson.serialization
		|
		|import scutil.Implicits._
		|import scmirror._
		|import scbson._
		|import BSONSerializationUtil._
		|
		|trait TupleProtocolGenerated {
		""".stripMargin		+ 
		(2 to 22 map genTupleMethod mkString "\n")	+
		"""
		|	protected def forceArray(in:BSONValue):Seq[BSONValue]
		|}
		""".stripMargin
	}
			
	def genTupleMethod(arity:Int):String	= {
		def aritywise(item:Int=>String):String	= 1 to arity map item mkString ","
		val typeParams	= aritywise("T"+_+":BSONFormat") 
		val typeNames	= aritywise("T"+_)
		("""
		|	implicit def Tuple"""+arity+"""BSONFormat["""+typeParams+"""]:BSONFormat[("""+typeNames+""")]	= new BSONFormat[("""+typeNames+""")] {
		|		def write(out:("""+typeNames+""")):BSONValue	= {
		|			BSONArray(Seq("""+ aritywise(i => "doWrite[T"+i+"](out._"+i+")")	+"""))
		|		}
		|		def read(in:BSONValue):("""+typeNames+""")	= {
		|			val	arr	= forceArray(in)
		|			("""+ aritywise(i => "doRead[T"+i+"](arr("+(i-1)+"))") +""")
		|		}
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	
	def genCaseClassFile(outDir:File):File	= {
		val outFile		= outDir / "CaseClassProtocolGenerated.scala"
		IO write (outFile,	genCaseClassTrait)
		outFile
	}
	
	def genCaseClassTrait:String	= {
		"""
		|package scbson.serialization
		|
		|import scutil.Implicits._
		|import scmirror._
		|import scbson._
		|import BSONSerializationUtil._
		|
		|trait CaseClassProtocolGenerated {
		""".stripMargin		+ 
		(2 to 22 map genCaseClassMethod mkString "\n")	+
		"""
		|	protected def fieldNamesFor[T:Manifest]:Seq[String]
		|	protected def forceMap(in:BSONValue):Map[String,BSONValue]
		|}
		""".stripMargin
	}
	
	def genCaseClassMethod(arity:Int):String	= {
		def aritywise(item:Int=>String):String	= 1 to arity map item mkString ","
		val typeParams	= aritywise("S"+_+":BSONFormat") 
		val typeNames	= aritywise("S"+_)
		val fieldNames	= aritywise("k"+_)
		("""
		|	def caseClassBSONFormat"""+arity+"""["""+typeParams+""",T:Manifest](apply:("""+typeNames+""")=>T, unapply:T=>Option[("""+typeNames+""")]):BSONFormat[T]	= {
		|		val Seq("""+fieldNames+""")	= fieldNamesFor[T]
		|		new BSONFormat[T] {
		|			def write(out:T):BSONValue	= {
		|				val fields	= unapply(out).get
		|				BSONVarDocument(""" + aritywise(i => "k"+i+" -> doWrite[S"+i+"](fields._"+i+")") + """)
		|			}
		|			def read(in:BSONValue):T	= {
		|				val map	= forceMap(in)
		|				apply(""" + aritywise(i => "doRead[S"+i+"](map(k"+i+"))") + """)
		|			}
		|		}
		|	}
		""").stripMargin
	}
}
