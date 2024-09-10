package com.example.pedido.DatabaseHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.pedido.Model.Pedido
import com.example.pedido.R

class DatabaseHelperPedido(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_NAME = "pedido.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "pedido"
        private const val COLUMN_ID = "ID"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_PRECO = "preco"
        private const val COLUMN_QUANTIDADE = "quantidade"
        private const val COLUMN_PRECOTOTAL = "precoTotal"
    }

    override fun onCreate(db: SQLiteDatabase)
    {
        val createTable = ("CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NOME TEXT, " +
                "$COLUMN_PRECO REAL, " +
                "$COLUMN_QUANTIDADE INTEGER, " +
                "$COLUMN_PRECOTOTAL REAL)")
        db.execSQL(createTable  )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun adicionarPedido(pedido: Pedido)
    {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NOME, pedido.nome)
            put(COLUMN_PRECO, pedido.preco)
            put(COLUMN_QUANTIDADE, pedido.quantidade)
            put(COLUMN_PRECOTOTAL, pedido.precoTotal)
        }

        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    fun obterPedido(): List<Pedido>
    {
        val pedidos = mutableListOf<Pedido>()

        val db = this.readableDatabase

        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NOME, COLUMN_PRECO, COLUMN_QUANTIDADE, COLUMN_PRECOTOTAL), null, null, null, null, null)

        with(cursor)
        {
            while (moveToNext())
            {
                val pedido = Pedido(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    nome = getString(getColumnIndexOrThrow(COLUMN_NOME)),
                    preco = getFloat(getColumnIndexOrThrow(COLUMN_PRECO)),
                    quantidade = getInt(getColumnIndexOrThrow(COLUMN_QUANTIDADE)),
                    precoTotal = getFloat(getColumnIndexOrThrow(COLUMN_PRECOTOTAL))
                )

                pedidos.add(pedido)
            }
        }

        cursor.close()
        db.close()

        return pedidos
    }

    fun obterPedidoPorID(id: Int): Pedido?
    {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NOME, COLUMN_PRECO, COLUMN_QUANTIDADE, COLUMN_PRECOTOTAL),
            "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null)

        var pedido: Pedido? = null

        if(cursor.moveToFirst())
        {
            pedido = Pedido(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
                preco = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRECO)),
                quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTIDADE)),
                precoTotal = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRECOTOTAL))
            )
        }

        cursor.close()
        db.close()

        return pedido
    }

    fun atualizarPedido(pedido: Pedido): Int
    {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NOME, pedido.nome)
            put(COLUMN_PRECO, pedido.preco)
            put(COLUMN_QUANTIDADE, pedido.quantidade)
            put(COLUMN_PRECOTOTAL, pedido.precoTotal)
        }

        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(pedido.id.toString()))
    }

    fun deletarPedido(id: Int): Int
    {
        val db = this.writableDatabase

        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

}