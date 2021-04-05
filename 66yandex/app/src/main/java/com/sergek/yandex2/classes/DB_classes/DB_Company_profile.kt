package com.sergek.yandex2.classes.DB_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.sergek.yandex2.classes.classes_for_a_saving.Main_stock_save
import com.sergek.yandex2.classes.classes_for_a_saving.Name_save
import com.sergek.yandex2.classes.classes_for_a_saving.Price_save
import java.lang.Exception

class DB_Company_profile (context: Context, factory: SQLiteDatabase.CursorFactory?, version: Int):
        SQLiteOpenHelper(context,DATABASE_NAME_C,factory,DATABASE_VERSION_C) {
    companion object {
        private val DATABASE_NAME_C = "company_info.db"
        private val DATABASE_VERSION_C = 1

        val COMPANY_TABLE_NAME = "company_info_tab"

        val  LOGO = "logo"
        val NAME_COMPANY = "name_company"
        val COUNTRY = "country"
        val IPO = "ipo"
        val WEBURL = "weburl"
        val PHONE = "phole"
        val CURRENTPRICE = "current"
        val HIHGPRICE = "high"
        val LOWPRICE = "low"
        val OPENPRICE= "open"
        val CURRENCY = "currency"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_STOCK_TABLE: String = ("CREATE TABLE IF NOT EXISTS $COMPANY_TABLE_NAME (" +
                "$LOGO TEXT," +
                "$NAME_COMPANY TEXT," +
                "$COUNTRY TEXT," +
                "$IPO TEXT," +
                "$CURRENCY TEXT ," +
                "$WEBURL TEXT ," +
                "$PHONE TEXT," +
                "$CURRENTPRICE REAL," +
                "$HIHGPRICE REAL," +
                "$LOWPRICE REAL," +
                "$OPENPRICE REAL ) ")
        db?.execSQL(CREATE_STOCK_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun getcomp_1(mCtx: Context): ArrayList<Name_save>  {
        val qry = "Select * From $COMPANY_TABLE_NAME"
        val db_company: SQLiteDatabase = this.readableDatabase
        val cursor = db_company.rawQuery(qry, null)
        var stocks_1 : ArrayList<Name_save> =  ArrayList()

        if (cursor.count == 0) {
            Toast.makeText(mCtx, "No stock records found", Toast.LENGTH_SHORT).show()
        }
        else {
                while (cursor.moveToNext()) {
                        val stocks_2 = Name_save(
                                cursor.getString(cursor.getColumnIndex(COUNTRY)),
                                cursor.getString(cursor.getColumnIndex(CURRENCY)),
                                cursor.getString(cursor.getColumnIndex(IPO)),
                                cursor.getString(cursor.getColumnIndex(LOGO)),
                                cursor.getString(cursor.getColumnIndex(NAME_COMPANY)),
                                cursor.getString(cursor.getColumnIndex(PHONE)),
                                "",
                                cursor.getString(cursor.getColumnIndex(WEBURL))
                        )
                        stocks_1.add(stocks_2)
                }
        }
        cursor.close()
        db_company.close()
        return stocks_1

    }

    fun getcomp_2(mCtx: Context): ArrayList<Price_save>  {
        val qry = "Select * From $COMPANY_TABLE_NAME"
        val db_company: SQLiteDatabase = this.readableDatabase
        val cursor = db_company.rawQuery(qry, null)
        var stock_1 = ArrayList<Price_save>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "No stock records found", Toast.LENGTH_SHORT).show()
        else {

            while (cursor.moveToNext()) {
                    val stock_2 = Price_save(cursor.getDouble(cursor.getColumnIndex(CURRENTPRICE)),
                            cursor.getDouble(cursor.getColumnIndex(HIHGPRICE)),
                            cursor.getDouble(cursor.getColumnIndex(LOWPRICE)),
                            cursor.getDouble(cursor.getColumnIndex(OPENPRICE)),
                            0.0)
                    stock_1.add(stock_2)
            }
        }
        cursor.close()
        db_company.close()
        return stock_1
    }


    fun addcopm_1(mCtx: Context, addstock: ArrayList<Name_save>) {
        val db: SQLiteDatabase = this.writableDatabase
            val values = ContentValues()
            values.put(COUNTRY,addstock[0].country )
            values.put(CURRENCY, addstock[0].currency)
            values.put(IPO, addstock[0].ipo)
            values.put(LOGO, addstock[0].logo)
            values.put(NAME_COMPANY, addstock[0].name)
            values.put(PHONE, addstock[0].phone)
            values.put(WEBURL, addstock[0].weburl)
            values.put(CURRENTPRICE, -1.0)
            values.put(HIHGPRICE, -1.0)
            values.put(LOWPRICE, -1.0)
            values.put(OPENPRICE, -1.0)

                try {
                    db.insert(COMPANY_TABLE_NAME, null, values)
                } catch (e: Exception) {
                    Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
                }
        db.close()
        }

    fun addcopm_2(mCtx: Context, addstock: ArrayList<Price_save>) {
        if (mCtx != null && addstock[0] !=  null) {
            val db: SQLiteDatabase = this.writableDatabase
            val values = ContentValues()
            values.put(COUNTRY, "")
            values.put(CURRENCY, "")
            values.put(IPO, "")
            values.put(LOGO, "")
            values.put(NAME_COMPANY, "")
            values.put(PHONE, "")
            values.put(WEBURL, "")
            values.put(CURRENTPRICE, addstock[0].c)
            values.put(HIHGPRICE, addstock[0].h)
            values.put(LOWPRICE, addstock[0].l)
            values.put(OPENPRICE, addstock[0].o)



            try {
                db.insert(COMPANY_TABLE_NAME, null, values)
            } catch (e: Exception) {
                Toast.makeText(mCtx, e.message, Toast.LENGTH_SHORT).show()
            }
            db.close()
        }
    }

    fun getcountstock(): Int {
        val qry = "Select * From $COMPANY_TABLE_NAME"
        val db_main_stock: SQLiteDatabase = this.readableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        return cursor.count
    }

    fun cleardbstock() {

        val qry = "DELETE FROM $COMPANY_TABLE_NAME"
        val db_main_stock: SQLiteDatabase = this.writableDatabase
        val cursor = db_main_stock.rawQuery(qry, null)
        db_main_stock.execSQL(qry)
        cursor.close()
        db_main_stock.close()
    }
}