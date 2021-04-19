package org.RAO.homeIncomeCalculator.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.model.{Multipart, StatusCodes}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import org.RAO.homeIncomeCalculator.DAL.HomeCQueryManager
import org.RAO.homeIncomeCalculator.utils.{ErrorConstants, Json}

import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


trait RestAPIs extends APIRoutes{
  implicit val system: ActorSystem
  var dateFormat = new SimpleDateFormat("YYYY:MM:dd")
  var date=dateFormat.format(new Date())
  var timeFormat = new SimpleDateFormat("HH:mm:ss")
  var time=timeFormat.format(new Date()).toString


  val dataRoutes = (pathPrefix("homeCalc")) {
    (path("insert") & post & entity(as[Multipart.FormData])) {
      formData=>
      {
        val inputMapF=getFormDataToMap(formData)
        onSuccess(inputMapF)
        {
          inputMap=>
            validate(checkTodaysEntry,ErrorConstants.DATE_ALREADY_EXISTS) {
              val amount = inputMap("amount").toString.toDouble
              //check gien date is exist in database if so throw error saying todays income already inerted
              HomeCQueryManager.insertCredit(date, amount)
              val bal = updateBalance(amount, "add")
              complete(HttpEntity(ContentTypes.`application/json`, Json.Value(Map("balance" -> bal, "amount" -> amount)).writeln))
            }
        }
      }
    } ~
    (path("debit") & post &entity(as[Multipart.FormData])) {
      formData =>
        {
          val inputMapF=getFormDataToMap(formData)
          onSuccess(inputMapF)
          {
            inputMap =>
              val amount=inputMap("amount").toString.toDouble
              val reason=inputMap("reason").toString
              HomeCQueryManager.insertDebit(date,time,amount,reason)
              val bal=updateBalance(amount,"sub")
              complete(HttpEntity(ContentTypes.`application/json`,Json.Value(Map("balance"->bal,"reason"->reason,"amount" -> amount,"time"->time)).writeln))
          }
        }
    } ~
      (path("balance") & get){
        val bal=HomeCQueryManager.getBalance.getOrElse(0.0)
        complete(HttpEntity(ContentTypes.`application/json`,Json.Value(Map("balance"->bal)).writeln))
      } ~
      (path("balZero") & post) {
        HomeCQueryManager.upDateBalZero
        complete(StatusCodes.OK)
      } ~
      (path("expense") & get) {
        val res=HomeCQueryManager.getOneDebit(date)
        complete(HttpEntity(ContentTypes.`application/json`,Json.Value(res).writeln))
      } ~
      (path("expenses") &get & entity(as[Multipart.FormData])){
        formData=>
          val inputMapF=getFormDataToMap(formData)
          onSuccess(inputMapF) {
            inputMap =>
//              val data = inputMap("data").toString
//              val dataConfig = Json.parse(data)
              val fromDate = inputMap("fromDate").toString
              val toDate = inputMap("toDate").toString
              println(formData)
              println(toDate)
              val res = HomeCQueryManager.getAllDebit(fromDate, toDate)
              complete(HttpEntity(ContentTypes.`application/json`, Json.Value(res).writeln))
          }
      } ~
      (path("credits") & get &entity(as[Multipart.FormData])) {
        formData=>
          val inputMapF=getFormDataToMap(formData)
          onSuccess(inputMapF) {
            inputMap => {
              val fromDate = inputMap("fromDate").toString
              val toDate = inputMap("toDate").toString
              println("from ", fromDate)
              println("TO ", toDate)
              val res = HomeCQueryManager.getAllCredit(fromDate, toDate)
              complete(HttpEntity(ContentTypes.`application/json`, Json.Value(res).writeln))
            }
          }
      } ~
      (path("updateBal") & post & entity(as[Multipart.FormData]))
      {
        formData =>
          val inputMapF=getFormDataToMap(formData)
          onSuccess(inputMapF){
            inputMap=>
//              val data=inputMap("data").toString
//              val dataConfig=Json.parse(data)
              val amount=inputMap("amount").toString.toDouble
              HomeCQueryManager.updateBalance(amount)
              complete(HttpEntity(ContentTypes.`application/json`,Json.Value(Map("bal"->amount)).writeln))
          }
      }
  }


  private def getFormDataToMap(formData: Multipart.FormData): Future[Map[String, Any]] =
  {
    // Method to extract byte array from multipart formdata.
    def getBytesFromFilePart(dataBytes: Source[ByteString, Any]) =
    {
      dataBytes.runFold(ArrayBuffer[Byte]())
      { case (accum, value) => accum ++= value.toArray }
    }
    // Process each form data and store it in a Map[String,Array[Byte]]
    formData.parts.mapAsync(1)
    {
      // Case to extract file or schema input
      // Looks like there might be issues in using the method below resulting in buffer overflow
      // See this thread: https://github.com/akka/akka-http/issues/285
      case b: BodyPart if b.name == "file" || b.name == "schema" => getBytesFromFilePart(b.entity.dataBytes).map(bytes => b.name -> bytes.toArray)
      // Case to extract the rest of the POST parameters
      // Not sure why we are using toStrict() below.
      // Reference: https://github.com/knoldus/akka-http-multipart-form-data.g8/blob/master/src/main/g8/src/main/scala/com/knoldus/MultipartFormDataHandler.scala
      case b: BodyPart => b.toStrict(2 seconds).map(strict => b.name -> strict.entity.data.utf8String.getBytes())
    }
      .runFold(mutable.HashMap[String, Any]())
      { case (map, (keyName, keyVal)) =>
        val value = if (keyName != "file") new String(keyVal)
        else keyVal
        map += (keyName -> value)
      }
      .map(_.toMap)
  }


  def updateBalance(amount:Double,flag:String)= {
    //TODO: check balance is not updating properly
    var bal=HomeCQueryManager.getBalance.getOrElse(0.0)
    if(flag=="add")
      bal=bal + amount
    if(flag=="sub")
      bal=bal - amount
    HomeCQueryManager.updateBalance(bal)
    bal
  }

  def checkTodaysEntry=
    {
      val res=HomeCQueryManager.checkEntry(date).getOrElse(0)
      if(res==1) false else true
    }
}
