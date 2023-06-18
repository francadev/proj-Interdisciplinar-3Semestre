package projetolp3.floralize;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import projetolp3.floralize.bd.ConexaoMySQL;

public class ForumController implements Initializable {

    @FXML
    private TitledPane p1;

    @FXML
    private TitledPane p2;
    
    @FXML
    private TitledPane p3;

    @FXML
    private Label lblP1;

    @FXML
    private Label lblP2;
    
    @FXML
    private Label lblP3;
    
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Estabeleça a conexão com o banco de dados
        try {
            connection = new ConexaoMySQL().getConnection();

            // Execute a consulta
            query = "SELECT pergunta, resposta FROM forum";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            // Recupere os valores retornados da consulta
            if (resultSet.next()) {
                String pergunta1 = resultSet.getString("pergunta");
                String resposta1 = resultSet.getString("resposta");
                p1.setText(pergunta1);
                lblP1.setText(resposta1);
            }

            if (resultSet.next()) {
                String pergunta2 = resultSet.getString("pergunta");
                String resposta2 = resultSet.getString("resposta");
                p2.setText(pergunta2);
                lblP2.setText(resposta2);
            }
            
            if (resultSet.next()) {
                String pergunta3 = resultSet.getString("pergunta");
                String resposta3 = resultSet.getString("resposta");
                p3.setText(pergunta3);
                lblP3.setText(resposta3);
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
