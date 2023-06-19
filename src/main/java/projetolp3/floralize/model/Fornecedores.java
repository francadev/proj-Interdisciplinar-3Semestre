package projetolp3.floralize.model;

/**
 *
 * @author r0039435
 */


public class Fornecedores {
    private String nome_fornecedor;
    private int qtd;
    private int unidades;
    private String cpf;
    private String area;
    private int id;
    
    public Fornecedores() {
    }

    public Fornecedores(String nome_fornecedor, int qtd, int unidades) {
        this.nome_fornecedor = nome_fornecedor;
        this.qtd = qtd;
        this.unidades = unidades;
    }

    public Fornecedores(int id, String nome_fornecedor, String cpf, String area) {
        this.nome_fornecedor = nome_fornecedor;
        this.cpf = cpf;
        this.area = area;
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}