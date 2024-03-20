package ru.otus.otuskotlin.financier.asset.business.exception

import java.lang.RuntimeException

class ServiceException : RuntimeException {

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}