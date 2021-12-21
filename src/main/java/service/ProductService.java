package service;

import entity.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ProductService {

    public static ArrayList<Product> generateProducts() {
        ArrayList<Product> products = new ArrayList<>(8);

        BigDecimal minGrossPrice = BigDecimal.valueOf(80.0);
        BigDecimal maxGrossPrice = BigDecimal.valueOf(2000.0);
        BigDecimal minNetPrice = minGrossPrice.divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP);

        BigDecimal sub = maxGrossPrice.subtract(minGrossPrice);
        BigDecimal increment = sub.divide(BigDecimal.valueOf(7.0), 4, RoundingMode.HALF_UP);
        BigDecimal netIncrement = increment.divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP);

        for (int i = 1; i < 9; i++) {
            products.add(
                    new Product(
                            (long) i,
                            "product " + i,
                            (i == 1 ? minNetPrice : minNetPrice.add(netIncrement)),
                            0.20));
            minNetPrice = products.get(i - 1).getNetPrice();
        }
        return products;
    }
}
