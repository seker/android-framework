package seker.framework.logger

import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

/**
 * @author xinjian
 * @since  2022-12-05
 */
abstract class Logger {

    companion object {
        /**
         * Priority constant for the println method; use Log.v.
         */
        const val VERBOSE = 2

        /**
         * Priority constant for the println method; use Log.d.
         */
        const val DEBUG = 3

        /**
         * Priority constant for the println method; use Log.i.
         */
        const val INFO = 4

        /**
         * Priority constant for the println method; use Log.w.
         */
        const val WARN = 5

        /**
         * Priority constant for the println method; use Log.e.
         */
        const val ERROR = 6

        /**
         * Priority constant for the println method.
         */
        const val ASSERT = 7
    }

    /**
     * log priority
     */
    private var logPriority = VERBOSE

    open fun setLogPriority(priority: Int) {
        logPriority = if (priority in VERBOSE..ASSERT) {
            priority
        } else {
            throw RuntimeException("priority should between [$VERBOSE , $ASSERT]")
        }
    }

    fun log(priority: Int, tag: String, msg: String? = null, tr: Throwable? = null): Int {
        return if (priority >= logPriority) {
            return if (null == msg)  {
                if (null == tr) {
                    -1
                } else {
                    println(priority, tag, getStackTraceString(tr))
                }
            } else {
                if (null == tr) {
                    println(priority, tag, msg)
                } else {
                    println(priority, tag, "$msg\n${getStackTraceString(tr)}".trimIndent())
                }
            }
        } else {
            -1
        }
    }

    /**
     * log out
     *
     * @param priority Log Priority
     * @param tag      Tag
     * @param msg      Message
     * @return The number of bytes written.
     */
    protected abstract fun println(priority: Int, tag: String, msg: String): Int

    /**
     * Convert Throwable to String
     *
     * @param tr Throwable
     * @return String
     */
    protected open fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}