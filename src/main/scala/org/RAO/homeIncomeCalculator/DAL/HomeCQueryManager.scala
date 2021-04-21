package org.RAO.homeIncomeCalculator.DAL


object HomeCQueryManager extends QueryManager
{
  def getBalance={
    db.queryWithSingleResult[Double]("select * from BAL",Array())
  }

  def updateBalance(balance: Double)={
    db.queyWithNoResult("update BAL set balance=?",Array(balance))
  }

  def insertCredit(date:String,amount:Double)={
    db.queyWithNoResult("insert into credit values (?,?)",Array(date,amount))
  }

  def insertDebit(date:String,time:String,amount:Double,reason:String)={
    db.queyWithNoResult("insert into debit values(?,?,?,?)",Array(date,time,amount,reason))
  }

  def getOneDebit(date:String)={
    db.queryWithResult("select * from debit where Date=?",Array(date))
  }

  def getAllDebit(fromDate:String,toDate:String)={
    db.queryWithResult("SELECT * FROM debit WHERE DATE BETWEEN ? and ?",Array(fromDate,toDate))
  }

  def getAllCredit(fromDate:String,toDate:String)={
    db.queryWithResult("SELECT * FROM debit WHERE DATE BETWEEN ? and ?",Array(fromDate,toDate))
  }

  def upDateBalZero={
    db.queyWithNoResult("update BAL set balance=0",Array())
  }
  def checkEntry(date:String)={
    db.queryWithSingleResult("select count(date) as res from credit where date=?",Array(date))
  }

}
