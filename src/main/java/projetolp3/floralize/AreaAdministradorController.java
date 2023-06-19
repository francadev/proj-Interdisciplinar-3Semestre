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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class AreaAdministradorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button btnFornecedor;

    @FXML
    private Button btnVoltar;

    @FXML
    private StackPane stackPane;
    
    @FXML
    private Pane paneHome;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {  
            telaFornecedor();
            btnFornecedor.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Definir como pane principal (visível para o usuário)
        paneHome.toFront();
    }    
    
    @FXML
    public void clickButton(ActionEvent actionEvent) throws IOException {
        
        if (actionEvent.getSource() == btnFornecedor) {
            System.out.println("Entrou");
            telaFornecedor(); 
            disableStyle();
            btnFornecedor.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        }        
    }
    
    @FXML
    private void telaFornecedor() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("FornecedoresAdministrador.fxml"));  
        stackPane.getChildren().setAll(pane);
    }
    
    private void disableStyle() {        
        btnFornecedor.setStyle("-fx-background-color: #FFFFFF;");
    }
    
    public void voltarTelaInicial() throws IOException {
        //obter palco para trocar de cena (tela)
        Stage stage = (Stage) btnVoltar.getScene().getWindow();

        //Carregar a nova cena
        FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("TelaInicial.fxml"));
        Scene telaInicial = new Scene(fxmlLoader.load());

        //Exibir a nova cena
        stage.setScene(telaInicial);
        stage.setTitle("Tela Inicial");
        stage.show();
    }
    
}
