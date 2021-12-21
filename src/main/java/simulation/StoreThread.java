package simulation;

import entity.Client;
import entity.Store;

import java.util.ArrayList;

public class StoreThread extends Thread {
    private final Simulate sim;
    private final Integer dayOfMonth;
    private final ArrayList<Store> stores;
    private final ArrayList<Client> clients;
    private final Store store;
    private final int dayOfSimulation;

    public StoreThread(Simulate sim, Integer dayOfMonth, ArrayList<Store> stores, ArrayList<Client> clients, Store store, int dayOfSimulation) {
        this.sim = sim;
        this.dayOfMonth = dayOfMonth;
        this.stores = stores;
        this.clients = clients;
        this.store = store;
        this.dayOfSimulation = dayOfSimulation;
    }

    @Override
    public void run() {
        synchronized (this) {
            sim.simulate(dayOfMonth, clients, stores, store, dayOfSimulation);
        }
    }
}
