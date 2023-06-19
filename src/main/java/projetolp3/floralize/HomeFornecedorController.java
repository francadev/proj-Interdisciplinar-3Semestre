/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

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
    
    /*
    private LoginController loginController;

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }*/
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //int fornecedorId = loginController.getLoggedInFornecedorId();
        String mensagem = lblMensagem.getText(); // Obtém o texto existente na Label
        String novaMensagem = mensagem + String.valueOf(LoginController.fornecedorLogado.getNome_fornecedor()); // Acrescenta o novo texto

        lblMensagem.setText(novaMensagem);  
    }
}