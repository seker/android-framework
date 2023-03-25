package seker.framework.service.countdown

/**
 * 这个接口的回调函数比较固定，应该不会变化了，所以设计成interface而不是abstract class
 *
 * @author xinwen
 * @since 2021.04.21
 */
interface CountdownCallback {
    /**
     * 倒计时回调
     *
     * @param total     总次数
     * @param count     当前次数
     */
    fun onCountdown(total: Int, count: Int)
}