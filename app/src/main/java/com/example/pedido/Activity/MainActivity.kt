    package com.example.pedido.Activity

    import android.content.Intent
    import android.os.Bundle
    import android.widget.ArrayAdapter
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ListView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.example.pedido.DatabaseHelper.DatabaseHelperPedido
    import com.example.pedido.Model.Pedido
    import com.example.pedido.R

    class MainActivity : AppCompatActivity()
    {
        private lateinit var databaseHelperPedido: DatabaseHelperPedido
        private lateinit var etNome: EditText
        private lateinit var etPreco: EditText
        private lateinit var etQuantidade: EditText
        private lateinit var etPrecoTotal: EditText
        private lateinit var btnAdicionar: Button
        private lateinit var listViewPedido: ListView
        private lateinit var adapter: ArrayAdapter<String>
        private var listaPedido = ArrayList<String>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.main_activity)

            //inicializa os componentes
            databaseHelperPedido = DatabaseHelperPedido(this)
            etNome = findViewById(R.id.etNome)
            etPreco = findViewById(R.id.etPreco)
            etQuantidade = findViewById(R.id.etQuantidade)
            etPrecoTotal = findViewById(R.id.etPrecoTotal)
            btnAdicionar = findViewById(R.id.btnAdicionar)
            listViewPedido = findViewById(R.id.listViewPedido)

            //configura o botão para adicionar pedidos
            btnAdicionar.setOnClickListener {
                adicionarPedido()
            }

            carregarPedidos()

            listViewPedido.setOnItemClickListener { parent, view, position, id ->
                val pedidoSelecionado = databaseHelperPedido.obterPedido()[position]

                val intent = Intent(this, EditPedidoActivity::class.java)
                intent.putExtra("PEDIDO_ID", pedidoSelecionado.id)
                startActivity(intent)
            }
        }

        override fun onResume() {
            super.onResume()

            //Recarregar clientes quando a atividade é retomada
            carregarPedidos()
        }

        private fun adicionarPedido() {
            val nome = etNome.text.toString()
            val preco = etPreco.text.toString().toFloatOrNull() ?: return // Conversão para Float
            val quantidade = etQuantidade.text.toString().toIntOrNull() ?: return // Conversão para Int
            val precoTotal = etPrecoTotal.text.toString().toFloatOrNull() ?: return // Conversão para Float

            if (nome.isNotEmpty() && preco != null && quantidade != null && precoTotal != null) {
                val pedido = Pedido(nome = nome, preco = preco, quantidade = quantidade, precoTotal = precoTotal)
                databaseHelperPedido.adicionarPedido(pedido) // Adiciona o pedido ao banco de dados
                etNome.text.clear()
                etPreco.text.clear()
                etQuantidade.text.clear()
                etPrecoTotal.text.clear()

                carregarPedidos()
            }
            else
            {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        private fun carregarPedidos()
        {
            listaPedido.clear()

            val pedidos = databaseHelperPedido.obterPedido()

            for(pedido in pedidos)
            {
                listaPedido.add("ID: ${pedido.id}, Nome: ${pedido.nome}, Preco: ${pedido.preco}, Quantidade: ${pedido.quantidade}, Preco Total: ${pedido.precoTotal}")
            }

            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaPedido)

            listViewPedido.adapter = adapter;
        }

    }