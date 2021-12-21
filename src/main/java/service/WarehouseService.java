package service;

import entity.Product;
import entity.Store;
import org.decimal4j.util.DoubleRounder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class WarehouseService {

    public synchronized static void realizeMonthlyDelivery(Store store) {
        ArrayList<Long> standardProductQuantities = new ArrayList<>(List.of(250L, 270L, 320L, 350L, 320L, 200L, 100L, 50L));
        HashMap<Product, Long> warehouse = new HashMap<>();
        Long storeId = store.getId();
        int i = 0;
        for (Map.Entry<Product, Long> w : store.getWarehouse().entrySet()) {
            Product product = w.getKey();
            Long orderedQuantity = w.getValue();
            switch (storeId.intValue()) {
                case 1:
                    Long increment1 = (long) (standardProductQuantities.get(i) * 1.2);
                    if (orderedQuantity > increment1) {
                        product.setMargin(0.02);
                    }
                    orderedQuantity = orderedQuantity + increment1;
                    break;
                case 2:
                    Long increment2 = standardProductQuantities.get(i);
                    if (orderedQuantity > increment2) {
                        product.setMargin(0.02);
                    }
                    orderedQuantity = orderedQuantity + increment2;
                    break;
                case 3:
                    Long increment3 = (long) (standardProductQuantities.get(i) * 0.8);
                    if (orderedQuantity > increment3) {
                        product.setMargin(0.02);
                    }
                    orderedQuantity = orderedQuantity + increment3;
                    break;
            }
            warehouse.put(product, orderedQuantity);
            i += 1;
        }
        store.setWarehouse(warehouse);
    }

    public synchronized static void deliverProductsToClient(Long productId, Integer numberOfProductsToBuy, Store store) {
        HashMap<Product, Long> warehouse = store.getWarehouse();
        Optional<Product> optionalProduct = store.getWarehouse().keySet()
                .stream()
                .filter(k -> Objects.equals(k.getId(), productId))
                .findFirst();

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            warehouse.put(product, warehouse.containsKey(product) ? warehouse.get(product) - numberOfProductsToBuy : warehouse.get(product));
        }
    }

    public synchronized static void calculateCredit(Store store) {
        BigDecimal debt = null;
        BigDecimal fixedCostAmount = BigDecimal.valueOf(1500);

        for (Map.Entry<Product, Long> w : store.getWarehouse().entrySet()) {
            Product product = w.getKey();
            Long orderedQuantity = w.getValue();
            debt = store.getDebt().add(product.getNetPrice().multiply(BigDecimal.valueOf(orderedQuantity).multiply(BigDecimal.valueOf(1.2))));
        }

        debt.add(fixedCostAmount);
        store.setDebt(debt.setScale(2, RoundingMode.UP));
    }

    public synchronized static void calculateIncome(Long productId, Integer numberOfProductsToBuy, Store store) {
        HashMap<Product, Long> warehouse = store.getWarehouse();
        BigDecimal income;
        Optional<Product> optionalProduct = warehouse.keySet()
                .stream()
                .filter(k -> Objects.equals(k.getId(), productId))
                .findFirst();

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            BigDecimal netPrice = product.getNetPrice();
            double margin = DoubleRounder.round(product.getMargin(), 2);
            BigDecimal grossPrice = netPrice.add(netPrice.multiply(BigDecimal.valueOf(margin)));

            BigDecimal monthlyIncome = grossPrice.multiply(BigDecimal.valueOf(numberOfProductsToBuy)).setScale(2, RoundingMode.UP);

            income = store.getIncome().add(monthlyIncome);
            store.setIncome(income);
        }
    }
}
