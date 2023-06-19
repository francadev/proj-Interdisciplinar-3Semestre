/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class TelaInicialController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button btnAreaCompras;

    @FXML
    private Button btnAreaFornecedor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    public void areaCompras(ActionEvent event) throws IOException {
            Stage stage = (Stage) btnAreaCompras.getScene().getWindow();

            //Carregar a nova cena
            FXMLLoader fxmlLoader = new FXMLLoader(
         App.class.getResource("AreaCompras.fxml"));
            Scene compras = new Scene(fxmlLoader.load());

            //Exibir a nova cena
            stage.setScene(compras);
            stage.setTitle("ÁREA DE COMPRAS");
            stage.show();
    }
    
    @FXML
    public void areaFornecedor(ActionEvent event) throws IOException{
        
        Stage stage = (Stage) btnAreaFornecedor.getScene().getWindow();

        //Carregar a nova cena
        FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("Login.fxml"));
        Scene login = new Scene(fxmlLoader.load());

        //Exibir a nova cena
        stage.setScene(login);
        stage.setTitle("Fazer login");
        stage.show();
    }
    
    @FXML
    public void areaAdministrador(ActionEvent event) throws IOException{
        Stage stage = (Stage) btnAreaFornecedor.getScene().getWindow();

        //Carregar a nova cena
        FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("Login.fxml"));
        Scene login = new Scene(fxmlLoader.load());

        //Exibir a nova cena
        stage.setScene(login);
        stage.setTitle("Fazer login");
        stage.show();
    }
}
