package com.edimitre.personalmanager.data.dao

import androidx.room.*
import com.edimitre.personalmanager.data.model.MainUser

@Dao
interface MainUserDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(mainUser: MainUser)

    @Delete
    suspend fun delete(mainUser: MainUser)

    @Query("SELECT * FROM main_user_table WHERE id = 1")
    suspend fun getMainUser(): MainUser?

}