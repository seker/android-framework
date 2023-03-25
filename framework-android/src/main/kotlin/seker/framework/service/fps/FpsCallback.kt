package seker.framework.service.fps

/**
 * 这个接口的回调函数比较固定，应该不会变化了，所以设计成interface而不是abstract class
 *
 * @author xinwen
 * @since 2019.10.18
 */
interface FpsCallback {
    /**
     * on fps each seconds.
     *
     * @param name      fps task name
     * @param index     callback times
     * @param fps       fps
     */
    fun onFps(name: String, index: Int, fps: Int)
}