package seker.framework.logger

import seker.framework.logger.Logger.Companion.ASSERT
import seker.framework.logger.Logger.Companion.ERROR
import seker.framework.logger.Logger.Companion.WARN
import seker.framework.logger.Logger.Companion.INFO
import seker.framework.logger.Logger.Companion.DEBUG
import seker.framework.logger.Logger.Companion.VERBOSE

object Log {

    private const val TAG = "Framework"

    private var targetLogger: Logger? = null

    fun setLogger(logger: Logger) {
        targetLogger = logger
    }

    fun setPriority(priority: Int) {
        targetLogger?.setLogPriority(priority)
    }

    fun v(tr: Throwable): Int {
        return v(tag = TAG, tr = tr)
    }

    fun v(msg: String, tr: Throwable? = null): Int {
        return v(tag = TAG, msg = msg, tr = tr)
    }

    fun v(tag: String, msg: String? = null, tr: Throwable? = null): Int {
        return if (null == targetLogger) -1 else targetLogger!!.log(VERBOSE, tag, msg, tr)
    }

    fun d(tr: Throwable): Int {
        return d(tag = TAG, tr = tr)
    }

    fun d(msg: String, tr: Throwable? = null): Int {
        return d(tag = TAG, msg = msg, tr = tr)
    }

    fun d(tag: String = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (null == targetLogger) -1 else targetLogger!!.log(DEBUG, tag, msg, tr)
    }

    fun i(tr: Throwable): Int {
        return i(tag = TAG, tr = tr)
    }

    fun i(msg: String, tr: Throwable? = null): Int {
        return i(tag = TAG, msg = msg, tr = tr)
    }

    fun i(tag: String = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (null == targetLogger) -1 else targetLogger!!.log(INFO, tag, msg, tr)
    }

    fun w(tr: Throwable): Int {
        return w(tag = TAG, tr = tr)
    }

    fun w(msg: String, tr: Throwable? = null): Int {
        return w(tag = TAG, msg = msg, tr = tr)
    }

    fun w(tag: String = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (null == targetLogger) -1 else targetLogger!!.log(WARN, tag, msg, tr)
    }

    fun e(tr: Throwable): Int {
        return e(tag = TAG, tr = tr)
    }

    fun e(msg: String, tr: Throwable? = null): Int {
        return e(tag = TAG, msg = msg, tr = tr)
    }

    fun e(tag: String = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (null == targetLogger) -1 else targetLogger!!.log(ERROR, tag, msg, tr)
    }

    fun f(tr: Throwable): Int {
        return f(tag = TAG, tr = tr)
    }

    fun f(msg: String, tr: Throwable? = null): Int {
        return f(tag = TAG, msg = msg, tr = tr)
    }

    fun f(tag: String = TAG, msg: String? = null, tr: Throwable? = null): Int {
        return if (null == targetLogger) -1 else targetLogger!!.log(ASSERT, tag, msg, tr)
    }
}