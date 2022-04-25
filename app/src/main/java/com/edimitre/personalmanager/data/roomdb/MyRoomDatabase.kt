package com.edimitre.personalmanager.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edimitre.personalmanager.data.dao.*
import com.edimitre.personalmanager.data.model.*


@Database(
    entities = [MainUser::class, Product::class,
        Expense::class, Description::class,
        MonthlyIncomeType::class, MonthlyIncome::class,
        Reminder::class,DailyReport::class], version = 1, exportSchema = false
)
@TypeConverters(MyRoomDbConverters::class)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract val mainUserDao: MainUserDao
    abstract val productDao: ProductDao
    abstract val expenseDao: ExpenseDao
    abstract val descriptionDao: DescriptionDao
    abstract val monthlyIncomeDao: MonthlyIncomeDao
    abstract val reminderDao: ReminderDao
    abstract val dailyReportDao: DailyReportDao


    companion object {

        @Volatile
        private var INSTANCE: MyRoomDatabase? = null

        fun getInstance(context: Context): MyRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyRoomDatabase::class.java,
                        "PersonalManagerDatabase.db"
                    )
                        .fallbackToDestructiveMigration()
                        .setJournalMode(JournalMode.TRUNCATE)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}