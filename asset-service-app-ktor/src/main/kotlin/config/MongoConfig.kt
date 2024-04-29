package ru.otus.otuskotlin.financier.asset.config

import ru.otus.otuskotlin.financier.asset.Config

data class MongoConfig(
  val host: String = "localhost",
  val port: Int = 27017,
  val user: String = "mongo",
  val pass: String = "mongo",
) {

  constructor(config: Config) : this(
    host = config.getProperty("$PATH.host"),
    port = config.getProperty("$PATH.port").toInt(),
    user = config.getProperty("$PATH.user"),
    pass = config.getProperty("$PATH.pass"),
  )

  companion object {
    const val PATH = "${ConfigPaths.repository}.mongo"
  }
}
