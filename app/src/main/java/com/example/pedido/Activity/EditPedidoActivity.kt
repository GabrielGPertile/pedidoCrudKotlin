package com.example.pedido.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.pedido.DatabaseHelper.DatabaseHelperPedido
import com.example.pedido.Model.Pedido
import com.example.pedido.R

class EditPedidoActivity : AppCompatActivity()
{
    private lateinit var databaseHelperPedido: DatabaseHelperPedido
    private lateinit var etNome: EditText
    private lateinit var etPreco: EditText
    private lateinit var etQuantidade: EditText
    private lateinit var etPrecoTotal: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnDeletar: Button
    private var pedidoID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_pedido_activity)

        databaseHelperPedido = DatabaseHelperPedido(this)
        etNome = findViewById(R.id.etNome)
        etPreco = findViewById(R.id.etPreco)
        etQuantidade = findViewById(R.id.etQuantidade)
        etPrecoTotal = findViewById(R.id.etPrecoTotal)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnDeletar = findViewById(R.id.btnDeletar)

        //receber o ID do pedido da Intent
        pedidoID = intent.getIntExtra("PEDIDO_ID", 0)

        //carregar os dados do pedido no formulario
        carregarDadosPedido(pedidoID)

        btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }

        btnDeletar.setOnClickListener{
            mostrarConfirmacaoDelecao()
        }
    }

    private fun carregarDadosPedido(id: Int)
    {
        val pedido = databaseHelperPedido.obterPedidoPorID(id)

        if(pedido != null)
        {
            etNome.setText(pedido.nome)
            etPreco.setText(pedido.preco.toString()) // Conversão para String
            etQuantidade.setText(pedido.quantidade.toString()) // Conversão para String
            etPrecoTotal.setText(pedido.precoTotal.toString()) // Conversão para String
        }
    }

    private fun salvarAlteracoes()
    {
        val nome = etNome.text.toString()
        val preco = etPreco.text.toString().toFloatOrNull() ?: return // Conversão para Float
        val quantidade = etQuantidade.text.toString().toIntOrNull() ?: return // Conversão para Int
        val precoTotal = etPrecoTotal.text.toString().toFloatOrNull() ?: return // Conversão para Float

        if (nome.isNotEmpty() && preco != null && quantidade != null && precoTotal != null)
        {
            val pedido = Pedido(id = pedidoID, nome = nome, preco = preco, quantidade = quantidade, precoTotal = precoTotal)

            databaseHelperPedido.atualizarPedido(pedido)

            Toast.makeText(this, "Pedido Atualizado!", Toast.LENGTH_SHORT).show()

            finish() // fecha a atividade e volta para a MainActitvity
        }
        else
        {
            Toast.makeText(this, "Por Favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarConfirmacaoDelecao()
    {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Deletar Pedido")
        builder.setMessage("Tem certeza de que deseja deletar este pedido?")
        builder.setPositiveButton("Sim") { dialog, which ->
            deletarPedido()
        }

        builder.setNegativeButton("Não") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun deletarPedido()
    {
        val result = databaseHelperPedido.deletarPedido(pedidoID)

        if(result > 0)
        {
            Toast.makeText(this, "Pedido deletado com sucesso!", Toast.LENGTH_SHORT).show()

            finish()
        }
        else
        {
            Toast.makeText(this, "Erro ao deletar o pedido.", Toast.LENGTH_SHORT).show()
        }
    }
}