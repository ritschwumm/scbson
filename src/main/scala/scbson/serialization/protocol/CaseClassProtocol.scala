package scbson.serialization

import scutil.Implicits._

import scmirror._

import scbson._

import BSONSerializationUtil._

object CaseClassProtocol extends CaseClassProtocol

trait CaseClassProtocol extends CaseClassProtocolGenerated {
	def caseObjectBSONFormat[T:Manifest](singleton:T):BSONFormat[T]	= new BSONFormat[T] {
		def write(out:T):BSONValue	= {
			BSONDocument.empty
		}
		def read(in:BSONValue):T	= {
			singleton
		}
	}
	
	def caseClassBSONFormat1[S1:BSONFormat,T:Manifest](
		apply:S1=>T,
		unapply:T=>Option[S1]
	):BSONFormat[T]	= {
		val Seq(k1)	= fieldNamesFor[T]
		new BSONFormat[T] {
			def write(out:T):BSONValue	= {
				val fields	= unapply(out).get
				BSONVarDocument(
					k1	-> doWrite[S1](fields)
				)
			}
			def read(in:BSONValue):T	= {
				val map	= forceMap(in)
				apply(
					doRead[S1](map(k1))
				)
			}
		}
	}
	
	/*
	def caseClassBSONFormat2[S1:BSONFormat,S2:BSONFormat,T:Manifest](
		apply:(S1,S2)=>T, 
		unapply:T=>Option[(S1,S2)]
	):BSONFormat[T]	= {
		val Seq(k1,k2)	= fieldNamesFor[T]
		new BSONFormat[T] {
			def write(out:T):BSONValue	= {
				val fields	= unapply(out).get
				BSONVarDocument(
					k1	-> doWrite[S1](fields._1),
					k2	-> doWrite[S2](fields._2)
				)
			}
			def read(in:BSONValue):T	= {
				val map	= forceMap(in)
				apply(
					doRead[S1](map(k1)),
					doRead[S2](map(k2))
				)
			}
		}
	}
	*/
	
	//------------------------------------------------------------------------------
	
	// BETTER cache results
	protected def fieldNamesFor[T:Manifest]:Seq[String]	=
			(Reflector constructor manifest[T].erasure) getOrError ("cannot get fields for type " + manifest[T].erasure)
		
	protected def forceMap(in:BSONValue):Map[String,BSONValue]	=
			downcast[BSONDocument](in).valueMap
		
	//------------------------------------------------------------------------------
	//## sums of case classes
	
	def caseClassSumBSONFormat[T](summands:Summand[_<:T]*):BSONFormat[T]	= {
		val helper	= new SumHelper[T](summands)
		BSONFormat[T](
			out => {
				val (identifier,formatted)	= helper write out
				BSONDocument(
					(Summand.typeTag -> identifier)	+:
					downcast[BSONDocument](formatted).value
				)
			},
			in => {
				val formatted	= downcast[BSONDocument](in)
				// val identifier	= downcast[BSONString](formatted.value.head._2).value
				val identifier	= downcast[BSONString](formatted.valueMap(Summand.typeTag))
				helper read (identifier, formatted)
			}
		)
	}
}
