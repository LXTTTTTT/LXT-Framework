package com.lxt.framework.common.taskDispatch.state

import android.util.Log
import java.util.concurrent.atomic.AtomicInteger

object TaskStat {
    @Volatile
    private var sCurrentSituation = ""
    private val TAG = "TaskStat"
    private val sBeans: MutableList<TaskStatBean> = ArrayList()
    private var sTaskDoneCount = AtomicInteger()
    private const val sOpenLaunchStat = false // 是否开启统计
    var currentSituation: String
        get() = sCurrentSituation
        set(currentSituation) {
            if (!sOpenLaunchStat) {
                return
            }
            Log.i(TAG, "currentSituation   $currentSituation")
            sCurrentSituation = currentSituation
            setLaunchStat()
        }

    fun markTaskDone() {
        sTaskDoneCount.getAndIncrement()
    }

    fun setLaunchStat() {
        val bean = TaskStatBean()
        bean.situation = sCurrentSituation
        bean.count = sTaskDoneCount.get()
        sBeans.add(bean)
        sTaskDoneCount = AtomicInteger(0)
    }
}
internal class TaskStatBean {
    var situation: String? = null
    var count = 0
}