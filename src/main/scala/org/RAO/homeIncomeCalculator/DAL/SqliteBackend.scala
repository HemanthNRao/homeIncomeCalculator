package org.RAO.homeIncomeCalculator.DAL

import org.RAO.homeIncomeCalculator.ConfigManager

import java.sql.{Connection, DriverManager}

object SqliteBackend
{
  Class.forName(ConfigManager.get("sqlite3.driver"))
  var url=ConfigManager.get("sqlite3.url")
  var user=ConfigManager.get("sqlite3.user")
  var pass=ConfigManager.get("sqlite3.pass")
  private def dbConn=DriverManager.getConnection(url,user,pass)
}

class SqliteBackend extends DBBackend{

//  Class.forName(ConfigManager.get("sqlite3.driver"))
//  var url=ConfigManager.get("sqlite3.url")
//  var user=ConfigManager.get("sqlite3.user")
//  var pass=ConfigManager.get("sqlite3.pass")
//  private def dbConn=DriverManager.getConnection(url,user,pass)

  //Execute the block for setting up the tables
  {
    val dropBAL = "drop table if exists BAL"
    val dropCredit = "drop table if exists credit"
    val dropDebit = "drop table if exists debit"

    val BAL = "CREATE TABLE if not exists BAL(balance double not null);"
    val credit = "CREATE TABLE if not exists credit(DATE date primary key,amount double NOT NULL);"
    val debit = "CREATE TABLE if not exists debit(Date date not null,time time,amount double,reason varchar2(4000));"
    val insertFirstBal = "INSERT INTO BAL values(0);"

//    queyWithNoResult(dropBAL,Array())
//    queyWithNoResult(dropCredit,Array())
//    queyWithNoResult(dropDebit,Array())
    queyWithNoResult(BAL,Array())
    queyWithNoResult(credit,Array())
    queyWithNoResult(debit,Array())
    queyWithNoResult(insertFirstBal,Array())
  }
  override def getConnection: Connection = SqliteBackend.dbConn
}
