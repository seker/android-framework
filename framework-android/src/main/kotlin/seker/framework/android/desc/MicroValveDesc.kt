package seker.framework.android.desc

import seker.framework.android.Framework
import seker.framework.android.MicroValve

/**
 * @author xinjian
 */
data class MicroValveDesc(
    /**
     * class
     */
    val clazz: Class<out MicroValve>,
    /**
     * TaskQueue 名字
     */
    val taskQueueName: String = Framework.COOL_START_TASK_QUEUE,
    /**
     * 权重
     */
    val priority: Int = 0,
) {

    override fun toString(): String {
        return "MicroValveDescription{" +
                "taskQueueName='" + taskQueueName + '\'' +
                ", clazz=" + clazz +
                ", priority=" + priority +
                '}'
    }
}