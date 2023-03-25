package seker.framework.service.fps

import seker.framework.android.MicroService

/**
 * @author xinwen
 * @since 2019.10.18
 */
abstract class FpsService : MicroService() {
    /**
     * 获取一个fps任务
     *
     * @param name              fps name
     * @param callback          fps回调
     * @return                  fps任务
     */
    abstract fun register(name: String, callback: FpsCallback): FpsFeature

    /**
     * 释放fps任务
     *
     * @param callback          fps回调
     * @return                  true/false : 成功/失败
     */
    abstract fun unregister(callback: FpsCallback): Boolean
}