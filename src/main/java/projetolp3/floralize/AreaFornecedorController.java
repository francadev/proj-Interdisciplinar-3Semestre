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
public class AreaFornecedorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button btnDados;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnProdutos;
    
    @FXML
    private Button btnPedidos;

    @FXML
    private Button btnVoltar;

    @FXML
    private Pane paneHome;

    @FXML
    private Pane paneProdutos;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private StackPane stackPane;

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
        
        else if (actionEvent.getSource() == btnPedidos) {
            telaPedidos(); 
            disableStyle();
            btnPedidos.setStyle("-fx-background-color: linear-gradient(to bottom, #f3f3f3, #ebeceb)");
        }
        
    }
    
    
    //abre os panes
    @FXML
    private void telaHome() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("HomeFornecedor.fxml"));  
        stackPane.getChildren().setAll(pane);
    }
    @FXML
    private void telaProdutos() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("ProdutosFornecedor.fxml"));  
        stackPane.getChildren().setAll(pane);
    }
    
    @FXML
    private void telaPedidos() throws IOException{     
        Pane pane = FXMLLoader.load(App.class.getResource("PedidosFornecedor.fxml"));  
        stackPane.getChildren().setAll(pane);
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
    
    private void disableStyle() {        
        btnDados.setStyle("-fx-background-color: #FFFFFF;");
        btnHome.setStyle("-fx-background-color: #FFFFFF;");
        btnProdutos.setStyle("-fx-background-color: #FFFFFF;");
        btnPedidos.setStyle("-fx-background-color: #FFFFFF;");
    }
    
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
    
}
