package ru.otus.otuskotlin.financier.asset.business.utils

import ru.otus.otuskotlin.financier.asset.business.exception.ServiceException
import ru.otus.otuskotlin.financier.asset.common.model.Asset
import ru.otus.otuskotlin.financier.asset.common.model.Cash
import ru.otus.otuskotlin.financier.asset.common.model.Deposit

fun Asset.copy() = when(this) {
    is Cash -> this.copy()
    is Deposit -> this.copy()
    else -> throw ServiceException("Type ${this::class.java} is unknown")
}