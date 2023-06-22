package projetolp3.floralize;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    @FXML
    private Label lb_erro;
    
    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNome;

    AppState appState = AppState.getInstance();
    Set<ItemCarrinho> carrinho = appState.getCarrinho();

    ConexaoMySQL conexao = new ConexaoMySQL();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshTableCarrinho();
    }

    public void confirmarPedido() throws IOException, SQLException {

        if (carrinho.isEmpty()) {
            // O carrinho está vazio, exibir uma mensagem de erro
            lb_erro.setText("O carrinho está vazio. Adicione itens antes de confirmar o pedido.");
        } else {
            if (!tfNome.getText().isEmpty() && !tfEmail.getText().isEmpty()){
                insertItensPedido();
                updateProdutos();
                inserirCliente();
                openPopUp();
            }else{
                lb_erro.setText("Digite todos os dados para a retirada.");
            }
        }
    }

    public void voltarProdutos() throws IOException {
        Pane pane = FXMLLoader.load(App.class.getResource("Produtos.fxml"));
        anchorPane.getChildren().setAll(pane);
    }

    public void refreshTableCarrinho() {
        tbvCompra.getItems().clear();
        tbcNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        tbcFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        tbcPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        // Define valores para a coluna tbcTotal
        tbcTotal.setCellValueFactory(cellData -> {
            ItemCarrinho item = cellData.getValue();
            double total = item.getPreco() * item.getQuantidade();
            return new SimpleStringProperty(String.valueOf(total));
        });

        tbcBtnAdicional.setCellFactory(col -> {
            TableCell<ItemCarrinho, String> cell = new TableCell<>() {
                final Button deleteButton = new Button("Excluir");

                {
                    deleteButton.setOnAction(event -> {
                        //apafa os itens do carrinho e volta a quantidade
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

        //multiplicacao para o total
        double total = 0.0;
        for (ItemCarrinho item : carrinho) {
            total += item.getPreco() * item.getQuantidade();
        }
        BigDecimal totalArredondado = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
        double totalFinal = totalArredondado.doubleValue();
        lblTotal.setText(String.valueOf(totalFinal));
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
            String query = "INSERT INTO ItensPedido (pedido_id, produto_id, fornecedor_id, preco_unitario, quantidade) VALUES (?, ?, ?, ?, ?)";

            for (ItemCarrinho item : carrinho) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, lastId + 1);
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
            
            PopUpConfirmacaoController popUpConfirmacaoController = loader.getController();
            popUpConfirmacaoController.setTextField(getLastItemId(), getLocaisRetirada());

            // Crie um novo Stage (janela) para o pop-up
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Confirmação");

            // Configure o conteúdo do pop-up
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            popupStage.setOpacity(0.95);

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

    public String getLocaisRetirada() {
        String locaisRetirada = "";
        int lastId = getLastItemId();

        try (Connection connection = conexao.getConnection()) {
            String query = "SELECT GROUP_CONCAT(area SEPARATOR '; ') AS areas_concatenadas FROM fornecedores "
                            + "WHERE id IN (SELECT fornecedor_id FROM itenspedido WHERE pedido_id = ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, lastId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    locaisRetirada = resultSet.getString("areas_concatenadas");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locaisRetirada;
    }

    
    public int getLastItemId() {
        int lastId = 0;

        try (Connection connection = conexao.getConnection()) {
            String query = "SELECT pedido_id FROM ItensPedido ORDER BY id DESC LIMIT 1";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    lastId = resultSet.getInt("pedido_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    public int getLastItemIdCliente() {
        int lastIdCliente = 0;

        try (Connection connection = conexao.getConnection()) {
            String query = "SELECT id FROM clientes ORDER BY id DESC LIMIT 1";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    lastIdCliente = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastIdCliente;
    }
    
    public void inserirCliente() throws SQLException {
        try (Connection connection = conexao.getConnection()) {
            String query = "INSERT INTO clientes (nome, email) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, tfNome.getText());
                statement.setString(2, tfEmail.getText());
                statement.executeUpdate(); // Executar a inserção
            }
            System.out.println("Cliente inserido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Ocorreu um erro ao inserir o cliente: " + e.getMessage());
        }
    }
}