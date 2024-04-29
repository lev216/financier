package ru.otus.otuskotlin.financier.asset.common.persistence

import ru.otus.otuskotlin.financier.asset.common.model.AssetError

interface IDbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<AssetError>
}