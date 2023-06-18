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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    private Button btnLimpar;

    @FXML
    private Button btnSalvar;

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPreco;

    @FXML
    private TextField tfQuantidade;
    
    @FXML
    private TextField tfDescricao;
    
    String query = null;
    Connection connection = null;
    ResultSet resultSet = null;
    PreparedStatement preparedStatement;
    private boolean update;
    int produtoId;
    int fornecedorId = 1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void save(MouseEvent event) {
        connection = new ConexaoMySQL().getConnection();
        
        String nome = tfNome.getText();
        String preco = tfPreco.getText();
        String qtd = tfQuantidade.getText();
        String descricao = tfDescricao.getText();

        if (nome.isEmpty() || preco.isEmpty() || qtd.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();
        }

    }

    @FXML
    private void clean() {
        tfNome.setText(null);
        tfPreco.setText(null);
        tfDescricao.setText(null);  
        tfQuantidade.setText(null);  
    }
    
    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO `produtos`(`fornecedor_id`,`nome`, `preco`, `descricao`,`quantidade`) VALUES (?,?,?,?,?)";
        }else{
            query = "UPDATE `produtos` SET "
                    + "`nome`=?,"
                    + "`preco`=?,"
                    + "`descricao`=?,"
                    + "`quantidade`= ? WHERE id = '"+produtoId+"'";
        }
    }

    private void insert() {

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, fornecedorId);
            preparedStatement.setString(2, tfNome.getText());
            preparedStatement.setString(3, tfPreco.getText());
            preparedStatement.setString(4, tfDescricao.getText());
            preparedStatement.setString(5, tfQuantidade.getText());
            preparedStatement.execute();

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

}
