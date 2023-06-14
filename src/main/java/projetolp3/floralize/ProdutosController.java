/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package projetolp3.floralize;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/*se usado consulta no banco*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Alert;
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
import javafx.scene.text.Text;
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
    private Text txtProdutos;
    
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Produtos produtos = null;
    
    ObservableList<Produtos> ListaProdutos = FXCollections.observableArrayList();
    //Set<ItemCarrinho> carrinho = new LinkedHashSet<>(); 
    private AppState appState = AppState.getInstance();


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Connection conn = new ConexaoMySQL().getConnection();

        //Prepara a consulta
        String sql = "SELECT COUNT(*) FROM fornecedores";

        try (PreparedStatement statement = conn.prepareStatement(sql)){
            // Executa a consulta e obtém o resultado
            ResultSet resultSet = statement.executeQuery();

            // Verifica se há resultados e armazena o valor em uma variável
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            // Use o valor armazenado na variável
            System.out.println("Número de fornecedores: " + count);
            
        } catch (SQLException e) {
            e.printStackTrace();
        
        
        
        }    */       
        
        loadDataProdutos();
        refreshQtdCarrinho();
    }    
    
    public void loadDataProdutos(){
        
        connection = new ConexaoMySQL().getConnection();
        refreshTable();
        
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
        tbcFornecedor.setCellValueFactory(new PropertyValueFactory<>("nome_fornecedor"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("qtd"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }
    
    public void loadCarrinho() throws IOException {
        /*Stage stage = (Stage) tbvProdutos.getScene().getWindow();

        //Carregar a nova cena
        FXMLLoader fxmlLoader = new FXMLLoader(
     App.class.getResource("ConfirmacaoPedido.fxml"));
        Scene dash = new Scene(fxmlLoader.load());

        //Exibir a nova cena
        stage.setScene(dash);
        stage.setTitle("CONFIRMAR PEDIDO");
        stage.show();*/
        
        Pane pane = FXMLLoader.load(App.class.getResource("ConfirmacaoPedido.fxml"));  
        anchorPane.getChildren().setAll(pane);
    }
    
    public void refreshQtdCarrinho(){
        Set<ItemCarrinho> carrinho = appState.getCarrinho();
        int quantidadeObjetos = carrinho.size();
        //System.out.println("Quantidade de objetos no carrinho: " + quantidadeObjetos);
        lblQtdCarrinho.setText(""+quantidadeObjetos);
    }
    
    public void refreshTable(){
        try {
            ListaProdutos.clear();
            
            query = "SELECT * FROM `view_produtos_descricao`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                ListaProdutos.add(new Produtos(
                    resultSet.getString("nome_produto"),
                    resultSet.getString("nome_fornecedor"),
                    resultSet.getInt("qtd"),
                    resultSet.getDouble("preco"),
                    resultSet.getString("descricao")));
                tbvProdutos.setItems(ListaProdutos);
                
                
                //add cell of button edit 
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
                        
                        final Button addButton = new Button ("Adicionar");
                        final Button detailButton = new Button ("Ver +");
                        detailButton.setId("detailButton");
                        addButton.setId("addButton");
                        Produtos p = getTableView().getItems().get(getIndex());
                        
                        addButton.setOnAction(event -> {
                            Set<ItemCarrinho> carrinho = appState.getCarrinho();
                            
                            Stage stage = new Stage();
                            VBox vbox = new VBox();
                            Label quantityLabel = new Label("Digite a quantidade:");
                            TextField quantityField = new TextField();
                            Button confirmButton = new Button("Confirmar");

                            vbox.getStyleClass().add("container");
                            quantityLabel.getStyleClass().add("label");
                            quantityField.getStyleClass().add("textfield");
                            confirmButton.getStyleClass().add("button");

                            confirmButton.setOnAction(confirmEvent -> {
                                int quantidade = Integer.parseInt(quantityField.getText());

                                ItemCarrinho novoItem = new ItemCarrinho(p.getNome_produto(), p.getNome_fornecedor(), p.getPreco(), quantidade);

                                if (carrinho.contains(novoItem)) {
                                    System.out.println("Item repetido!");
                                    lblMensagemErro.setText("O item já está no carrinho!");
                                } else if (quantidade > p.getQtd()) {
                                    System.out.println("Quantidade inválida!");
                                    lblMensagemErro.setText("Quantidade selecionada maior do que o estoque!");
                                } else {
                                    carrinho.add(novoItem);
                                    System.out.println("Item adicionado ao carrinho: " + novoItem.getNomeProduto());
                                    lblMensagemErro.setText("Item adicionado ao carrinho!");
                                    refreshQtdCarrinho();
                                }

                                System.out.println("\n\n\nItens no carrinho:");
                                for (ItemCarrinho items : carrinho) {
                                    System.out.println("Nome do produto: " + items.getNomeProduto());
                                    System.out.println("Nome do fornecedor: " + items.getFornecedor());
                                    System.out.println("Preço: " + items.getPreco());
                                    System.out.println("Quantidade: " + items.getQuantidade());
                                    System.out.println("-----------------------------");
                                }
                                stage.close();
                            });

                            ItemCarrinho novoItem = new ItemCarrinho(p.getNome_produto(), p.getNome_fornecedor(), p.getPreco(), 1);

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


                        detailButton.setOnAction(event ->{
                            //Produtos p = getTableView().getItems().get(getIndex());
                            //Alert alert = new Alert (Alert.AlertType.INFORMATION);
                            //alert.setContentText(p.getNome_fornecedor());
                            //alert.show();

                            FXMLLoader fxmlLoader = new FXMLLoader(
                         App.class.getResource("DetalhesProdutos.fxml"));                                    

                            try {
                                fxmlLoader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(ProdutosController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            DetalhesProdutosController detalhesProdutosController = fxmlLoader.getController();
                            detalhesProdutosController.setUpdate(true);
                            detalhesProdutosController.setTextField(p.getNome_produto(), p.getNome_fornecedor(),p.getQtd(), p.getPreco(), p.getDescricao());
                            Parent parent = fxmlLoader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();

                        });
                        //setGraphic(editButton);
                        //setText(null);
                        
                        HBox managebtn = new HBox(addButton, detailButton);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(addButton, new Insets(2, 2, 0, 3));
                        HBox.setMargin(detailButton, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);
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