/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import projetolp3.floralize.bd.ConexaoMySQL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import projetolp3.floralize.model.Produtos;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class ProdutosFornecedorController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView<Produtos> tbvProdutos;

    @FXML
    private TableColumn<Produtos, String> tbcNome;

    @FXML
    private TableColumn<Produtos, String> tbcPreco;

    @FXML
    private TableColumn<Produtos, String> tbcQuantidade;

    @FXML
    private TableColumn<Produtos, String> tbcDetalhes;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<Produtos>  ListaProdutos = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDataProdutos();
    }

    public void loadDataProdutos() {

        connection = new ConexaoMySQL().getConnection();
        ListaProdutos.clear();
        refreshTable();

        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("qtd"));
    }

    public void adicionarProduto(){
        
       /*FXMLLoader fxmlLoader = new FXMLLoader(
    App.class.getResource("AlterarProduto.fxml"));

       try {
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       
       FXMLLoader fxmlLoader = new FXMLLoader(
    App.class.getResource("AlterarProduto.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
        }

        AlterarProdutoController alterarProdutoController = fxmlLoader.getController();
        alterarProdutoController.setUpdate(true);
        //produtosFornecedorController.setTextField(p.getNome_produto(), p.getNome_fornecedor(), p.getQtd(), p.getPreco(), p.getDescricao());
        Parent parent = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public void refreshTable() {
        try {
            ListaProdutos.clear();

            query = "SELECT * FROM view_produtos WHERE nome_fornecedor = 'Floricultura Flores Belas'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Produtos produto = new Produtos(
                    resultSet.getString("nome_produto"),
                    resultSet.getDouble("preco"),
                    resultSet.getInt("qtd")   
                );
                ListaProdutos.add(produto);
            }
            tbvProdutos.setItems(ListaProdutos);

            Callback<TableColumn<Produtos, String>, TableCell<Produtos, String>> cellFactory = (TableColumn<Produtos, String> param) -> {
                final TableCell<Produtos, String> cell = new TableCell<Produtos, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {

                            final Button editButton = new Button("Editar");
                            final Button deleteButton = new Button("Deletar");
                            editButton.setId("editButton");
                            deleteButton.setId("deleteButton");
                            Produtos p = getTableView().getItems().get(getIndex());
                            editButton.setOnAction(event -> {
                                
                            });
                            deleteButton.setOnAction(event -> {
                                try {
                                    String deleteQuery = "DELETE FROM produtos WHERE fornecedor_id = 1 AND nome = ?";
                                    preparedStatement = connection.prepareStatement(deleteQuery);
                                    //preparedStatement.setInt(1, p.getId_fornecedor());
                                    preparedStatement.setString(1, p.getNome_produto());

                                    // Execute the delete query
                                    int rowsAffected = preparedStatement.executeUpdate();

                                    if (rowsAffected > 0) {
                                        System.out.println("Product deleted successfully.");
                                        // Refresh the table to reflect the changes
                                        refreshTable();
                                    } else {
                                        System.out.println("Failed to delete product.");
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                            });
                            
                            editButton.setOnAction(event -> {
                                
                            });

                            HBox managebtn = new HBox(editButton, deleteButton);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(editButton, new Insets(2, 2, 0, 3));
                            HBox.setMargin(deleteButton, new Insets(2, 3, 0, 2));

                            setGraphic(managebtn);
                            setText(null);
                        }
                    }
                };
                return cell;
            };
            tbcDetalhes.setCellFactory(cellFactory);
        } catch (SQLException ex) {
            Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
