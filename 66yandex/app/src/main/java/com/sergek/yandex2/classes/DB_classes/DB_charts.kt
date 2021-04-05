package com.sergek.yandex2.classes.DB_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.sergek.yandex2.classes.classes_for_a_saving.Recomm_save
import java.lang.Exception

class DB_charts (context: Context, factory: SQLiteDatabase.CursorFactory?, version: Int):
        SQLiteOpenHelper(context,DATABASE_NAME_C,factory,DATABASE_VERSION_C) {
    companion object {
        private val DATABASE_NAME_C = "charts.db"
        private val DATABASE_VERSION_C = 1

        val STOCK_TABLE_NAME_C = "charts_tab"

        val BUY = "buy"
        val HOLD = "hold"
        val PERIOD = "period"
        val SELL = "sell"
        val STRONGBUY = "strongbuy"
        val STRONGSELL = "strongsell"
        val SYMBOL = "symbol"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TRANDS_TABLE: String = ("CREATE TABLE IF NOT EXISTS $STOCK_TABLE_NAME_C (" +
                "$BUY INTEGER," +
                "$HOLD INTEGER," +
                "$PERIOD TEXT," +
                "$SELL INTEGER," +
                "$STRONGBUY INTEGER," +
                "$STRONGSELL INTEGER," +
                "$SYMBOL TEXT )" )
        db?.execSQL(CREATE_TRANDS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun getcharts(mCtx: Context): ArrayList<Recomm_save> {
        val qry = "Select * From $STOCK_TABLE_NAME_C"
        val db_main_stock_n: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock_n.rawQuery(qry, null)
        val charts: ArrayList<Recomm_save> = ArrayList()
        var i = 0

        if (cursor.count == 0)
        else {
            while (cursor.moveToNext()) {
                i++
                val charts_1 = Recomm_save(
                        cursor.getInt(cursor.getColumnIndex(BUY)),
                        cursor.getInt(cursor.getColumnIndex(HOLD)),
                        cursor.getString(cursor.getColumnIndex(PERIOD)),
                        cursor.getInt(cursor.getColumnIndex(SELL)),
                        cursor.getInt(cursor.getColumnIndex(STRONGBUY)),
                        cursor.getInt(cursor.getColumnIndex(STRONGSELL)),
                        cursor.getString(cursor.getColumnIndex(SYMBOL))
                )
                charts.add(charts_1)

            }
            Toast.makeText(mCtx,"Charts ${cursor.count.toString()} records found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db_main_stock_n.close()
        return charts

    }

    fun addcharts(mCtx: Context, addcharts: ArrayList<Recomm_save>) {
        val db: SQLiteDatabase = this.writableDatabase
        if (addcharts.size != 0) {
            for (ii in 0..addcharts.size - 1) {
                val values = ContentValues()
//                values.put(ID_N, ii)
                values.put(BUY, addcharts[ii].buy)
                values.put(HOLD, addcharts[ii].hold)
                values.put(PERIOD, addcharts[ii].period)
                values.put(SELL, addcharts[ii].sell)
                values.put(STRONGBUY, addcharts[ii].strongBuy)
                values.put(STRONGSELL, addcharts[ii].strongSell)
                values.put(SYMBOL, addcharts[ii].symbol)

                try {
                    db.insert(STOCK_TABLE_NAME_C, null, values)
                } catch (e: Exception) {
                    Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        db.close()

    }

    fun getcouncharts(): Int {
        val qry = "Select * From $STOCK_TABLE_NAME_C"
        val db_main_stock: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        return cursor.count
    }

    fun cleardbcharts(){

        val qry = "DELETE   FROM  $STOCK_TABLE_NAME_C"
        val db_main_stock: SQLiteDatabase = this.writableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        val contentValues = ContentValues()
        db_main_stock.execSQL(qry)
        cursor.close()
        db_main_stock.close()

    }
}