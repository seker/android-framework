package seker.framework.service.countdown

import seker.framework.android.MicroService
import java.util.concurrent.TimeUnit

/**
 * 倒计时服务
 *
 * @author xinwen
 * @since 2021.04.21
 */
abstract class CountdownService : MicroService() {
    /**
     * 请求一个倒计时任务，默认时间间隔为1S
     *
     * @param total             倒计时总次数
     * @param callback          倒计时回调接口
     * @return                  true/false：成功/失败
     */
    fun register(total: Int, callback: CountdownCallback): CountdownFeature {
        return register(total, 1, TimeUnit.SECONDS, callback)
    }

    /**
     * 请求一个倒计时任务
     *
     * @param total             倒计时总次数
     * @param period            时间间隔单位数
     * @param unit              时间间隔的单位
     * @param callback          倒计时回调接口
     * @return                  true/false：成功/失败
     */
    abstract fun register(total: Int, period: Long, unit: TimeUnit?, callback: CountdownCallback): CountdownFeature
}