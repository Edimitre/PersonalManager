package com.edimitre.personalmanager.system

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.edimitre.personalmanager.data.utils.TimeUtils

class MyBackGroundWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val mySystemService: SystemService = SystemService(context)

    override fun doWork(): Result {

        backUpDatabase()

        val hour: Int = TimeUtils().getCurrentHour()

        if (hour > 9) {
            mySystemService.notify(
                "PersonalManager",
                "Ka ndonje shpenzim te harruar ?"
            )
        }

        return Result.success()
    }

    private fun backUpDatabase() {
        mySystemService.exportDatabase()
    }


}