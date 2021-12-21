package service;

import entity.Client;
import entity.Product;
import entity.Store;
import org.decimal4j.util.DoubleRounder;

import java.util.*;
import java.util.stream.Collectors;

import static service.WarehouseService.*;

public class ClientOrderService {

    private static final Random r = new Random();

    public static Integer generateShoppingDay(Double age, int dayOfYearWhenEndOfMonth) {
        ArrayList<Integer> lambdaValues = generateLambdaValuesForExp();
        Integer lambda = 0;
        int getExp;
        int shoppingDay;

        if (age >= 20 && age <= 30) {
            lambda = lambdaValues.get(0);
        } else if (age > 30 && age <= 40) {
            lambda = lambdaValues.get(1);
        } else if (age > 40 && age <= 50) {
            lambda = lambdaValues.get(2);
        } else if (age > 50 && age <= 60) {
            lambda = lambdaValues.get(3);
        } else if (age > 60 && age <= 70) {
            lambda = lambdaValues.get(4);
        } else if (age > 70 && age <= 80) {
            lambda = lambdaValues.get(5);
        } else if (age > 80 && age <= 90) {
            lambda = lambdaValues.get(6);
        } else if (age > 90 && age <= 100) {
            lambda = lambdaValues.get(7);
        }

        do {
            getExp = getExp(r, lambda).intValue();
            shoppingDay = dayOfYearWhenEndOfMonth + getExp;
        } while (shoppingDay == 0 || getExp == 0);

        return shoppingDay;
    }

    public static ArrayList<Integer> generateLambdaValuesForExp() {
        ArrayList<Integer> lambdaValues = new ArrayList<>();
        double minLambdaValue = 20.0;
        double maxLambdaValue = 60.0;

        double sub = maxLambdaValue - minLambdaValue;
        double increment = sub / 7;

        for (int i = 0; i < 8; i++) {
            lambdaValues.add((int) DoubleRounder.round(minLambdaValue, 0));
            minLambdaValue = minLambdaValue + increment;
        }
        return lambdaValues;
    }

    public static Double getExp(Random rand, double lambda) {
        return -lambda * Math.log(1 - rand.nextDouble());
    }

    public static Integer generateNumberOfProductsToBuy(Double age) {
        ArrayList<Integer> lambdaValues = generateLambdaValuesForPoisson();
        Integer lambda = 0;
        int numberOfProductsToBuy;

        if (age >= 20 && age <= 30) {
            lambda = lambdaValues.get(0);
        } else if (age > 30 && age <= 40) {
            lambda = lambdaValues.get(1);
        } else if (age > 40 && age <= 50) {
            lambda = lambdaValues.get(2);
        } else if (age > 50 && age <= 60) {
            lambda = lambdaValues.get(3);
        } else if (age > 60 && age <= 70) {
            lambda = lambdaValues.get(4);
        } else if (age > 70 && age <= 80) {
            lambda = lambdaValues.get(5);
        } else if (age > 80 && age <= 90) {
            lambda = lambdaValues.get(6);
        } else if (age > 90 && age <= 100) {
            lambda = lambdaValues.get(7);
        }

        do {
            numberOfProductsToBuy = getPoisson(r, lambda);
        } while (numberOfProductsToBuy < 0);

        return numberOfProductsToBuy;
    }

    public static ArrayList<Integer> generateLambdaValuesForPoisson() {
        ArrayList<Integer> lambdaValues = new ArrayList<>();
        double minLambdaValue = 4.0;
        double maxLambdaValue = 15.0;

        double sub = maxLambdaValue - minLambdaValue;
        double increment = sub / 7;

        for (int i = 0; i < 8; i++) {
            lambdaValues.add((int) DoubleRounder.round(maxLambdaValue, 0));
            maxLambdaValue = maxLambdaValue - increment;
        }
        return lambdaValues;
    }

    public static int getPoisson(Random rand, double lambda) {
        double l = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        do {
            k++;
            p *= rand.nextDouble();
        } while (p > l);
        return k - 1;
    }

    public static void evaluateShopping(Client client, Store store) {
        double clientEvaluation = 0.0;

        if (client.getDaysOfWaiting() >= 0 && client.getDaysOfWaiting() <= 7) {
            clientEvaluation = 5.0;
        } else if (client.getDaysOfWaiting() > 7 && client.getDaysOfWaiting() <= 14) {
            clientEvaluation = 4.0;
        } else if (client.getDaysOfWaiting() > 14 && client.getDaysOfWaiting() <= 21) {
            clientEvaluation = 3.0;
        } else if (client.getDaysOfWaiting() > 21 && client.getDaysOfWaiting() <= 31) {
            clientEvaluation = 2.0;
        } else if (client.getDaysOfWaiting() > 31) {
            clientEvaluation = 1.0;
        }

        store.setEvaluation(store.getEvaluation() + clientEvaluation);

        HashMap<Long, Double> tempMap = new HashMap<>(client.getShoppingEvaluation());
        tempMap.put(store.getId(), clientEvaluation);
        client.setShoppingEvaluation(tempMap);
    }

