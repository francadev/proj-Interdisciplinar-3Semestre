/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetolp3.floralize.model;

/**
 *
 * @author r0039435
 */
public class Pedidos {

    private String nome_produto;
    private String nome_cliente;
    private int quantidade;

    public Pedidos(String nome_cliente, String nome_produto, int quantidade) {
        this.nome_cliente = nome_cliente;
        this.nome_produto = nome_produto;
        this.quantidade = quantidade;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
