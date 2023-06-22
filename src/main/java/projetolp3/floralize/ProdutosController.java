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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import projetolp3.floralize.model.AppState;
import projetolp3.floralize.model.ItemCarrinho;
import projetolp3.floralize.model.Produtos;

/**
 * FXML Controller class
 *
 * @author r0039435
 */
public class ProdutosController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label lblMensagemErro;

    @FXML
    private Label lblQtdCarrinho;

    @FXML
    private TableView<Produtos> tbvProdutos;

    @FXML
    private TableColumn<Produtos, String> tbcNome;

    @FXML
    private TableColumn<Produtos, String> tbcFornecedor;

    @FXML
    private TableColumn<Produtos, String> tbcPreco;

    @FXML
    private TableColumn<Produtos, String> tbcQuantidade;

    @FXML
    private TableColumn<Produtos, String> tbcDetalhes;

    @FXML
    private TextField tfPesquisar;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<Produtos> ListaProdutos = FXCollections.observableArrayList();
    private AppState appState = AppState.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDataProdutos();
        refreshQtdCarrinho();

        tfPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            realizarPesquisa(newValue);
        });
    }

    private void realizarPesquisa(String termo) {
        //busca por fornecedores
        List<Produtos> resultados = new ArrayList<>();
        for (Produtos produto : ListaProdutos) {
            if (produto.getNome_produto().contains(termo)
                    || produto.getNome_fornecedor().contains(termo)
                    || produto.getDescricao().contains(termo)) {
                resultados.add(produto);
            }
        }
        // Atualize a TableView com os resultados da pesquisa
        ObservableList<Produtos> observableResultados = FXCollections.observableArrayList(resultados);
        tbvProdutos.setItems(observableResultados);
    }

    public void loadDataProdutos() {
        connection = new ConexaoMySQL().getConnection();
        ListaProdutos.clear();
        refreshTable();
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
        tbcFornecedor.setCellValueFactory(new PropertyValueFactory<>("nome_fornecedor"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }

    public void loadCarrinho() throws IOException {
        Pane pane = FXMLLoader.load(App.class.getResource("ConfirmacaoPedido.fxml"));
        anchorPane.getChildren().setAll(pane);
    }

    public void refreshQtdCarrinho() {
        Set<ItemCarrinho> carrinho = appState.getCarrinho();
        int quantidadeObjetos = carrinho.size();
        lblQtdCarrinho.setText("" + quantidadeObjetos);
    }

    public void updateQuantidade(int rowIndex, int novaQuantidade) {
        Produtos produto = tbvProdutos.getItems().get(rowIndex);
        produto.setQtd(novaQuantidade);
        produto.setQuantidadeAdicionada(novaQuantidade);
        tbvProdutos.refresh();

        appState.setQuantidadeAtualizada(produto.getNome_produto(), novaQuantidade);
    }

    public void refreshTable() {
        try {
            ListaProdutos.clear();

            query = "SELECT * FROM `view_produtos`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Produtos produto = new Produtos(
                        resultSet.getString("nome_produto"),
                        resultSet.getInt("id_produto"),
                        resultSet.getString("nome_fornecedor"),
                        resultSet.getInt("id_fornecedor"),
                        resultSet.getInt("qtd"),
                        resultSet.getDouble("preco"),
                        resultSet.getString("descricao")
                );

                int quantidadeAtualizada = appState.getQuantidadeAtualizada(produto.getNome_produto());
                if (quantidadeAtualizada >= 0) {
                    produto.setQtd(quantidadeAtualizada);
                }

                ListaProdutos.add(produto);
            }

            tbvProdutos.setItems(ListaProdutos);

            Callback<TableColumn<Produtos, String>, TableCell<Produtos, String>> cellFactory = (TableColumn<Produtos, String> param) -> {
                final TableCell<Produtos, String> cell = new TableCell<Produtos, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        //célula criada apenas em linhas não vazias
                        if (empty) {
                            setGraphic(null);
                            setText(null);

                        } else {
                            //cria os botoes de adicionar, ver + e seus ids para o css
                            final Button addButton = new Button("Adicionar");
                            final Button detailButton = new Button("Ver +");
                            detailButton.setId("detailButton");
                            addButton.setId("addButton");
                            Produtos p = getTableView().getItems().get(getIndex());

                            addButton.setOnAction(event -> {
                                Set<ItemCarrinho> carrinho = appState.getCarrinho();

                                //abre uma tela para inserir a quantidade
                                Stage stage = new Stage();
                                VBox vbox = new VBox();
                                Label quantityLabel = new Label("Digite a quantidade:");
                                TextField quantityField = new TextField();
                                Button confirmButton = new Button("Confirmar");

                                //estilo da tela
                                vbox.getStyleClass().add("container");
                                quantityLabel.getStyleClass().add("label");
                                quantityField.getStyleClass().add("textfield");
                                confirmButton.getStyleClass().add("button");

                                confirmButton.setOnAction(confirmEvent -> {
                                    //adiciona ao carrinho junto as verificações
                                    int quantidade = Integer.parseInt(quantityField.getText());
                                    ItemCarrinho novoItem = new ItemCarrinho(p.getNome_produto(), p.getNome_fornecedor(), p.getPreco(), quantidade, p.getId_produto(), p.getId_fornecedor());

                                    if (carrinho.contains(novoItem)) {
                                        lblMensagemErro.setText("O item já está no carrinho!");
                                    } else if (quantidade > p.getQtd()) {
                                        lblMensagemErro.setText("Quantidade selecionada maior do que o estoque!");
                                    } else {
                                        carrinho.add(novoItem);
                                        lblMensagemErro.setText("Item adicionado ao carrinho!");
                                        refreshQtdCarrinho();
                                        p.setQtd(p.getQtd() - quantidade);
                                        int rowIndex = getIndex();
                                        updateQuantidade(rowIndex, p.getQtd());
                                    }
                                    stage.close();
                                });

                                ItemCarrinho novoItem = new ItemCarrinho(p.getNome_produto(), p.getNome_fornecedor(), p.getPreco(), 1, p.getId_produto(), p.getId_fornecedor());

                                // Verificar se o item já está no carrinho antes de abrir a janela
                                if (carrinho.contains(novoItem)) {
                                    System.out.println("O item já está no carrinho!");
                                    lblMensagemErro.setText("O item já está no carrinho!");
                                } else {
                                    vbox.getChildren().addAll(quantityLabel, quantityField, confirmButton);
                                    Scene scene = new Scene(vbox);
                                    stage.setScene(scene);
                                    scene.getStylesheets().add(getClass().getResource("/projetolp3/floralize/css/styleConfirm.css").toExternalForm());
                                    stage.showAndWait();
                                }
                            });

                            detailButton.setOnAction(event -> {

                                FXMLLoader fxmlLoader = new FXMLLoader(
                                        App.class.getResource("DetalhesProdutos.fxml"));

                                try {
                                    fxmlLoader.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                //abre a tela com detalhes daquele produto em especifico
                                DetalhesProdutosController detalhesProdutosController = fxmlLoader.getController();
                                //
                                detalhesProdutosController.setUpdate(true);
                                detalhesProdutosController.setTextField(p.getNome_produto(), p.getNome_fornecedor(), p.getQtd(), p.getPreco(), p.getDescricao());
                                Parent parent = fxmlLoader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();

                            });
                            //configura a posicao dos botoes
                            HBox managebtn = new HBox(addButton, detailButton);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(addButton, new Insets(2, 2, 0, 3));
                            HBox.setMargin(detailButton, new Insets(2, 3, 0, 2));

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
