package org.example.roundapp.core.data.logging

import co.touchlab.kermit.Logger
import org.example.roundapp.core.domain.logger.AppLogger

object KermitLogger : AppLogger {
    private val logger = Logger.withTag("RoundApp")

    override fun debug(message: String) = logger.d(message)
    override fun info(message: String) = logger.i(message)
    override fun warn(message: String) = logger.w(message)
    override fun error(message: String, throwable: Throwable?) = logger.e(message, throwable)
}
