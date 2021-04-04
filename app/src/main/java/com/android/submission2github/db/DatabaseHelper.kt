package com.android.submission2github.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.TABLE_NAME
import com.android.submission2github.db.DatabaseContract.UserColumn

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbuserfavorite"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumn._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${UserColumn.LOGIN} TEXT NOT NULL," +
                " ${UserColumn.NODE_ID} TEXT NOT NULL," +
                " ${UserColumn.AVATAR_URL} TEXT NOT NULL," +
                " ${UserColumn.TYPE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    /*
    Method onUpgrade akan di panggil ketika terjadi perbedaan versi
    Gunakan method onUpgrade untuk melakukan proses migrasi data
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
        */
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}