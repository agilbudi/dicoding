package com.abupras.eventapp.data.local.entitiy

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "event")
@Parcelize
data class EventEntity(
    @ColumnInfo(name = "event_id")
    @PrimaryKey
    val eventId: Int,

    @ColumnInfo(name = "summary")
    val summary: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo(name = "mediaCover")
    val mediaCover: String,

    @ColumnInfo("ownerName")
    val ownerName: String,

    @ColumnInfo(name = "imageLogo")
    val imageLogo: String,

    @ColumnInfo(name = "cityName")
    val cityName: String,

    @ColumnInfo(name = "quota")
    val quota: Int,

    @ColumnInfo(name = "registrants")
    val registrants: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "beginTime")
    val beginTime: String,

    @ColumnInfo(name = "endTime")
    val endTime: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo("link")
    val link: String,

    @ColumnInfo(name = "favorite")
    var isFavorite: Boolean
): Parcelable
