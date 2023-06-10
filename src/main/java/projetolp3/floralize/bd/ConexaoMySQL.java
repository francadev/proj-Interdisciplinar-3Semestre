/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetolp3.floralize.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author r0039435
 */
public class ConexaoMySQL extends Conexao{
    
    public ConexaoMySQL() {

        this.driver = "com.mysql.cj.jdbc.Driver";
        this.porta = 3306;
        this.servidor = "localhost";
        this.bd = "floralize";
        this.usuario = "root";
        this.senha = "1banco";
    }
    
    @Override
    public Connection getConnection() {

        try {

            Class.forName(driver);
            con = DriverManager.getConnection(getURL(), usuario, senha);

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ConexaoMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return con;
    }

    @Override
    public String getURL() {
        return "jdbc:mysql://" + this.servidor + ":" + this.porta + "/" + this.bd
                + "?useTimezone=true&serverTimezone=UTC";
    }    
}
