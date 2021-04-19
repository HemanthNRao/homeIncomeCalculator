package org.RAO.homeIncomeCalculator

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{decodeRequest, handleExceptions, handleRejections}
import org.RAO.homeIncomeCalculator.routes.RestAPIs

object APIServer extends App with RestAPIs {
  implicit val system = ActorSystem()
  ConfigManager.setConfig("local-config.conf")
  val routes = (handleExceptions(serverExceptionHandler) &
    handleRejections(serverRejectionHandler) & decodeRequest) {
    dataRoutes
  }
  val bindingFuture = Http().bindAndHandle(routes, ConfigManager.get("http.interface"), ConfigManager.get("http.port").toInt)
}
