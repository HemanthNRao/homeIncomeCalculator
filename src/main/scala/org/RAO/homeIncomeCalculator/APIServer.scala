package org.RAO.homeIncomeCalculator

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.RAO.homeIncomeCalculator.routes.RestAPIs
import org.RAO.homeIncomeCalculator.utils.Json
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import java.lang.Thread.sleep
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global


object APIServer extends App with RestAPIs {
  implicit val system = ActorSystem()
  ConfigManager.setConfig("local-config.conf")
  /*val routes = (handleExceptions(serverExceptionHandler) &
    handleRejections(serverRejectionHandler) & decodeRequest & optionalHeaderValueByName("session")) {
    headerSession => {
      println("session: ",headerSession.getOrElse(""))
      val res = getLogin(headerSession.getOrElse(""))
      println("res in main code: ", res)
      if (headerSession.getOrElse("") != res) complete("Unauthenticated")
      else dataRoutes
    }
  }*/

  val routes = (handleExceptions(serverExceptionHandler) &
    handleRejections(serverRejectionHandler) &decodeRequest & cors())
  {
    dataRoutes
  }
  val bindingFuture = Http().bindAndHandle(routes, ConfigManager.get("http.interface"), ConfigManager.get("http.port").toInt)

  def getLogin(id:String):String=
  {
    var res =""
    if(id.isEmpty)
      null
    else {
      val request = HttpRequest(uri = s"http://localhost:8181/user/getLogin/${id}")
      val responseFuture = Http().singleRequest(request)
      val entityFuture = responseFuture.flatMap(res => Unmarshal(res).to[String].map(data => Json.parse(data)))
      val entity = entityFuture onComplete {
        case Success(json) => res = json.get("sessionId").toString
        case Failure(e) => None
      }
      println("entered else part")
      sleep(500)
      println("res:", res)
      res
    }
  }
}
