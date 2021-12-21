package entity;

import java.math.BigDecimal;
import java.util.*;

public class Store {

    private final Long id;
    private String name;
    private HashMap<Product, Long> warehouse;
    private BigDecimal income;
    private BigDecimal debt;
    private Double evaluation;
    private Integer clientsNumber;
    private ArrayList<Client> clientsInQueue;

    public Store(Long id, String name, HashMap<Product, Long> warehouse, BigDecimal income, BigDecimal debt, Double evaluation, Integer clientsNumber, ArrayList<Client> clientsInQueue) {
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
        this.income = income;
        this.debt = debt;
        this.evaluation = evaluation;
        this.clientsNumber = clientsNumber;
        this.clientsInQueue = clientsInQueue;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Product, Long> getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(HashMap<Product, Long> warehouse) {
        this.warehouse = warehouse;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }

    public Integer getClientsNumber() {
        return clientsNumber;
    }

    public void setClientsNumber(Integer clientsNumber) {
        this.clientsNumber = clientsNumber;
    }

    public Double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Double evaluation) {
        this.evaluation = evaluation;
    }

    public ArrayList<Client> getClientsInQueue() {
        return clientsInQueue;
    }

    public void setClientsInQueue(ArrayList<Client> clientsInQueue) {
        this.clientsInQueue = clientsInQueue;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", warehouse=" + warehouse +
                ", income=" + income +
                ", debt=" + debt +
                ", evaluation=" + evaluation +
                ", clientsNumber=" + clientsNumber +
                ", clientsInQueue=" + clientsInQueue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(id, store.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
