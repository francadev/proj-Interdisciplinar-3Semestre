/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetolp3.floralize.model;

/**
 *
 * @author r0039435
 */
public class Fornecedores {
    
    private String nome_fornecedor;
    private int qtd;
    private int unidades;

    public Fornecedores(String nome_fornecedor, int qtd, int unidades) {
        this.nome_fornecedor = nome_fornecedor;
        this.qtd = qtd;
        this.unidades = unidades;
    }

    public String getNome_fornecedor() {
        return nome_fornecedor;
    }

    public void setNome_fornecedor(String nome_fornecedor) {
        this.nome_fornecedor = nome_fornecedor;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
    
    
    
}
