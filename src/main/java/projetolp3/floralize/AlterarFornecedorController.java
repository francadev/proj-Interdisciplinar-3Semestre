package projetolp3.floralize;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import projetolp3.floralize.bd.ConexaoMySQL;

/**
 * FXML Controller class
 *
 * @author lhcue
 */
public class AlterarFornecedorController implements Initializable {

    @FXML
    private Button btcadastro;

    @FXML
    private TextField tx_nome;

    @FXML
    private TextField tx_login;

    @FXML
    private PasswordField tx_senha;

    @FXML
    private TextField tx_area;

    @FXML
    private TextField tx_cpfcnpj;

    @FXML
    private Label lb_mensagem;

    Connection connection = null;
    PreparedStatement statement = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Definir evento para formatar o CPF enquanto é digitado
        tx_cpfcnpj.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > oldValue.length()) {
                String text = tx_cpfcnpj.getText();
                if (text.length() == 3 || text.length() == 7) {
                    tx_cpfcnpj.setText(text + ".");
                } else if (text.length() == 11) {
                    tx_cpfcnpj.setText(text + "-");
                } else if (text.length() > 14) {
                    tx_cpfcnpj.setText(oldValue);
                }
            }
        });

    }

    public void cadastrarFornecedor(ActionEvent event) {
        try {
            connection = new ConexaoMySQL().getConnection();
            String sql = "INSERT INTO Fornecedores (login, senha, nome, area,cpf_cnpj) VALUES (?, ?, ?, ?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, tx_nome.getText());
            statement.setString(2, tx_login.getText());
            statement.setString(3, tx_senha.getText());
            statement.setString(4, tx_area.getText());
            statement.setString(5, tx_cpfcnpj.getText());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                lb_mensagem.setText("Fornecedor cadastrado com sucesso!");
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                lb_mensagem.setText("Fornecedor já cadastrado.");
            } else {
                lb_mensagem.setText("Erro ao cadastrar fornecedor, tente novamente");
            }
        }
    }
    
    
    
    public void limpar(ActionEvent event) {
        tx_nome.clear();
        tx_login.clear();
        tx_senha.clear();
        tx_area.clear();
        tx_cpfcnpj.clear();
        lb_mensagem.setText("");
    
    }

}