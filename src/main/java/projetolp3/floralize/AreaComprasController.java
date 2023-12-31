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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author r0039435
 */


public class AreaComprasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button btnHome;
    
    @FXML
    private Button btnProdutos;
    
    @FXML
    private Button btnFornecedores;

    @FXML
    private Button btnForum;
    
    @FXML
    private Button btnVoltar;
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private StackPane stackPane;    
    
    @FXML
    private Pane paneHome;
    
    @FXML
    private Pane paneProdutos;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {  
            telaHome();
            btnHome.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //Definir como pane principal (visível para o usuário)
        paneHome.toFront();
    }
    
    @FXML
    public void clickButton(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnHome) {
            telaHome(); 
            disableStyle();
            btnHome.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        }
        
        else if (actionEvent.getSource() == btnProdutos) {
            telaProdutos(); 
            disableStyle();
            btnProdutos.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        }
        else if (actionEvent.getSource() == btnFornecedores) {
            telaFornecedores(); 
            disableStyle();
            btnFornecedores.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        }
        else if (actionEvent.getSource() == btnForum) {
            telaForum(); 
            disableStyle();
            btnForum.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        }
    }
    
    public void voltarTelaInicial() throws IOException {
        //obter palco para trocar de cena (tela)
        Stage stage = (Stage) rootPane.getScene().getWindow();

        //Carregar a nova cena
        FXMLLoader fxmlLoader = new FXMLLoader(
        App.class.getResource("TelaInicial.fxml"));
        Scene telaInicial = new Scene(fxmlLoader.load());

        //Exibir a nova cena
        stage.setScene(telaInicial);
        stage.setTitle("Tela Inicial");
        stage.show();
    }
    @FXML
    private void telaHome() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("Home.fxml"));  
        stackPane.getChildren().setAll(pane);
    }
    
    @FXML
    private void telaProdutos() throws IOException{
        Pane pane = FXMLLoader.load(App.class.getResource("Produtos.fxml"));  
        stackPane.getChildren().setAll(pane);
        paneProdutos.toFront();
        
    }

    @FXML
    private void telaFornecedores() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("Fornecedores.fxml"));  
        stackPane.getChildren().setAll(pane);
    }
    
    @FXML
    private void telaForum() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("Forum.fxml"));  
        stackPane.getChildren().setAll(pane);
    }
    
    private void disableStyle() { 
        btnFornecedores.setStyle("-fx-background-color: #FFFFFF;");
        btnForum.setStyle("-fx-background-color: #FFFFFF;");
        btnHome.setStyle("-fx-background-color: #FFFFFF;");
        btnProdutos.setStyle("-fx-background-color: #FFFFFF;");
    }
}