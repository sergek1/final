package com.sergek.yandex2.classes.DB_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.sergek.yandex2.classes.classes_for_a_saving.Data_news_save
import com.sergek.yandex2.classes.classes_for_a_saving.Main_stock_save
import java.lang.Exception

class DB_News (context: Context, factory: SQLiteDatabase.CursorFactory?, version: Int):
        SQLiteOpenHelper(context,DATABASE_NAME_N,factory,DATABASE_VERSION_N) {
    companion object {
        private val DATABASE_NAME_N = "news.db"
        private val DATABASE_VERSION_N = 1

        val STOCK_TABLE_NAME_N = "news_tab"

        val CATEGORY = "category"
        val  DATATIME= "datatime"
        val  HEADLINE= "headline"
        val  SUMMARY= "summary"
        val  IMAGE= "image"
        val RELATED = "related"
        val  SOURCE= "source"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_STOCK_TABLE: String = ("CREATE TABLE IF NOT EXISTS $STOCK_TABLE_NAME_N (" +
                "$CATEGORY TEXT," +
                "$DATATIME INTEGER," +
                "$HEADLINE TEXT," +
                "$SUMMARY TEXT," +
                "$IMAGE TEXT," +
                "$RELATED TEXT," +
                "$SOURCE TEXT )" )
        db?.execSQL(CREATE_STOCK_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun getmainstock_news(mCtx: Context): ArrayList<Data_news_save> {
        val qry = "Select * From $STOCK_TABLE_NAME_N"
        val db_main_stock_n: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock_n.rawQuery(qry, null)
        val stocks: ArrayList<Data_news_save> = ArrayList()
        var i = 0

        if (cursor.count == 0)
        else {
            while (cursor.moveToNext()) {
                i++
                val stocks_1 = Data_news_save(
                        cursor.getString(cursor.getColumnIndex(CATEGORY)),
                        cursor.getInt(cursor.getColumnIndex(DATATIME)),
                        cursor.getString(cursor.getColumnIndex(HEADLINE)),
                        cursor.getString(cursor.getColumnIndex(SUMMARY)),
                        cursor.getString(cursor.getColumnIndex(IMAGE)),
                        cursor.getString(cursor.getColumnIndex(RELATED)),
                        cursor.getString(cursor.getColumnIndex(SOURCE))
                )
                stocks.add(stocks_1)

            }
        }
        cursor.close()
        db_main_stock_n.close()
        return stocks

    }

    fun addmainstock_news(mCtx: Context, addstock: ArrayList<Data_news_save>) {
        val db: SQLiteDatabase = this.writableDatabase
        if (addstock.size != 0) {
            for (ii in 0..addstock.size - 1) {
                val values = ContentValues()
                values.put(CATEGORY, addstock[ii].category)
                values.put(DATATIME, addstock[ii].datetime)
                values.put(HEADLINE, addstock[ii].headline)
                values.put(SUMMARY, addstock[ii].summary)
                values.put(IMAGE, addstock[ii].image)
                values.put(RELATED, addstock[ii].related)
                values.put(SOURCE, addstock[ii].source)

                try {
                    db.insert(STOCK_TABLE_NAME_N, null, values)
                } catch (e: Exception) {
                    Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        db.close()

    }

    fun getcountstock(): Int {
        val qry = "Select * From $STOCK_TABLE_NAME_N"
        val db_main_stock: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        return cursor.count
    }

    fun cleardbstock(){

        val qry = "DELETE   FROM  $STOCK_TABLE_NAME_N"
        val db_main_stock: SQLiteDatabase = this.writableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        val contentValues = ContentValues()
        db_main_stock.execSQL(qry)
        cursor.close()
        db_main_stock.close()

    }
}