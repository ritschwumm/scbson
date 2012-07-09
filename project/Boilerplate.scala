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
	//## tuples
	
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
		val awc	= aritywise(arity)(",") _
		val typeParams	= awc("T$:BSONFormat") 
		val typeNames	= awc("T$")
		("""
		|	implicit def Tuple"""+arity+"""BSONFormat["""+typeParams+"""]:BSONFormat[("""+typeNames+""")]	= new BSONFormat[("""+typeNames+""")] {
		|		def write(out:("""+typeNames+""")):BSONValue	= {
		|			BSONArray(Seq("""+ awc("doWrite[T$](out._$)")	+"""))
		|		}
		|		def read(in:BSONValue):("""+typeNames+""")	= {
		|			val	arr	= forceArray(in)
		|			("""+ awc("doRead[T$](arr($-1))") + """)
		|		}
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	//## case classes
	
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
		val awc	= aritywise(arity)(",") _
		val typeParams	= awc("S$:BSONFormat") 
		val typeNames	= awc("S$")
		val fieldNames	= awc("k$")
		("""
		|	def caseClassBSONFormat"""+arity+"""["""+typeParams+""",T:Manifest](apply:("""+typeNames+""")=>T, unapply:T=>Option[("""+typeNames+""")]):BSONFormat[T]	= {
		|		val Seq("""+fieldNames+""")	= fieldNamesFor[T]
		|		new BSONFormat[T] {
		|			def write(out:T):BSONValue	= {
		|				val fields	= unapply(out).get
		|				BSONVarDocument(""" + awc("k$ -> doWrite[S$](fields._$)") + """)
		|			}
		|			def read(in:BSONValue):T	= {
		|				val map	= forceMap(in)
		|				apply(""" + awc("doRead[S$](map(k$))") + """)
		|			}
		|		}
		|	}
		""").stripMargin
	}
	
	//------------------------------------------------------------------------------
	//## helper
	
	private def aritywise(arity:Int)(separator:String)(format:String):String	= 
			1 to arity map { i => format replace ("$", i.toString) } mkString separator
}
