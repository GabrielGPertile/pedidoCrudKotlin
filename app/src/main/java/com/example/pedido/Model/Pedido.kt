package com.example.pedido.Model

data class Pedido
(
    val id: Int = 0,
    val nome: String,
    val preco: Float = 0.0F,
    val quantidade: Int = 0,
    val precoTotal: Float = 0.0f
)
