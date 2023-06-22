/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class PopUpConfirmacaoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label lblNumPedido;

    @FXML
    private Label lblOndeRetirar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void fecharPopUp() throws IOException{
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    
    public void setTextField(int numPedido, String locaisRetirada){
        lblNumPedido.setText(String.valueOf(numPedido));
        lblOndeRetirar.setText(locaisRetirada);
    }
}
