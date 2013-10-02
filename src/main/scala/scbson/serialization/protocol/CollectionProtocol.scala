package scbson.serialization

import scala.reflect._

import scutil.lang._

import scbson._

import BSONSerializationUtil._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	/*
	implicit def SeqFormat[T:Format]:Format[Seq[T]]	= {
		val sub	= format[T]
		SubtypeFormat[Seq[T],BSONArray](
				it	=> BSONArray(it map doWrite[T]),
				it	=> it.value map sub.read)
	}
	*/
	implicit def SeqFormat[T:Format]:Format[Seq[T]] = 
			Format[Seq[T]](
				(out:Seq[T])	=> BSONArray(out map doWrite[T]),
				(in:BSONValue)	=> arrayValue(in) map doRead[T]
			)
	
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
	implicit def ViaSetMapFormat[K:Format,V:Format]:Format[Map[K,V]]	= {
		// TODO dubious
		import TupleProtocol.Tuple2Format
		Format[Map[K,V]](
			(out:Map[K,V])	=> doWrite[Set[(K,V)]](out.toSet),
			(in:BSONValue)	=> doRead[Set[(K,V)]](in).toMap
		)
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
	implicit def OptionFormat[T:Format]:Format[Option[T]]	= {
		val someTag	= "some"
		val noneTag	= "none"
		Format[Option[T]](
			_ match {
				case Some(value)	=> BSONVarDocument(someTag -> doWrite(value))
				case None			=> BSONVarDocument(noneTag -> BSONBoolean(true))
			},
			(in:BSONValue)	=> {
				val map	= documentMap(in)
				(map get someTag, map get noneTag) match {
					case (Some(js), None)	=> Some(doRead[T](js))
					case (None, Some(js))	=> None
					case _					=> fail("unexpected option")
				}
			}
		)
	}
	
	// alternative {left} or {right}
	implicit def EitherFormat[L:Format,R:Format]:Format[Either[L,R]]	= {
		val rightTag	= "right"
		val leftTag		= "left"
		Format[Either[L,R]]( 
			_ match {
				case Right(value)	=>  BSONVarDocument(
					rightTag	-> doWrite[R](value)
				)
				case Left(value)	=>  BSONVarDocument(
					leftTag		-> doWrite[L](value)
				)
			},
			(in:BSONValue)	=> {
				val map	= documentMap(in)
				(map get leftTag, map get rightTag) match {
					case (None, Some(js))	=> Right(doRead[R](js))
					case (Some(js), None)	=> Left(doRead[L](js))
					case _					=> fail("unexpected either")
				}
			}
		)
	}
	
	implicit def TriedFormat[F:Format,W:Format]:Format[Tried[F,W]]	= {
		val winTag	= "win"
		val failTag	= "fail"
		Format[Tried[F,W]]( 
			_ match {
				case Fail(value)	=> BSONVarDocument(
					failTag	-> doWrite[F](value)
				)
				case Win(value)		=> BSONVarDocument(
					winTag	-> doWrite[W](value)
				)
			},
			(in:BSONValue)	=> { 
				val map	= documentMap(in)
				(map get failTag, map get winTag) match {
					case (Some(bs), None)	=> Fail(doRead[F](bs))
					case (None, Some(bs))	=> Win(doRead[W](bs))
					case _					=> fail("unexpected trial")
				}
			}
		)
	}
}
