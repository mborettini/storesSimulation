package service;

import entity.Client;
import entity.Product;
import entity.Store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class StoreService {

    public static ArrayList<Store> generateStores(ArrayList<Product> products) {
        ArrayList<Store> stores = new ArrayList<>(3);
        HashMap<Product, Long> warehouse = new HashMap<>(8);
        ArrayList<Client> clientsInQueue = new ArrayList<>();
        for (Product product : products) {
            warehouse.put(product, (long) 0);
        }
        for (int i = 1; i < 4; i++) {
            stores.add(new Store(
                    (long) i,
                    "store " + i,
                    warehouse,
                    BigDecimal.valueOf(0),
                    BigDecimal.valueOf(0),
                    0.0,
                    0,
                    clientsInQueue));
        }
        return stores;
    }
}
