package com.example.griddominion.models.db;

import com.example.griddominion.utils.Item;
import com.example.griddominion.utils.errors.InsufficientStorage;
import jakarta.persistence.*;

import java.util.HashMap;


@Entity
@Table(name = "inventory")
public class InventoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventoryHashMap")
    private HashMap<Item, Integer> inventoryHashMap;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserModel user;

    public InventoryModel() {
    }

    public HashMap<Item, Integer> getInventory(){
        return inventoryHashMap;
    }

    public void setInventory(HashMap<Item, Integer> inventoryHashMap){
        this.inventoryHashMap = inventoryHashMap;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}