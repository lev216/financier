package ru.otus.otuskotlin.financier.asset.persistence.mongo.model

import org.bson.codecs.pojo.annotations.BsonId
import java.math.BigDecimal

data class AssetEntity(
  @BsonId
  val id: String,
  val type: AssetType,
  val sum: BigDecimal,
  val currency: String,
  val userId: String,
  val startDate: String? = null,
  val endDate: String? = null,
  val interestRate: BigDecimal? = null,
  val lock: String?,
)

enum class AssetType {
  CASH,
  DEPOSIT,
}