    // Do wyboru sklepu do zakupów została stworzona formuła, która nadaje wagi ocenom w zależności od wieku
    // x(s) = a + b + 3c + i * a + j * b, gdzie:
    // s - id sklepu (od 1 do 3),
    // a - średnia ocena sklepu (od 1 do 5),
    // b - ocena sklepu wystawiona przez danego klienta (od 1 do 5),
    // c - promocja cenowa (0/1) pomnożona razy 3, żeby nadać jej większą wagę
    // i - współczynnik zależny od wieku (im młodsza osoba tym wyższy, ocena sklepu jest dla takich osób bardziej znacząca),
    // j - współczynnik zalezny od wieku (im młodsza osoba tym niższy, personalne odczucia z zakupu nie są aż tak ważne).
    // Sklep, dla którego wartość funkcji będzie najwyższa "wygrywa" i zostaje wybrany przez klienta
    public synchronized static long chooseStoreForShopping(Client client, ArrayList<Store> stores) {
        Integer age = client.getAge();
        Long winnerStore = 0L;
        double i = 0.0;
        double j = 0.0;

        if (age >= 20 && age <= 30) {
            i = 2;
            j = 0.6;
        } else if (age > 30 && age <= 40) {
            i = 1.8;
            j = 0.8;
        } else if (age > 40 && age <= 50) {
            i = 1.6;
            j = 1;
        } else if (age > 50 && age <= 60) {
            i = 1.4;
            j = 1.2;
        } else if (age > 60 && age <= 70) {
            i = 1.2;
            j = 1.4;
        } else if (age > 70 && age <= 80) {
            i = 1;
            j = 1.6;
        } else if (age > 80 && age <= 90) {
            i = 0.8;
            j = 1.8;
        } else if (age > 90 && age <= 100) {
            i = 0.6;
            j = 2;
        }

        double result = 0.0;
        for (Store store : stores) {
            Long id = store.getId();

            Double evaluation = store.getEvaluation();
            Integer clientsNo = store.getClientsNumber();
            double a = (double) evaluation / clientsNo;

            Double b = client.getShoppingEvaluation().get(id);

            if (b != 1) {
                List<Product> products = store.getWarehouse().entrySet().stream()
                        .filter(e -> Objects.equals(e.getKey().getId(), client.getProductId()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                Double margin = products.get(0).getMargin();

                int c;
                if (margin == 0.02) {
                    c = 1;
                } else {
                    c = 0;
                }

                // x(s) = a + b + 3c + i * a + j * b
                double xS;
                xS = a + b + c * 3 + i * a + j * b;

                if (xS > result) {
                    result = xS;
                    winnerStore = id;
                }
            } else {
                // jeżeli ostatnio ocena z zakupów w tym sklepie była 1,
                // zmieniamy ją na średnią sklepu tak, aby dać klientowi możliwość powrotu do tego sklepu w przyszłości
                client.getShoppingEvaluation().put(id, DoubleRounder.round(a, 2));
                break;
            }
        }
        return winnerStore;
    }

    public synchronized static int generateStoreForShopping() {
        return r.ints(1, 4)
                .findFirst()
                .getAsInt();
    }

    public static Long determineProductForClient(Double age) {
        long productId = 0;
        if (age >= 20 && age <= 30) {
            productId = 1;
        } else if (age > 30 && age <= 40) {
            productId = 2;
        } else if (age > 40 && age <= 50) {
            productId = 3;
        } else if (age > 50 && age <= 60) {
            productId = 4;
        } else if (age > 60 && age <= 70) {
            productId = 5;
        } else if (age > 70 && age <= 80) {
            productId = 6;
        } else if (age > 80 && age <= 90) {
            productId = 7;
        } else if (age > 90 && age <= 100) {
            productId = 8;
        }
        return productId;
    }

    public synchronized static boolean checkIfProductIsOnStack(Long productId, Integer numberOfProductsToBuy, HashMap<Product, Long> warehouse) {
        boolean isOnStack = false;
        Long id;
        for (Map.Entry<Product, Long> e : warehouse.entrySet()) {
            id = e.getKey().getId();
            if (Objects.equals(id, productId)) {
                isOnStack = numberOfProductsToBuy <= e.getValue();
                break;
            }
        }
        return isOnStack;
    }

    public synchronized static void performShopping(ArrayList<Client> clientsWhoShoppingToday, ArrayList<Store> stores, Store store, int dayOfYearWhenEndOfMonth) {
        Long productId;
        Integer numberOfProductsToBuy;
        ArrayList<Client> clientsToBeRemovedFromQueue = new ArrayList<>();

        clientsWhoShoppingToday.addAll(store.getClientsInQueue());
        for (Client client : Collections.unmodifiableList(clientsWhoShoppingToday)) {
            productId = client.getProductId();
            numberOfProductsToBuy = client.getNumberOfProductsToBuy();

            boolean isOnStack = ClientOrderService.checkIfProductIsOnStack(productId, numberOfProductsToBuy, store.getWarehouse());

            if (isOnStack) {
                deliverProductsToClient(productId, numberOfProductsToBuy, store);
                store.setClientsNumber(store.getClientsNumber() + 1);
                clientsToBeRemovedFromQueue.add(client);
                evaluateShopping(client, store);
                client.setDaysOfWaiting(0);

                Long storeId = ClientOrderService.chooseStoreForShopping(client, stores);
                Integer shoppingDay = generateShoppingDay((double) client.getAge(), dayOfYearWhenEndOfMonth);

                client.setStoreId(storeId);
                client.setShoppingDay(shoppingDay);

                WarehouseService.calculateIncome(productId, numberOfProductsToBuy, store);
            } else {
                int days = client.getDaysOfWaiting() + 1;
                client.setDaysOfWaiting(days);
            }
        }
        clientsWhoShoppingToday.removeAll(clientsToBeRemovedFromQueue);
        store.setClientsInQueue(clientsWhoShoppingToday);
    }
}
