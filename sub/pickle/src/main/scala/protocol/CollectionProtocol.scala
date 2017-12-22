package scbson.pickle.protocol

import scala.reflect.ClassTag

import scutil.lang._

import scbson.ast._
import scbson.pickle._

object CollectionProtocol extends CollectionProtocol

trait CollectionProtocol {
	implicit def SetFormat[T](implicit ev:Format[ISeq[T]]):Format[Set[T]]				= ev compose Bijection(_.toVector,	_.toSet)
	implicit def ListFormat[T](implicit ev:Format[ISeq[T]]):Format[List[T]]				= ev compose Bijection(_.toVector,	_.toList)
	implicit def ArrayFormat[T:ClassTag](implicit ev:Format[ISeq[T]]):Format[Array[T]]	= ev compose Bijection(_.toVector,	_.toArray)
	
	// TODO careful, should sort it's keys maybe
	implicit def MapViaSetFormat[K:Format,V:Format](implicit ev:Format[Set[(K,V)]]):Format[Map[K,V]]	= {
		Format[Map[K,V]](
			(out:Map[K,V])	=> ev get out.toSet,
			(in:BsonValue)	=> (ev put in).toMap
		)
	}
	
	/*
	def mapFormat[S,T:Format](conv:Bijection[S,String]):Format[Map[S,T]]	=
			StringMapFormat[T] compose Bijection[Map[S,T],Map[String,T]](
				_ map { case (k,v) => (conv get k, v) },
				_ map { case (k,v) => (conv read  k, v) }
			)
			
	implicit def StringMapFormat[T:Format]:Format[Map[String,T]]	=
			Format[Map[String,T]](
				(out:Map[String,T])	=> {
					BsonDocument(out.toVector map {
						case (k,v) => (k, doWrite[T](v))
					})
				},
				(in:BsonValue)	=> {
					documentValue(in)
					.mapToMap {
						case (k,v) => (k, doRead[T](v))
					}
				}
			)
	*/
	
	/*
	// NOTE mongo keys should be ordered
	implicit def MapF[T:BFormat]:BFormat[Map[String,T]]	= {
		val sub	= bformat[T]
		BFormatIn[Map[String,T],BsonDocument](
				it	=> BsonDocument(it.toSeq map { case (k,v) => (k, sub get v) }),
				it	=> it.value map { case (k,v) => (k, sub read v) } toMap)
	}
	
	// automatically sorts keys alphabetically
	def sortingDocument[T](writeFunc:T=>Map[String,BsonValue], readFunc:Map[String,BsonValue]=>T):Format[T]	=
			FormatSubtype[T,BsonDocument](
					it	=> sortKeys(BsonDocument(writeFunc(it).toSeq)),
					it	=> readFunc(it.value.toMap))
		
	def sortKeys(doc:BsonDocument):BsonDocument	=
			BsonDocument(doc.value sortBy { _._1 })

	// expects ordered keys
	def orderedDocument[T](writeFunc:T=>ISeq[(String,BsonValue)], readFunc:Map[String,BsonValue]=>T):Format[T]	=
			FormatSubtype[T,BsonDocument](it => BsonDocument(writeFunc(it)), it => readFunc(it.value.toMap))
	*/
}
