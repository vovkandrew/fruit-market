package model;

import java.time.LocalDate;

public class Fruit {
    private FruitType fruitType;
    private int expirationPeriod;
    private LocalDate supplyDate; //yyyy-MM-dd
    private double price;

    public Fruit(FruitType fruitType, int expirationPeriod, LocalDate supplyDate, double price) {
        this.fruitType = fruitType;
        this.expirationPeriod = expirationPeriod;
        this.supplyDate = supplyDate;
        this.price = price;
    }

    public Fruit() {
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public int getExpirationPeriod() {
        return expirationPeriod;
    }

    public void setExpirationPeriod(int expirationPeriod) {
        this.expirationPeriod = expirationPeriod;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(LocalDate supplyDate) {
        this.supplyDate = supplyDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Fruit{"
                + "fruitType=" + fruitType
                + ", expirationPeriod=" + expirationPeriod
                + ", supplyDate=" + supplyDate
                + ", price=" + price
                + '}';
    }
}
