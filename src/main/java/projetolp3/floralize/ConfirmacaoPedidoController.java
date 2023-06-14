/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import projetolp3.floralize.model.AppState;
import projetolp3.floralize.model.ItemCarrinho;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class ConfirmacaoPedidoController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label lblTotal;

    @FXML
    private TableColumn<ItemCarrinho, String> tbcBtnAdicional;

    @FXML
    private TableColumn<ItemCarrinho, String> tbcFornecedor;

    @FXML
    private TableColumn<ItemCarrinho, String> tbcNome;

    @FXML
    private TableColumn<ItemCarrinho, String> tbcPreco;

    @FXML
    private TableColumn<ItemCarrinho, String> tbcQuantidade;

    @FXML
    private TableColumn<ItemCarrinho, String> tbcTotal;

    @FXML
    private TableView<ItemCarrinho> tbvCompra;
    
    AppState appState = AppState.getInstance();
    Set<ItemCarrinho> carrinho = appState.getCarrinho();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        refreshTableCarrinho();
    }    
    
    public void refreshTableCarrinho() {
        tbvCompra.getItems().clear();
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        tbcFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        //tbcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Define a fábrica de valores para a coluna tbcTotal
        tbcTotal.setCellValueFactory(cellData -> {
            ItemCarrinho item = cellData.getValue();
            double total = item.getPreco() * item.getQuantidade();
            return new SimpleStringProperty(String.valueOf(total));
        });

        tbcBtnAdicional.setCellFactory(col -> {
            TableCell<ItemCarrinho, String> cell = new TableCell<>() {
                final Button deleteButton = new Button ("Excluir");
                
                {
                    deleteButton.setOnAction(event -> {
                        ItemCarrinho item = getTableView().getItems().get(getIndex());
                        carrinho.remove(item);
                        refreshTableCarrinho();
                    });
                    
                    deleteButton.setId("deleteButton");
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });


        double total = 0.0;
        for (ItemCarrinho items : carrinho) {
            total += items.getPreco() * items.getQuantidade();
        }
        lblTotal.setText(String.valueOf(total));

        tbvCompra.getItems().addAll(carrinho);
    }

    
    public void voltarProdutos() throws IOException{
        Pane pane = FXMLLoader.load(App.class.getResource("Produtos.fxml"));  
        anchorPane.getChildren().setAll(pane);
    }
}
