/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import projetolp3.floralize.model.AppState;
import projetolp3.floralize.model.ItemCarrinho;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class HomeController implements Initializable {

    @FXML
    private Label lblHome;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        AppState appState = AppState.getInstance();
        Set<ItemCarrinho> carrinho = appState.getCarrinho();
    }        
}
