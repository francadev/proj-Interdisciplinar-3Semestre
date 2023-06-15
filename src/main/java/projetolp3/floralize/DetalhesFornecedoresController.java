/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import projetolp3.floralize.bd.ConexaoMySQL;
import projetolp3.floralize.model.Produtos;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class DetalhesFornecedoresController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblNomeFornecedor;

    @FXML
    private Label lblQtd;

    @FXML
    private Label lblUnidades;
    
    @FXML
    private TableView<Produtos> tbvProdutosFornecedores;
    
    @FXML
    private TableColumn<Produtos, String> tbcNomeProduto;
    
    @FXML
    private TableColumn<Produtos, Integer> tbcQtd;
    
    private String nome_do_fornecedor;
            
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<Produtos> listaProdutos = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void loadProdutosFornecedores() throws SQLException {
        System.out.println("loadProdutosFornecedores: "+nome_do_fornecedor);
        
        connection = new ConexaoMySQL().getConnection();
        query = "SELECT nome_produto, qtd FROM `view_produtos_descricao` WHERE nome_fornecedor = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nome_do_fornecedor); 
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            listaProdutos.add(new Produtos(
                resultSet.getString("nome_produto"),
                resultSet.getInt("qtd")));
        }

        tbvProdutosFornecedores.setItems(listaProdutos);
        
        tbcNomeProduto.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
        tbcQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
    }

    public void setTextField(String nome_fornecedor, int qtd, int unidades) {
        lblNomeFornecedor.setText(nome_fornecedor);
        
        System.out.println("setTextField: "+lblNomeFornecedor.getText());
        this.nome_do_fornecedor = nome_fornecedor;
        String qtdString = "" + qtd;
        lblQtd.setText(qtdString);
        String unidadesString = "" + unidades;
        lblUnidades.setText(unidadesString);
        
        try {
            loadProdutosFornecedores();
        } catch (SQLException ex) {
            Logger.getLogger(DetalhesFornecedoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
