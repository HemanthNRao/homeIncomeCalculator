package org.RAO.homeIncomeCalculator.utils

import akka.actor.ActorSystem
import akka.http.scaladsl._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, path}
import org.RAO.homeIncomeCalculator.routes.RestAPIs

object APIServer extends App with RestAPIs {
  implicit val system = ActorSystem()
  ConfigManager.setConfig("local-config.conf")
  val routes =
 {
//   path("hello")
//   {
//     complete(StatusCodes.OK)
//   }
   dataRoutes
 }
  val bindingFuture=Http().bindAndHandle(routes,ConfigManager.get("http.interface"),ConfigManager.get("http.port").toInt)
}
