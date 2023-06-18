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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import projetolp3.floralize.bd.ConexaoMySQL;
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
    
    ConexaoMySQL conexao = new ConexaoMySQL();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        refreshTableCarrinho();
    }    
    
    public void confirmarPedido() throws IOException {
        insertItensPedido();
        updateProdutos();
        criarPedidos();
        openPopUp();
    }

    
    public void voltarProdutos() throws IOException{
        Pane pane = FXMLLoader.load(App.class.getResource("Produtos.fxml"));  
        anchorPane.getChildren().setAll(pane);
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
                        int quantidadeAtualizada = appState.getQuantidadeAtualizada(item.getNomeProduto());
                        quantidadeAtualizada += item.getQuantidade();
                        appState.setQuantidadeAtualizada(item.getNomeProduto(), quantidadeAtualizada);
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
    
    public void updateProdutos() throws IOException {
        try (Connection connection = conexao.getConnection()) {
            for (ItemCarrinho item : carrinho) {
                int quantidade = item.getQuantidade();
                int id_produto = item.getId_produto();
                int id_fornecedor = item.getId_fornecedor();

                String query = "UPDATE view_produtos SET qtd = qtd - ? WHERE id_produto = ? AND id_fornecedor = ? ";

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, quantidade);
                    statement.setInt(2, id_produto);
                    statement.setInt(3, id_fornecedor);
                    statement.executeUpdate();
                }
            }

            // Exibindo uma mensagem de sucesso
            System.out.println("Produtos atualizados com sucesso!");
            carrinho.clear();
        
            // Retornar à tela de produtos
            Pane pane = FXMLLoader.load(App.class.getResource("Produtos.fxml"));
            anchorPane.getChildren().setAll(pane);
            
        } catch (SQLException e) {
            // Exibindo uma mensagem de erro
            System.err.println("Ocorreu um erro ao atualizar os produtos: " + e.getMessage());
        }
    }
    
    public void insertItensPedido() {
       
        try (Connection connection = conexao.getConnection()) {
            int lastId = getLastItemId();
            String query = "INSERT INTO ItensPedido (id, produto_id, fornecedor_id, preco_unitario, quantidade) VALUES (?, ?, ?, ?, ?)";

            for (ItemCarrinho item : carrinho) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, lastId +1);
                    statement.setInt(2, item.getId_produto());
                    statement.setInt(3, item.getId_fornecedor());
                    statement.setDouble(4, item.getPreco());
                    statement.setInt(5, item.getQuantidade());
                    statement.executeUpdate();
                }
            }

            System.out.println("Itens de pedidos inseridos com sucesso!");
        } catch (SQLException e) {
            System.err.println("Ocorreu um erro ao inserir os itens de pedidos: " + e.getMessage());
        }
    }
        public void openPopUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUpConfirmacao.fxml"));
            Parent root = loader.load();

            // Crie um novo Stage (janela) para o pop-up
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Confirmação");

            // Configure o conteúdo do pop-up
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            
            // Defina a opacidade desejada para a janela pop-up (0.0 - totalmente transparente, 1.0 - totalmente opaco)
            popupStage.setOpacity(0.90);
            
            popupStage.setOnHidden(event -> {
            System.out.println("Pop-up fechado!");
                try {
                    voltarProdutos();
                } catch (IOException ex) {
                    Logger.getLogger(ConfirmacaoPedidoController.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
            
            // Exiba o pop-up
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    public int getLastItemId() {
        int lastId = 0;

        try (Connection connection = conexao.getConnection()) {
            String query = "SELECT id FROM ItensPedido ORDER BY id DESC LIMIT 1";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    lastId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }
    
    public void criarPedidos() {
        int lastItemId = getLastItemId();

        try (Connection connection = conexao.getConnection()) {
            // Recuperar os fornecedores presentes no carrinho
            Set<Integer> fornecedores = new HashSet<>();
            for (ItemCarrinho item : carrinho) {
                fornecedores.add(item.getId_fornecedor());
            }

            // Criar um pedido para cada fornecedor presente no carrinho
            for (Integer fornecedorId : fornecedores) {
                // Calcular o preço total do pedido para o fornecedor atual
                double precoTotal = 0.0;
                for (ItemCarrinho item : carrinho) {
                    if (item.getId_fornecedor() == fornecedorId) {
                        precoTotal += item.getPreco() * item.getQuantidade();
                    }
                }

                // Inserir o pedido na tabela Pedidos
                String query = "INSERT INTO Pedidos (cliente_id, fornecedor_id, itenspedido_id, preco_total, status) VALUES (?, ?, ?, ?, 'pendente')";

                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, 1);
                    statement.setInt(2, fornecedorId);
                    statement.setInt(3, lastItemId); // Corrigido: usar lastItemId como itenspedido_id
                    statement.setDouble(4, precoTotal);
                    statement.executeUpdate();
                }
            }

            System.out.println("Pedidos criados com sucesso!");
        } catch (SQLException e) {
            System.err.println("Ocorreu um erro ao criar os pedidos: " + e.getMessage());
        }
    }


}


