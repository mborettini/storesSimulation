package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private final Long id;
    private String name;
    private BigDecimal netPrice;
    private Double margin;

    public Product(Long id, String name, BigDecimal netPrice, Double margin) {
        this.id = id;
        this.name = name;
        this.netPrice = netPrice;
        this.margin = margin;
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

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", netPrice=" + netPrice +
                ", margin=" + margin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
