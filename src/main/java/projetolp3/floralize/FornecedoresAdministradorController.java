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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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
public class FornecedoresAdministradorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableColumn<Fornecedores, String> tbcDetalhes;

    @FXML
    private TableColumn<Fornecedores, String> tbcNome;

    @FXML
    private TableColumn<Fornecedores, String> tbcCPF;

    @FXML
    private TableColumn<Fornecedores, String> tbcArea;

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
            if (fornecedores.getNome_fornecedor().contains(termo)) {
                resultados.add(fornecedores);
            }
        }

        // Atualiza a TableView com os resultados da pesquisa
        ObservableList<Fornecedores> observableResultados = FXCollections.observableArrayList(resultados);
        tbvFornecedores.setItems(observableResultados);
    }

    public void loadDataFornecedores() {

        connection = new ConexaoMySQL().getConnection();
        refreshTable();

        tbcNome.setCellValueFactory(new PropertyValueFactory<>("Nome_fornecedor"));
        tbcCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tbcArea.setCellValueFactory(new PropertyValueFactory<>("Area"));
    }

    public void adicionarProduto() {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("AlterarFornecedor.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.setOnHidden(event -> refreshTable()); // Add refreshTable() call when the window is closed
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ProdutosFornecedorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refreshTable() {
        try {
            ListaFornecedores.clear();

            query = "SELECT id, nome,cpf_cnpj, area FROM fornecedores;";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ListaFornecedores.add(new Fornecedores(
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        resultSet.getString("cpf_cnpj"),
                        resultSet.getString("area")));
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
                                //cria os botoes de editar e deletar assim como seus ids para o css
                                final Button editButton = new Button("Editar");
                                final Button deleteButton = new Button("Deletar");
                                editButton.setId("editButton");
                                deleteButton.setId("deleteButton");

                                editButton.setOnAction(event -> {
                                    //implementar
                                });

                                deleteButton.setOnAction(event -> {
                                    Fornecedores f = getTableView().getItems().get(getIndex());
                                    System.out.println("f.getId(): " + f.getId());
                                    try {
                                        String deleteQuery = "DELETE FROM fornecedores WHERE id = ?";
                                        preparedStatement = connection.prepareStatement(deleteQuery);
                                        preparedStatement.setInt(1, f.getId());

                                        // para atualizar ao deletar
                                        int rowsAffected = preparedStatement.executeUpdate();

                                        if (rowsAffected > 0) {
                                            System.out.println("Produto deletado com sucesso!");
                                            refreshTable();
                                        } else {
                                            System.out.println("Falha ao deletar o produto!");
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
                                
                                //alinhamento dos botoes  
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
