package entity;

import java.util.Map;
import java.util.Objects;

public class Client {

    private final Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private Integer shoppingDay;
    private Long productId;
    private Integer numberOfProductsToBuy;
    private Long storeId;
    private Integer daysOfWaiting;
    private Map<Long, Double> shoppingEvaluation;
    private String email;
    private Long phone;
    private String city;
    private String street;

    public Client(Long id, String firstName, String lastName, Integer age, Integer shoppingDay, Long productId, Integer numberOfProductsToBuy, Long storeId, Integer daysOfWaiting, Map<Long, Double> shoppingEvaluation, String email, Long phone, String city, String street) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.shoppingDay = shoppingDay;
        this.productId = productId;
        this.numberOfProductsToBuy = numberOfProductsToBuy;
        this.storeId = storeId;
        this.daysOfWaiting = daysOfWaiting;
        this.shoppingEvaluation = shoppingEvaluation;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getShoppingDay() {
        return shoppingDay;
    }

    public void setShoppingDay(Integer shoppingDay) {
        this.shoppingDay = shoppingDay;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getNumberOfProductsToBuy() {
        return numberOfProductsToBuy;
    }

    public void setNumberOfProductsToBuy(Integer numberOfProductsToBuy) {
        this.numberOfProductsToBuy = numberOfProductsToBuy;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Map<Long, Double> getShoppingEvaluation() {
        return shoppingEvaluation;
    }

    public void setShoppingEvaluation(Map<Long, Double> shoppingEvaluation) {
        this.shoppingEvaluation = shoppingEvaluation;
    }

    public Integer getDaysOfWaiting() {
        return daysOfWaiting;
    }

    public void setDaysOfWaiting(Integer daysOfWaiting) {
        this.daysOfWaiting = daysOfWaiting;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", shoppingDay=" + shoppingDay +
                ", productId=" + productId +
                ", numberOfProductsToBuy=" + numberOfProductsToBuy +
                ", storeId=" + storeId +
                ", daysOfWaiting=" + daysOfWaiting +
                ", shoppingEvaluation=" + shoppingEvaluation +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
