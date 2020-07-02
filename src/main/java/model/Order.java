package model;

public class Order {
    private String name;
    private Fruit.FruitType fruitType;

    enum FruitType {
        APPLE,
        BANANA,
        MANGO,
        KIWI,
        PINEAPPLE,
        CHERRY,
        STRAWBERRY
    }

    private int fruitQuantity;

    public Order() {
    }

    public Order(String name, Fruit.FruitType fruitType, int fruitQuantity) {
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

    public Fruit.FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(Fruit.FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public int getFruitQuantity() {
        return fruitQuantity;
    }

    public void setFruitQuantity(int fruitQuantity) {
        this.fruitQuantity = fruitQuantity;
    }
}
