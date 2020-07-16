package model;

public class Order {
    private String name;
    private FruitType fruitType;
    private int fruitQuantity;

    public Order() {
    }

    public Order(String name, FruitType fruitType, int fruitQuantity) {
        this.name = name;
        this.fruitType = fruitType;
        this.fruitQuantity = fruitQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public int getFruitQuantity() {
        return fruitQuantity;
    }

    public void setFruitQuantity(int fruitQuantity) {
        this.fruitQuantity = fruitQuantity;
    }
}
