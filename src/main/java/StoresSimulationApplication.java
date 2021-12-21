import entity.Client;
import entity.Product;
import entity.Store;
import service.*;
import simulation.Simulate;
import simulation.StoreThread;

import java.text.DecimalFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoresSimulationApplication {

    public static void main(String[] args) {
        // Stores, products and clients lists initialization
        ArrayList<Product> products = ProductService.generateProducts(); // 8 products
        ArrayList<Store> stores = StoreService.generateStores(products); // 3 stores
        ArrayList<Client> clients = ClientService.generateClients(); // 1000 clients

        //Initial products' delivery to warehouse
        stores.forEach(WarehouseService::realizeMonthlyDelivery);

        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Simulation for 12 months
        int dayOfSimulation = 1;
        for (final Month month : Month.values()) {
            Simulate sim = new Simulate();

            StoreThread S1 = new StoreThread(sim, month.length(false), stores, clients, stores.get(0), dayOfSimulation);
            StoreThread S2 = new StoreThread(sim, month.length(false), stores, clients, stores.get(1), dayOfSimulation);
            StoreThread S3 = new StoreThread(sim, month.length(false), stores, clients, stores.get(2), dayOfSimulation);

            S1.start();
            S2.start();
            S3.start();

            try {
                S1.join();
                S2.join();
                S3.join();

                String leftAlignFormat = "| %-7s | %-10.1f | %-14s | %-15s | %-15s | %-140s |%n";

                System.out.println(month);
                System.out.format("+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+%n");
                System.out.format("| Store   | Evaluation | Customers no.  | Income          | Debt            | Warehouse state                                                                                                                              |%n");
                System.out.format("+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+%n");
                for (int i = 0; i < 3; i++) {
                    Store store = stores.get(i);
                    HashMap<Product, Long> warehouse = store.getWarehouse();
                    StringBuilder builder = new StringBuilder();
                    for (Map.Entry<Product, Long> e : warehouse.entrySet()) {
                        builder.append(e.getKey().getName()).append(": ").append(e.getValue()).append(", ");
                    }
                    Double evaluation = store.getEvaluation();
                    Integer clientsNo = store.getClientsNumber();
                    Double eval = (double) evaluation / clientsNo;
                    String readyText = builder.toString();
                    System.out.format(leftAlignFormat, store.getName(), eval, store.getClientsNumber(), df.format(store.getIncome()), df.format(store.getDebt()), readyText);
                }
                System.out.format("+---------+------------+----------------+-----------------+-----------------+----------------------------------------------------------------------------------------------------------------------------------------------+%n");
            } catch (Exception e) {
                System.out.println("Interrupted");
            }
            dayOfSimulation = dayOfSimulation + month.length(false);
        }
    }
}
