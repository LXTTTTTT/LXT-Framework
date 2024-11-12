package com.lxt.framework.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lxt.framework.common.global.Constant
import com.lxt.framework.data.model.common.User

@Dao
interface UserDao {

    @Insert(entity = User::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(user: MutableList<User>)

    @Delete
    fun delete(user: User): Int

    @Query("DELETE FROM ${Constant.USER_TABLE}")
    fun deleteAll()

    @Update
    fun update(user: User): Int

    @Query("SELECT * FROM ${Constant.USER_TABLE}")
    fun queryAll(): MutableList<User>?

    @Query("SELECT * FROM ${Constant.USER_TABLE} WHERE id=:id")
    fun queryById(id: Long): User?

}