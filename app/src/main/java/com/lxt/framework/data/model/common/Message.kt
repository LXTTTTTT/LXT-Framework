package com.lxt.framework.data.model.common

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.lxt.framework.common.global.Constant
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = Constant.MESSAGE_TABLE)
data class Message (
    @PrimaryKey(autoGenerate = true) var id:Long = 0,

    @ColumnInfo(name = "number", defaultValue = "1998") var number:String?,
    @ColumnInfo(name = "content", defaultValue = "lxt") var content:String?,
    @ColumnInfo(name = "content2", defaultValue = "lxt") var content2:String?,
    @ColumnInfo(name = "time", defaultValue = "1018") var time:Long?,

    @ColumnInfo(name = "longitude") var longitude:Double?,
    @ColumnInfo(name = "latitude") var latitude:Double?,
    @ColumnInfo(name = "altitude") var altitude:Double?,

) : Parcelable{
    @Ignore
    constructor():this(0,"","","",0,0.0,0.0,0.0)
}