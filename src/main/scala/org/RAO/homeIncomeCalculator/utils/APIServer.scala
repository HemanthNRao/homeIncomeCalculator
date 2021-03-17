package org.RAO.homeIncomeCalculator.utils

import akka.actor.ActorSystem
import akka.http.scaladsl._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, decodeRequest, handleExceptions, handleRejections, path}
import org.RAO.homeIncomeCalculator.routes.RestAPIs

object APIServer extends App with RestAPIs {
  implicit val system = ActorSystem()
  ConfigManager.setConfig("local-config.conf")
  val routes =( handleExceptions(serverExceptionHandler) &
    handleRejections(serverRejectionHandler) & decodeRequest)
 {
   dataRoutes
 }
  val bindingFuture=Http().bindAndHandle(routes,ConfigManager.get("http.interface"),ConfigManager.get("http.port").toInt)
}
