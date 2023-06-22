/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import projetolp3.floralize.bd.ConexaoMySQL;
import projetolp3.floralize.model.Fornecedores;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class FornecedoresController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TableColumn<Fornecedores, String> tbcDetalhes;

    @FXML
    private TableColumn<Fornecedores, String> tbcNome;

    @FXML
    private TableColumn<Fornecedores, String> tbcQuantidade;

    @FXML
    private TableColumn<Fornecedores, String> tbcUnidades;

    @FXML
    private TableView<Fornecedores> tbvFornecedores;
    
    @FXML
    private TextField tfPesquisar;
    
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    ObservableList<Fornecedores> ListaFornecedores = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDataFornecedores();
        tfPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            realizarPesquisa(newValue);
        });
    }
    
    private void realizarPesquisa(String termo) {
        List<Fornecedores> resultados = new ArrayList<>();

        for (Fornecedores fornecedores : ListaFornecedores) {
            if (
                fornecedores.getNome_fornecedor().contains(termo)
                ) {
                resultados.add(fornecedores);
            }
        }

        // Atualiza a TableView com os resultados da pesquisa
        ObservableList<Fornecedores> observableResultados = FXCollections.observableArrayList(resultados);
        tbvFornecedores.setItems(observableResultados);
    }
    
    public void loadDataFornecedores(){
        
        connection = new ConexaoMySQL().getConnection();
        refreshTable();
        
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_fornecedor"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        tbcUnidades.setCellValueFactory(new PropertyValueFactory<>("unidades"));
    }
    
    public void refreshTable(){
        try {
            ListaFornecedores.clear();
            
            query = "SELECT * FROM `view_fornecedores`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                ListaFornecedores.add(new Fornecedores(
                    resultSet.getString("nome_fornecedor"),
                    resultSet.getInt("qtd"),
                    resultSet.getInt("unidades")));
                tbvFornecedores.setItems(ListaFornecedores);

                            
                Callback<TableColumn<Fornecedores, String>, TableCell<Fornecedores, String>> cellFactory = (TableColumn<Fornecedores, String> param) -> {
                final TableCell<Fornecedores, String> cell = new TableCell<Fornecedores, String>() {

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        //cria onotao de vermais
                        final Button detailButton = new Button ("Ver +");
                        detailButton.setId("detailButton");
                        Fornecedores f = getTableView().getItems().get(getIndex());
           
                        detailButton.setOnAction(event ->{
                            FXMLLoader fxmlLoader = new FXMLLoader(
                         App.class.getResource("DetalhesFornecedores.fxml"));                                    

                            try {
                                fxmlLoader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            //carrega a nova tela passando os parametros para a proxima
                            DetalhesFornecedoresController detalhesFornecedoresController = fxmlLoader.getController();
                            detalhesFornecedoresController.setTextField(f.getNome_fornecedor(), f.getQtd(), f.getUnidades());
                            Parent parent = fxmlLoader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                        });
                        setGraphic(detailButton);
                        setText(null);
                    }}                     
                };
                return cell;
            };
            tbcDetalhes.setCellFactory(cellFactory);            
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
