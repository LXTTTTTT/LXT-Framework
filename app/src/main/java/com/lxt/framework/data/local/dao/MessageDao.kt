package com.lxt.framework.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lxt.framework.common.global.Constant
import com.lxt.framework.data.model.common.Message

@Dao
interface MessageDao {

    @Insert(entity = Message::class, onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)  // 这条VideoInfo对象新的数据，id已经存在了这个表当中，此时就会发生冲突

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(messages: MutableList<Message>)

    @Query("DELETE FROM ${Constant.MESSAGE_TABLE} WHERE id=:id")
    fun deleteById(id: Long)

    @Delete
    fun delete(message: Message): Int

    /**
     * 删除表中所有数据
     */
    @Query("DELETE FROM ${Constant.MESSAGE_TABLE}")
    fun deleteAll()

    /**
     * 更新某个item
     * 不指定的entity也可以，会根据你传入的参数对象来找到你要操作的那张表
     */
    @Update
    fun update(videoInfo: Message): Int

    /**
     * 根据id更新数据
     */
//    @Query("UPDATE ${Constant.MESSAGE_TABLE} SET number=:number WHERE id=:id")
//    fun updateById(id: Long, number: String)

    /**
     * 查询所有数据
     */
    @Query("SELECT * FROM ${Constant.MESSAGE_TABLE}")
    fun queryAll(): MutableList<Message>?

    /**
     * 根据id查询某个数据
     */
    @Query("SELECT * FROM ${Constant.MESSAGE_TABLE} WHERE id=:id")
    fun queryById(id: Long): Message?

    /**
     * 通过LiveData以观察者的形式获取数据库数据，可以避免不必要的NPE，
     * 可以监听数据库表中的数据的变化，也可以和RXJava的Observer使用
     * 一旦发生了insert，update，delete，room会自动读取表中最新的数据，发送给UI层，刷新页面
     * 不要使用MutableLiveData和suspend 会报错
     */
    @Query("SELECT * FROM ${Constant.MESSAGE_TABLE}")
    fun queryAllLiveData(): LiveData<List<Message>>
}