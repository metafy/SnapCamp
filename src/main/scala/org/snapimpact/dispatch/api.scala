package org.snapimpact.dispatch

import scala.xml.NodeSeq
import net.liftweb.http.{S, LiftResponse, InMemoryResponse, XmlResponse}
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.json._
import org.snapimpact.lib.Serializers.anyToRss



object api { 
  def volopps(): Box[LiftResponse] = Full( S.param("key") match { 
    case Full(_) => S.param("output") match {
      case Full("json") => InMemoryResponse(sampleJson.getBytes("UTF-8"), ("Content-Type" -> "text/javascript") :: Nil, Nil, 200)
      //XXX these responses should include encoding in the xml prolog
      case Full("rss") => XmlResponse(sampleRss, "application/rss+xml")
      case _ => XmlResponse(sampleHtml, "application/xhtml+xml; charset=utf-8")
    }
    case _ => XmlResponse(missingKey, 401, "application/xhtml+xml; charset=utf-8")
  } )

  val missingKey =
    <p>You seem to be missing the API key parameter ('key') in your query.  Please see the <a href="http://www.allforgood.org/docs/api.html">directions for how to get an API key</a>.</p>

  case class Sample(categories: List[String], quality_score: Double, pageviews: Int)
  val sample = Sample(List("category1", "category2"), .5, 2312)

  implicit val formats = Serialization.formats(NoTypeHints)
  val sampleJson = Serialization.write(sample) 

  val sampleHtml =
    <p>here's some info on volunteer opportunities</p>

  val sampleRss = <rss version="2.0" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:fp="http://www.allforgood.org/" xmlns:georss="http://www.georss.org/georss" xmlns:gml="http://www.opengis.net/gml">{anyToRss(sample)}</rss>
}
