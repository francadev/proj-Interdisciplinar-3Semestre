package projetolp3.floralize;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import projetolp3.floralize.bd.ConexaoMySQL;
import projetolp3.floralize.model.Pedidos;

public class PedidosFornecedorController implements Initializable {

    @FXML
    private TableView<Pedidos> tbvPedidos;

    @FXML
    private TableColumn<Pedidos, String> tbcNomeCliente;

    @FXML
    private TableColumn<Pedidos, String> tbcNomeFornecedor;

    @FXML
    private TableColumn<Pedidos, String> tbcNomeProduto;

    @FXML
    private TableColumn<Pedidos, String> tbcQuantidade;

    private int fornecedorID;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<Pedidos> ListaPedidos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDataProdutos();
    }

    public void loadDataProdutos() {
        try {
            ListaPedidos.clear();

            connection = new ConexaoMySQL().getConnection();
            query = "CALL pedidosFornecedor(?)";

            preparedStatement = connection.prepareStatement(query);
            
            //resgata o fornecedor logado
            fornecedorID = LoginController.fornecedorLogado.getId();

            // Define o id do Fornecedor
            preparedStatement.setInt(1, fornecedorID);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ListaPedidos.add(new Pedidos(
                        resultSet.getString("nome_cliente"),
                        resultSet.getString("nome_produto"),
                        resultSet.getInt("quantidade")));
            }
            tbvPedidos.setItems(ListaPedidos);

            tbcNomeCliente.setCellValueFactory(new PropertyValueFactory<>("nome_cliente"));
            tbcNomeProduto.setCellValueFactory(new PropertyValueFactory<>("nome_produto"));
            tbcQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        } catch (SQLException ex) {
            Logger.getLogger(PedidosFornecedorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}