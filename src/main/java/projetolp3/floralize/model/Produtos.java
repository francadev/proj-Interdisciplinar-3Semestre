/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetolp3.floralize.model;

/**
 *
 * @author r0039435
 */
public class Produtos {
    
    private String nome_produto;
    private String nome_fornecedor;
    private double preco;
    private int qtd;

    public Produtos(String nome_produto, String nome_fornecedor, int qtd,  double preco) {
        this.nome_produto = nome_produto;
        this.nome_fornecedor = nome_fornecedor;
        this.preco = preco;
        this.qtd = qtd;
    }

    public String getNome_produto() {
        return nome_produto;
    }

    public void setNome_produto(String nome_produto) {
        this.nome_produto = nome_produto;
    }

    public String getNome_fornecedor() {
        return nome_fornecedor;
    }

    public void setNome_fornecedor(String nome_fornecedor) {
        this.nome_fornecedor = nome_fornecedor;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}


