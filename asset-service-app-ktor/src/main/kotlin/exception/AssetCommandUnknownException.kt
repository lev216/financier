package ru.otus.otuskotlin.financier.asset.exception

import ru.otus.otuskotlin.financier.asset.common.model.AssetCommand

class AssetCommandUnknownException(command: AssetCommand) : RuntimeException("Command [$command] is not supported")