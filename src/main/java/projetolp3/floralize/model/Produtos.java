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
    private String descricao;
    private double preco;
    private int qtd;
    private int quantidadeAdicionada;
    private int id_produto;
    private int id_fornecedor;
    
    /*public Produtos(int id_produto, int id_fornecedor, double preco, int qtd) {
        this.id_produto = id_produto;
        this.id_fornecedor = id_fornecedor;
        this.preco = preco;
        this.qtd = qtd;
    }*/
    
    public Produtos(String nome_produto, int qtd) {
        this.nome_produto = nome_produto;
        this.qtd = qtd;
    }
    
    public Produtos(String nome_produto, double preco, int qtd) {
        this.nome_produto = nome_produto;
        this.preco = preco;
        this.qtd = qtd;
    }
    
    public Produtos(String nome_produto, int id_produto, String nome_fornecedor, int id_fornecedor, int qtd,  double preco, String descricao) {
        this.nome_produto = nome_produto;
        this.nome_fornecedor = nome_fornecedor;
        this.preco = preco;
        this.qtd = qtd;
        this.descricao = descricao;
        this.id_produto = id_produto;
        this.id_fornecedor = id_fornecedor;
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
    
        public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public void decrementarQuantidade(int quantidade) {
        this.qtd -= quantidade;
    }
    //--
    
    public void setQuantidadeAdicionada(int quantidadeAdicionada) {
        this.quantidadeAdicionada = quantidadeAdicionada;
    }

    public int getQuantidadeAdicionada() {
        return quantidadeAdicionada;
    }

    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public int getId_fornecedor() {
        return id_fornecedor;
    }

    public void setId_fornecedor(int id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }
    
    
}



