package simulation;

import entity.Client;
import entity.Store;
import service.WarehouseService;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static service.ClientOrderService.performShopping;

public class Simulate {

    public synchronized void simulate(Integer dayOfMonth, ArrayList<Client> clients, ArrayList<Store> stores, Store store, int dayOfSimulation) {
        int dayOfYearWhenEndOfMonth = dayOfSimulation + dayOfMonth;
        try {
            for (int i = 1; i <= dayOfMonth; i++) {
                int finalDayOfSimulation = dayOfSimulation;
                ArrayList<Client> clientsWhoShoppingToday = (ArrayList<Client>) clients.stream()
                        .filter(c -> c.getShoppingDay().equals(finalDayOfSimulation) && c.getStoreId().equals(store.getId()))
                        .collect(Collectors.toList());
                synchronized (store) {
                    performShopping(clientsWhoShoppingToday, stores, store, dayOfYearWhenEndOfMonth);
                }
                dayOfSimulation++;
            }
            WarehouseService.realizeMonthlyDelivery(store);
            WarehouseService.calculateCredit(store);
        } catch (Exception e) {
            System.out.println("Thread  interrupted.");
        }
    }
}



