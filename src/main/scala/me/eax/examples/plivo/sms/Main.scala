package me.eax.examples.plivo.sms

import akka.actor._
import akka.stream._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  val authId = "Ololo"
  val authToken = "Trololo"
  val src = "NOTIFY" // "+13364960988"
  val dst = "+79161234567"
  val msg = "Your sms code: 7654"
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val fResponse = Http().singleRequest(
    HttpRequest(
      method = HttpMethods.POST,
      uri = s"https://api.plivo.com/v1/Account/$authId/Message/",
      headers = List(
        headers.Authorization(headers.BasicHttpCredentials(authId, authToken))
      ),
      entity = HttpEntity(
        ContentTypes.`application/json`,
        compact(JObject(List(
          "src" -> JString(src),
          "dst" -> JString(dst),
          "text" -> JString(msg)
        )))
      )
    )
  )

  val fResult = {
    for {
      resp <- fResponse
    } yield {
      resp.status.isSuccess()
    }
  }

  val result = Await.result(fResult, Duration.Inf)
  println(s"Success: $result")
  System.exit(0)
}
