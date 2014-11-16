package scbson.serialization

import scala.reflect.ClassTag

import scutil.lang._

import scbson._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SetFormat[T](implicit ev:Format[ISeq[T]]):Format[Set[T]]				= ev compose Bijection(_.toVector,	_.toSet)
	implicit def ListFormat[T](implicit ev:Format[ISeq[T]]):Format[List[T]]				= ev compose Bijection(_.toVector,	_.toList)
	implicit def ArrayFormat[T:ClassTag](implicit ev:Format[ISeq[T]]):Format[Array[T]]	= ev compose Bijection(_.toVector,	_.toArray)
	
	// TODO careful, should sort it's keys maybe
	implicit def MapViaSetFormat[K:Format,V:Format](implicit ev:Format[Set[(K,V)]]):Format[Map[K,V]]	= {
		Format[Map[K,V]](
			(out:Map[K,V])	=> ev write out.toSet,
			(in:BSONValue)	=> (ev read in).toMap
		)
	}
	
	/*
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv write k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
			
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	=
			Format[Map[String,T]](
				(out:Map[String,T])	=> {
					BSONDocument(out.toVector map { 
						case (k,v) => (k, doWrite[T](v)) 
					})
				},
				(in:BSONValue)	=> { 
					documentValue(in) 
					.map { 
						case (k,v) => (k, doRead[T](v)) 
					}
					.toMap
				}
			)
	*/
	
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
	def orderedDocument[T](writeFunc:T=>ISeq[(String,BSONValue)], readFunc:Map[String,BSONValue]=>T):Format[T]	=
			FormatSubtype[T,BSONDocument](it => BSONDocument(writeFunc(it)), it => readFunc(it.value.toMap))
	*/
}
