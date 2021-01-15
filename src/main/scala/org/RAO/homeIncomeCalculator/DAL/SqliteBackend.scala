package org.RAO.homeIncomeCalculator.DAL

import org.RAO.homeIncomeCalculator.utils.ConfigManager

import java.sql.{Connection, DriverManager}

class SqliteBackend extends DBBackend{

  Class.forName(ConfigManager.get("sqlite3.driver"))
  var url=ConfigManager.get("sqlite3.url")
  var user=ConfigManager.get("sqlite3.user")
  var pass=ConfigManager.get("sqlite3.pass")
  private def dbConn=DriverManager.getConnection(url,user,pass)

  override def getConnection: Connection = dbConn
}
