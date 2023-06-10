/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/*se usado consulta no banco*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import projetolp3.floralize.bd.ConexaoMySQL;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import projetolp3.floralize.model.Produtos;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class ProdutosController implements Initializable {
    @FXML
    private TableView<Produtos> tbvProdutos;
    
    @FXML
    private TableColumn<Produtos, String> tbcNome;
    
    @FXML
    private TableColumn<Produtos, String> tbcFornecedor;

    @FXML
    private TableColumn<Produtos, String> tbcPreco;

    @FXML
    private TableColumn<Produtos, String> tbcQuantidade;
    
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Produtos produtos = null;
    
    ObservableList<Produtos>  ListaProdutos = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Connection conn = new ConexaoMySQL().getConnection();

        //Prepara a consulta
        String sql = "SELECT COUNT(*) FROM fornecedores";

        try (PreparedStatement statement = conn.prepareStatement(sql)){
            // Executa a consulta e obtém o resultado
            ResultSet resultSet = statement.executeQuery();

            // Verifica se há resultados e armazena o valor em uma variável
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Use o valor armazenado na variável
            System.out.println("Número de fornecedores: " + count);
            
        } catch (SQLException e) {
            e.printStackTrace();
        
        
        
        }    */       
        
        loadDataProdutos();
    }    
    
    public void loadDataProdutos(){
        
        connection = new ConexaoMySQL().getConnection();
        refreshTable();
        
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
        tbcFornecedor.setCellValueFactory(new PropertyValueFactory<>("nome_fornecedor"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }
    
    public void refreshTable(){
        try {
            ListaProdutos.clear();
            
            query = "SELECT * FROM `view_produtos`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                ListaProdutos.add(new Produtos(
                    resultSet.getString("nome_produto"),
                    resultSet.getString("nome_fornecedor"),
                    resultSet.getInt("qtd"),
                    resultSet.getDouble("preco")));
                tbvProdutos.setItems(ListaProdutos);      
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
