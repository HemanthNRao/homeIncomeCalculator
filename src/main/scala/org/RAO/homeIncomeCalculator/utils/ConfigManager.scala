package org.RAO.homeIncomeCalculator.utils

import java.io.{File,IOException}
import com.typesafe.config.{Config,ConfigFactory}

object ConfigManager {

  private var config:Option[Config]=None

  //Method to read configuration in provide file
  def setConfig(configFile:String)= {
    val fileObj=new File(configFile)
    if(fileObj.exists())
      config=Some(ConfigFactory.parseFile(fileObj))
    else
      throw new IOException(s"Config file doesn't exist:$configFile")
  }

  //Method to get a config from the config file
  def get(key:String)=config.get.getAnyRef(key).toString

  //Method to check if a key exists
  def exists(key:String)=config.get.hasPath(key)
}
