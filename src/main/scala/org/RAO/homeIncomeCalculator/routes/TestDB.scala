package org.RAO.homeIncomeCalculator.routes

import org.RAO.homeIncomeCalculator.DAL.{HomeCQueryManager, SqliteBackend}
import org.RAO.homeIncomeCalculator.utils.ConfigManager

import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.collection.mutable
object TestDB{
  def main(args:Array[String])= {
//      Class.forName("org.sqlite.JDBC")
//      var url="jdbc:sqlite:/home/hemanth/IdeaProjects/homeIncomeCalculator/src/main/scala/org/RAO/homeIncomeCalculator/DAL/DB/homeCalDB.db"
//      var user="root"
//      var pass=""
//      private val dbConn=DriverManager.getConnection(url,user,pass)
//      var st=dbConn.createStatement()
//      var sql="select * from BAL"
//      var rs=st.executeQuery(sql)
//      while(rs.next()){
//        print(rs.getObject(1))
//      }
    ConfigManager.setConfig("local-config.conf")

    HomeCQueryManager.updateBalance(2)
    var dateFormat = new SimpleDateFormat("dd/MM/yyyy")
    var date=dateFormat.format(new Date()).toString
    var timeFormat = new SimpleDateFormat("HH:mm:ss")
    var time=timeFormat.format(new Date()).toString
//    HomeCQueryManager.insertDebit(date,time,122.0,"something")
//    var res = HomeCQueryManager.getAllCredit("10/01/2021","11/01/2021")
//    print(res)
//    var res1=HomeCQueryManager.getOneDebit(date)
//    print(res1)
//    HomeCQueryManager.upDateBalZero
    val res=HomeCQueryManager.getOneDebit(date)
    var m:Map[String,Any]=Map()
    var i=1
    val s:mutable.Seq[Map[String,Any]]=null
    res.foreach{
      rs=>
        rs.foreach{
          case (key,value) => m += (key -> value)
            println(key)
            println(value)
        }
        println(i)
        i+=1
    }
    println(m.size)
  }
  def print(res:Seq[mutable.LinkedHashMap[String,Any]])={
    res.foreach {
      rs =>
        rs.foreach {
          case (key, value) => println(value)
        }
    }
  }
}
