package ru.otus.otuskotlin.financier.asset.api.v1.exception

import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand

class AssetCommandUnknownException(command: AssetCommand) : RuntimeException("Command [$command] is not supported")