package org.RAO.homeIncomeCalculator.DAL

import org.RAO.homeIncomeCalculator.ConfigManager

trait QueryManager
{
  val dbType = ConfigManager.get("db")

  val db = dbType match
  {
    case "sqlite3" => new SqliteBackend
  }
}
