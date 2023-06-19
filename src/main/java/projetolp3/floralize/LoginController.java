/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import projetolp3.floralize.bd.ConexaoMySQL;
import projetolp3.floralize.model.Fornecedores;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnEntrar;

    @FXML
    private Label lblMensagem;

    @FXML
    private PasswordField tFSenha;

    @FXML
    private TextField tfUsuario;
    
    String query = null;
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    
    static Fornecedores fornecedorLogado = new Fornecedores();

    @FXML
    void fazerLogin(ActionEvent event) throws IOException {
        String login = tfUsuario.getText();
        String senha = tFSenha.getText();

        // Assuming you have a database connection and the necessary imports

        try {
            if (login.equals("admin") && senha.equals("admin")) {
                Stage stage = (Stage) btnEntrar.getScene().getWindow();

                //Carregar a nova cena
                FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("AreaAdministrador.fxml"));
                Scene fornecedor = new Scene(fxmlLoader.load());

                //Exibir a nova cena
                stage.setScene(fornecedor);
                stage.setTitle("ÁREA DO ADMINISTRADOR");
                stage.show();
            }else{
                connection = new ConexaoMySQL().getConnection();
                query = "SELECT * FROM Fornecedores WHERE login = ? AND senha = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, login);
                statement.setString(2, senha);

                // Execute the query
                resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Login successful
                    lblMensagem.setText("Login feito com sucesso!");

                    fornecedorLogado.setNome_fornecedor(resultSet.getString("nome"));
                    fornecedorLogado.setId(resultSet.getInt("id"));

                    Stage stage = (Stage) btnEntrar.getScene().getWindow();

                    //Carregar a nova cena
                    FXMLLoader fxmlLoader = new FXMLLoader(
                    App.class.getResource("AreaFornecedor.fxml"));
                    Scene fornecedor = new Scene(fxmlLoader.load());

                    //Exibir a nova cena
                    stage.setScene(fornecedor);
                    stage.setTitle("ÁREA DO FORNECEDOR");
                    stage.show();

                } else {
                    // Login failed
                    lblMensagem.setText("Usuário ou senha incorretos");
                }

                // Close the ResultSet, PreparedStatement, and Connection
                resultSet.close();
                statement.close();
                connection.close();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void voltarTelaInicial() throws IOException {
        Stage stage = (Stage) btnEntrar.getScene().getWindow();
        //Carregar a nova cena
        FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("TelaInicial.fxml"));
        Scene fornecedor = new Scene(fxmlLoader.load());

        //Exibir a nova cena
        stage.setScene(fornecedor);
        stage.setTitle("TELA INICIAL");
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
