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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import projetolp3.floralize.bd.ConexaoMySQL;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class HomeFornecedorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label lblMensagem;
    
    @FXML
    private Label totalVendas;
    
    ConexaoMySQL conexao = new ConexaoMySQL();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Obtém o texto existente na Label
        String mensagem = lblMensagem.getText(); 
        // Acrescenta o nome do fornecesor
        String novaMensagem = mensagem + String.valueOf(LoginController.fornecedorLogado.getNome_fornecedor()); 

        lblMensagem.setText(novaMensagem);  
        
        double valorTotalVendas = getTotalVendas();
        totalVendas.setText(Double.toString(valorTotalVendas));
    }
    
    public double getTotalVendas() {
        // Declaração inicial da variável
        double tVendas = 0.0; 

        try (Connection connection = conexao.getConnection()) {
            String query = "SELECT totalVendas(?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, LoginController.fornecedorLogado.getId());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        tVendas = resultSet.getDouble(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Ocorreu um erro ao chamar a função totalVendas: " + e.getMessage());
        }
        // Retorna o valor da variável
        return tVendas; 
    }
}