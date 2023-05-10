package woowacourse.shopping.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import woowacourse.shopping.domain.CartProduct

class CartDBHelper(context: Context) : SQLiteOpenHelper(context, "cart", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${CartConstract.TABLE_NAME} (" +
                "  ${CartConstract.TABLE_COLUMN_ID} Int PRIMARY KEY not null," +
                "  ${CartConstract.TABLE_COLUMN_COUNT} Int not null" +
                ");",
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${CartConstract.TABLE_NAME}")
        onCreate(db)
    }

    fun insert(id: Int, count: Int) {
        val values = ContentValues()
        values.put(CartConstract.TABLE_COLUMN_ID, id)
        values.put(CartConstract.TABLE_COLUMN_COUNT, count)
        writableDatabase.insert(CartConstract.TABLE_NAME, null, values)
    }

    fun remove(id: Int) {
        writableDatabase.execSQL("DELETE FROM ${CartConstract.TABLE_NAME} WHERE ${CartConstract.TABLE_COLUMN_ID}=$id")
    }

    fun selectAll(): List<CartProduct> {
        val products = mutableListOf<CartProduct>()
        val sql = "select * from ${CartConstract.TABLE_NAME}"
        val cursor = readableDatabase.rawQuery(sql, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(CartConstract.TABLE_COLUMN_ID))
            val count = cursor.getInt(cursor.getColumnIndexOrThrow(CartConstract.TABLE_COLUMN_COUNT))
            products.add(CartProduct(id, count))
        }
        cursor.close()
        return products
    }
}