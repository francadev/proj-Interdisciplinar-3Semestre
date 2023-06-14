/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetolp3.floralize.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author r0039435
 */
public class AppState {
    private static final AppState instance = new AppState();
    private Set<ItemCarrinho> carrinho;

    private AppState() {
        carrinho = new LinkedHashSet<>();
    }

    public static AppState getInstance() {
        return instance;
    }

    public Set<ItemCarrinho> getCarrinho() {
        return carrinho;
    }
}
