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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projetolp3.floralize.bd.ConexaoMySQL;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class AlterarProdutoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPreco;

    @FXML
    private TextField tfQuantidade;
    
    @FXML
    private TextField tfDescricao;
    
    private ProdutosFornecedorController produtosFornecedorController;
    
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement;
    private boolean update;
    int produtoId;
    int fornecedorId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void salvar() {
        String nome = tfNome.getText();
        String preco = tfPreco.getText();
        String qtd = tfQuantidade.getText();
        String descricao = tfDescricao.getText();

        if (nome.isEmpty() || preco.isEmpty() || qtd.isEmpty() || descricao.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os dados!");
            alert.showAndWait();

        } else {
            inserirProduto();
            limpar();
        }
    }

    @FXML
    private void limpar() {
        tfNome.setText(null);
        tfPreco.setText(null);
        tfDescricao.setText(null);  
        tfQuantidade.setText(null);  
    }

    private void inserirProduto() {
        fornecedorId = LoginController.fornecedorLogado.getId();
        query = "INSERT INTO `produtos`(`fornecedor_id`, `nome`, `preco`, `descricao`, `quantidade`) VALUES (?,?,?,?,?)";
        System.out.println("Entrou no insert");
        try {
            connection = new ConexaoMySQL().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(fornecedorId));
            preparedStatement.setString(2, tfNome.getText());
            preparedStatement.setString(3, tfPreco.getText());
            preparedStatement.setString(4, tfDescricao.getText());
            preparedStatement.setString(5, tfQuantidade.getText());

            // Verificar se já existe um produto com o mesmo nome
            String checkQuery = "SELECT COUNT(*) FROM produtos WHERE nome = ? and fornecedor_id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, tfNome.getText());
            checkStatement.setString(2, String.valueOf(fornecedorId));
            ResultSet checkResult = checkStatement.executeQuery();
            if (checkResult.next()) {
                int count = checkResult.getInt(1);
                if (count > 0) {
                    // Já existe um produto com o mesmo nome, exibir alerta
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Produto Duplicado");
                    alert.setHeaderText(null);
                    alert.setContentText("Já existe um produto com o mesmo nome.");
                    alert.showAndWait();
                    return; 
                }
            }
            // Inserir o produto
            preparedStatement.execute();

            Stage stage = (Stage) tfNome.getScene().getWindow();
            stage.close();

        } catch (SQLException ex) {
            Logger.getLogger(AlterarProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void setTextField(int id, String name, double preco, int qtd) {
        produtoId = id;
        tfNome.setText(name);
        tfPreco.setText(String.valueOf(preco));
        tfDescricao.setText(String.valueOf(preco));
        tfQuantidade.setText(String.valueOf(qtd));
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

    public void setProdutosFornecedorController(ProdutosFornecedorController controller) {
        this.produtosFornecedorController = controller;
    
    }
}
