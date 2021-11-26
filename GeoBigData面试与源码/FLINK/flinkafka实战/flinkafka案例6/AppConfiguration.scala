package com.binderror.core

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Class that wraps the config
 */
class AppConfiguration extends Serializable {
  val configuration: Config = ConfigFactory.load()
}

object AppConfiguration {
  def config(): Config = {
    val appConfig = new AppConfiguration
    appConfig.configuration
  }
}
