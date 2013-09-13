package scbson.serialization

import scala.reflect._

import scutil.lang._

import scbson._

import BSONSerializationUtil._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SeqFormat[T:Format]:Format[Seq[T]]	= {
		val sub	= format[T]
		SubtypeFormat[Seq[T],BSONArray](
				it	=> BSONArray(it map doWrite[T]),
				it	=> it.value map sub.read)
	}
	
	implicit def SetFormat[T:Format]:Format[Set[T]]					= SeqFormat[T] compose Bijection(_.toSeq,	_.toSet)
	implicit def ListFormat[T:Format]:Format[List[T]]				= SeqFormat[T] compose Bijection(_.toSeq,	_.toList)
	implicit def ArrayFormat[T:Format:ClassTag]:Format[Array[T]]	= SeqFormat[T] compose Bijection(_.toSeq,	_.toArray)
	
	//------------------------------------------------------------------------------
	
	/*
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
			
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	= new Format[Map[String,T]] {
		def write(out:Map[String,T]):BSONValue	=
				BSONObject(out map { 
					case (k,v) => (k, doWrite[T](v)) 
				})
		def read(in:BSONValue):Map[String,T]	= 
				objectValue(in) map { 
					case (k,v) => (k, doRead[T](v)) 
				}
	}
	*/
	
	// TODO careful, should sort it's keys maybe
	implicit def ViaSetMapFormat[K:Format,V:Format]:Format[Map[K,V]]	= new Format[Map[K,V]] {
		// TODO dubious
		import TupleProtocol.Tuple2Format
		def write(out:Map[K,V]):BSONValue	= doWrite[Set[(K,V)]](out.toSet)
		def read(in:BSONValue):Map[K,V]		= doRead[Set[(K,V)]](in).toMap
	}
	
	/*
	// NOTE mongo keys should be ordered
	implicit def MapF[T:BFormat]:BFormat[Map[String,T]]	= {
		val sub	= bformat[T]
		BFormatIn[Map[String,T],BSONDocument](
				it	=> BSONDocument(it.toSeq map { case (k,v) => (k, sub write v) }), 
				it	=> it.value map { case (k,v) => (k, sub read v) } toMap)
	}
	
	// automatically sorts keys alphabetically
	def sortingDocument[T](writeFunc:T=>Map[String,BSONValue], readFunc:Map[String,BSONValue]=>T):Format[T]	=
			FormatSubtype[T,BSONDocument](
					it	=> sortKeys(BSONDocument(writeFunc(it).toSeq)),
					it	=> readFunc(it.value.toMap))
		
	def sortKeys(doc:BSONDocument):BSONDocument	=
			BSONDocument(doc.value sortBy { _._1 })

	// expects ordered keys
	def orderedDocument[T](writeFunc:T=>Seq[(String,BSONValue)], readFunc:Map[String,BSONValue]=>T):Format[T]	=
			FormatSubtype[T,BSONDocument](it => BSONDocument(writeFunc(it)), it => readFunc(it.value.toMap))
	*/
	
	//------------------------------------------------------------------------------
			
	// alternative {some} or {none}
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= new Format[Option[T]] {
		private val someTag	= "some"
		private val noneTag	= "none"
		
		def write(out:Option[T]):BSONValue	= out match {
			case Some(value)	=> BSONDocument(Seq(someTag -> doWrite(value)))
			case None			=> BSONDocument(Seq(noneTag -> BSONBoolean(true)))
		}
		def read(in:BSONValue):Option[T]	= {
			val map	= downcast[BSONDocument](in).value.toMap
			(map get someTag, map get noneTag) match {
				case (Some(js), None)	=> Some(doRead[T](js))
				case (None, Some(js))	=> None
				case _					=> fail("unexpected option")
			}
		}
	}
	
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	= new Format[Either[L,R]] {
		private val rightTag	= "right"
		private val leftTag		= "left"
		
		def write(out:Either[L,R]):BSONValue	= out match {
			case Right(value)	=> BSONDocument(Seq(
				rightTag	-> doWrite[R](value)
			))
			case Left(value)	=> BSONDocument(Seq(
				leftTag		-> doWrite[L](value)
			))
		}
		def read(in:BSONValue):Either[L,R]	= { 
			val	map	= downcast[BSONDocument](in).value.toMap
			(map get leftTag, map get rightTag) match {
				case (None, Some(bs))	=> Right(doRead[R](bs))
				case (Some(bs), None)	=> Left(doRead[L](bs))
				case _					=> fail("unexpected either")
			}
		}
	}
	
	implicit def TriedFormat[F:Format,W:Format]:Format[Tried[F,W]]	= new Format[Tried[F,W]] {
		private val winTag	= "win"
		private val failTag	= "fail"
		
		def write(out:Tried[F,W]):BSONValue	= out match {
			case Fail(value)	=> BSONDocument(Seq(
				failTag		-> doWrite[F](value)
			))
			case Win(value)	=> BSONDocument(Seq(
				winTag	-> doWrite[W](value)
			))
		}
		def read(in:BSONValue):Tried[F,W]	= { 
			val	map	= downcast[BSONDocument](in).value.toMap
			(map get failTag, map get winTag) match {
				case (Some(bs), None)	=> Fail(doRead[F](bs))
				case (None, Some(bs))	=> Win(doRead[W](bs))
				case _					=> fail("unexpected trial")
			}
		}
	}
}
