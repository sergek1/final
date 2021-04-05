package com.sergek.yandex2.classes.DB_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.sergek.yandex2.classes.classes_for_a_saving.Main_stock_save
import java.lang.Exception


class DB_main_stock (context: Context, factory: SQLiteDatabase.CursorFactory?, version: Int):
        SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "main_stock.db"
        private val DATABASE_VERSION = 1

        val ID = "id"
        val STOCK_TABLE_NAME = "main_stock_tab"
        val STOCK_TIKER = "tiker"
        val NAME_COMPANY = "name"
        val CURRENCY = "currency"
        val FAVOURITE = "favourite"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_STOCK_TABLE: String = ("CREATE TABLE IF NOT EXISTS $STOCK_TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$CURRENCY TEXT," +
                "$NAME_COMPANY TEXT," +
                "$STOCK_TIKER," +
                "$FAVOURITE INTEGER )")
        db?.execSQL(CREATE_STOCK_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun getmainstock(mCtx: Context): ArrayList<Main_stock_save> {
        val qry = "Select * From $STOCK_TABLE_NAME"
        val db_main_stock: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        val stocks: ArrayList<Main_stock_save> = ArrayList()
        var i = 0

        if (cursor.count == 0)
        else {
            while (cursor.moveToNext()) {
                i++
                val stocks_1 = Main_stock_save(i,
                        cursor.getString(cursor.getColumnIndex(CURRENCY)),
                        cursor.getString(cursor.getColumnIndex(NAME_COMPANY)),
                        cursor.getString(cursor.getColumnIndex(STOCK_TIKER)),
                        cursor.getInt(cursor.getColumnIndex(FAVOURITE))
                )
                stocks.add(stocks_1)

            }
        }
        cursor.close()
        db_main_stock.close()
        return stocks

    }

    fun addmainstock(mCtx: Context, addstock: ArrayList<Main_stock_save>) {
        val db: SQLiteDatabase = this.writableDatabase
        if (addstock.size != 0) {
            for (ii in 0..addstock.size - 1) {
                val values = ContentValues()
                values.put(ID, ii)
                values.put(NAME_COMPANY, addstock[ii].description)
                values.put(STOCK_TIKER, addstock[ii].displaySymbol)
                values.put(CURRENCY, addstock[ii].currency)
                values.put(FAVOURITE, addstock[ii].favourite)

                try {
                    db.insert(STOCK_TABLE_NAME, null, values)
                } catch (e: Exception) {
                    Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        db.close()

    }

    fun getcountstock(): Int {
        val qry = "Select * From $STOCK_TABLE_NAME"
        val db_main_stock: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        return cursor.count
    }

    fun cleardbstock() {

        val qry = "DELETE FROM $STOCK_TABLE_NAME"
        val db_main_stock: SQLiteDatabase = this.writableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        cursor.close()
        db_main_stock.execSQL(qry)
        db_main_stock.close()

    }


    fun addfav_2(favstock: Main_stock_save) {
        val db_main_stock = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FAVOURITE, 1)
        try {
            db_main_stock.update(STOCK_TABLE_NAME, contentValues, "$ID = ?", arrayOf((favstock.id-1).toString()))
            db_main_stock.close()
        } catch (e: Exception) {

        }
    }


    fun dellfav_2(favstock: Main_stock_save) {
        val db_main_stock = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FAVOURITE, 0)
        try {
            db_main_stock.update(STOCK_TABLE_NAME, contentValues, "$ID = ?", arrayOf((favstock.id-1).toString()))
            db_main_stock.close()
        } catch (e: Exception) {

        }
    }
}