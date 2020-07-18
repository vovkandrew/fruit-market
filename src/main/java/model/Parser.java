package model;

import java.time.LocalDate;
import java.util.List;
import org.json.simple.JSONObject;

public class Parser {
    public static Order parseToOrder(JSONObject object) {
        Order order = new Order();
        JSONObject jsonObject = (JSONObject) object.get("client");
        order.setName((String) jsonObject.get("name"));
        order.setFruitType((FruitType.valueOf((String) jsonObject.get("type"))));
        order.setFruitQuantity(Integer.parseInt((String) jsonObject.get("count")));
        return order;
    }

    public static void parseAndAddFruit(JSONObject object, List<Fruit> fruits) {
        Fruit fruit = new Fruit();
        JSONObject jsonObject = (JSONObject) object.get("fruit");
        fruit.setFruitType((FruitType.valueOf((String) jsonObject.get("fruitType"))));
        fruit.setExpirationPeriod(Integer.parseInt((String) jsonObject.get("expirationPeriod")));
        fruit.setSupplyDate(LocalDate.parse((CharSequence) jsonObject.get("supplyDate")));
        fruit.setPrice(Double.parseDouble((String) jsonObject.get("price")));
        fruits.add(fruit);
    }
}
