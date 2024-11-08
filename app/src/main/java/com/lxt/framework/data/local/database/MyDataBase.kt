package com.lxt.framework.data.local.database

import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lxt.framework.MainApplication
import com.lxt.framework.common.global.Constant
import com.lxt.framework.data.local.dao.MessageDao
import com.lxt.framework.data.model.common.Message

/**
 * @date   2023/4/11 16:23
 * @desc   数据库操作类
 * 指定有哪些表，version必须指定版本，exportSchema生成一个json文件，方便排查问题，还需要在build.gradle文件中配置
 */
@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class MyDataBase : RoomDatabase() {
    //抽象方法或者抽象类标记
    abstract fun messageDao(): MessageDao
    companion object {
        val TAG = "MyDataBase"
        private var dataBase: MyDataBase? = null
        //同步锁，可能在多个线程中同时调用
        @Synchronized
        fun getInstance(): MyDataBase {
            val MIGRATION_1_2 = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    try {
                        database.execSQL("ALTER TABLE ${Constant.MESSAGE_TABLE} ADD COLUMN content2 TEXT DEFAULT 'lxt'")
                        Log.e(TAG, "数据库升级成功")
                    } catch (e: Exception) {
                        Log.e(TAG, "数据库升级失败")
                    }
                }
            }
            return dataBase ?: Room.databaseBuilder(MainApplication.getInstance(), MyDataBase::class.java, Constant.DATABASE_NAME)
                    //是否允许在主线程查询，默认是false
                    .allowMainThreadQueries()
                    //数据库被创建或者被打开时的回调
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            val cursor = db.query("PRAGMA user_version;")
                            cursor.moveToFirst()
                            val version = cursor.getInt(0)
                            cursor.close()
                            Log.e(TAG, "当前数据库版本: $version")
                        }
                    })
                    //指定数据查询的线程池，不指定会有个默认的
                    //.setQueryExecutor {  }
                    //任何数据库有变更版本都需要升级，升级的同时需要指定migration，如果不指定则会报错
                    //数据库升级 1-->2
                    .addMigrations(MIGRATION_1_2)
                    //设置数据库工厂，用来链接room和SQLite，可以利用自行创建SupportSQLiteOpenHelper，来实现数据库加密
                    //.openHelperFactory()
                    .build()
        }
    }
}