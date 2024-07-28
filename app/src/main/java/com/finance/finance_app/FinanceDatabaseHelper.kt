package com.finance.finance_app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FinanceDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "finance.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "finance"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_SUBCATEGORY = "subcategory"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_SPEND = "spend"
        private const val COLUMN_DATE = "date"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_CATEGORY TEXT, $COLUMN_SUBCATEGORY TEXT, $COLUMN_DESCRIPTION TEXT, $COLUMN_SPEND REAL, $COLUMN_DATE TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropQueryTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropQueryTable)
        onCreate(db)
    }

    fun insertFinance(finance: Finance) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY, finance.category)
            put(COLUMN_SUBCATEGORY, finance.subcategory)
            put(COLUMN_DESCRIPTION, finance.description)
            put(COLUMN_SPEND, finance.spend)
            put(COLUMN_DATE, finance.date)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getFinances(): List<Finance>{
        val financeList = mutableListOf<Finance>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY ID DESC"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
            val subcategory = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBCATEGORY))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val spend = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_SPEND))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            val finance = Finance(id,category, subcategory, description, spend, date)
            financeList.add(finance)
        }

        cursor.close()
        db.close()
        return financeList
    }
    fun deleteFinance(financeID: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(financeID.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}