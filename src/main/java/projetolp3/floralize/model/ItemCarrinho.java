/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetolp3.floralize.model;

import java.util.Objects;

/**
 *
 * @author r0039435
 */
public class ItemCarrinho {
    private String nomeProduto;
    private String fornecedor;
    private double preco;
    private int quantidade;
    private int id_produto;
    private int id_fornecedor;

    public ItemCarrinho(String nomeProduto, String fornecedor, double preco, int quantidade, int id_produto, int id_fornecedor) {
        this.nomeProduto = nomeProduto;
        this.fornecedor = fornecedor;
        this.preco = preco;
        this.quantidade = quantidade;
        this.id_produto = id_produto;
        this.id_fornecedor = id_fornecedor;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ItemCarrinho other = (ItemCarrinho) obj;
        return Objects.equals(nomeProduto, other.nomeProduto) && Objects.equals(fornecedor, other.fornecedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeProduto, fornecedor);
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
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
