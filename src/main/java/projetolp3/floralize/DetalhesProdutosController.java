/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import projetolp3.floralize.model.Produtos;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class DetalhesProdutosController implements Initializable {

    @FXML
    private Label lblDescricao;

    @FXML
    private Label lblFornecedor;

    @FXML
    private Label lblNome;

    @FXML
    private Label lblPreco;

    @FXML
    private Label lblQuantidade;
    
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Produtos produtos = null;
    private boolean update;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    void setUpdate(boolean b) {
        this.update = b;

    }
    
    void setTextField(String nome_produto, String nome_fornecedor, int qtd,  double preco, String descricao) {

        lblNome.setText(nome_produto);
        lblFornecedor.setText(nome_fornecedor);
        
        String qtdString = "" + qtd;
        lblQuantidade.setText(qtdString);
        
        String precoString = "" + preco;
        lblPreco.setText(precoString);
        lblDescricao.setText(descricao);
    } 
    
}
