package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static model.Parser.parseAndAddFruit;
import static model.Parser.parseToOrder;

public class ShopWarehouse {
    private static final List<Fruit> fruitList = new ArrayList<>();
    private static double moneyBalance = 0;

    public static List<Fruit> getFruitList() {
        return fruitList;
    }

    public static double getMoneyBalance() {
        return moneyBalance;
    }

    public void addFruit(String jsonPath) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            jsonArray.forEach(o -> parseAndAddFruit((JSONObject) o, fruitList));
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Can't open the file and complete parsing", e);
        }

    }

    public void save(String jsonPath) {
        try (FileWriter file = new FileWriter(jsonPath)) {
            file.write(fruitList.toString());
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException("Can't save data from the list of fruits", e);
        }
    }

    public void load(String jsonPath) {
        fruitList.clear();
        addFruit(jsonPath);
    }

    public List<Fruit> getSpoiledFruits(Date date) {
        return fruitList.stream()
                .filter(fruit -> willExpireBeforeDate(fruit, date))
                .collect(Collectors.toList());
    }

    public List<Fruit> getSpoiledFruits(Date date, String fruitType) {
        return fruitList.stream()
                .filter(fruit -> willExpireBeforeDate(fruit, date))
                .filter(fruit -> fruit.getFruitType().equals(
                        FruitType.valueOf(fruitType)))
                .collect(Collectors.toList());
    }

    private boolean willExpireBeforeDate(Fruit fruit, Date date) {
        int daysTillExpire = fruit.getExpirationPeriod();
        LocalDate supplyDate = fruit.getSupplyDate();
        LocalDate whenExpire = supplyDate.plusDays(daysTillExpire);
        LocalDate expirationDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return whenExpire.isBefore(expirationDate);
    }

    public List<Fruit> getAvailableFruits(Date date) {
        return fruitList.stream()
                .filter(fruit -> !willExpireBeforeDate(fruit, date))
                .collect(Collectors.toList());
    }

    public List<Fruit> getAvailableFruits(Date date, String fruitType) {
        return fruitList.stream()
                .filter(fruit -> !willExpireBeforeDate(fruit, date))
                .filter(fruit -> fruit.getFruitType().equals(
                        FruitType.valueOf(fruitType)))
                .collect(Collectors.toList());
    }

    public List<Fruit> getAddedFruits(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return fruitList.stream()
                .filter(fruit -> fruit.getSupplyDate().equals(localDate))
                .collect(Collectors.toList());
    }

    public List<Fruit> getAddedFruits(Date date, String fruitType) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return fruitList.stream()
                .filter(fruit -> fruit.getSupplyDate().equals(localDate))
                .filter(fruit -> fruit.getFruitType().equals(
                        FruitType.valueOf(fruitType)))
                .collect(Collectors.toList());
    }

    public void sell(String jsonPath) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonPath)) {
            Object object = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) object;
            List<Order> orderList = (List<Order>) jsonArray.stream()
                            .map(o -> parseToOrder((JSONObject) o))
                            .collect(Collectors.toList());
            for (Order o: orderList) {
                checkWarehouse(o);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Can't open the file and complete parsing");
        }
    }

    private void checkWarehouse(Order order) {
        int availableFruits = (int) fruitList.stream()
                        .filter(fruit -> fruit.getFruitType().equals(order.getFruitType()))
                        .count();
        if (availableFruits > order.getFruitQuantity()) {
            for (int i = order.getFruitQuantity(); i > 0; i--) {
                Fruit fruit = fruitList.stream()
                        .filter(fr -> fr.getFruitType().equals(order.getFruitType()))
                        .findFirst()
                        .get();
                moneyBalance = moneyBalance + fruit.getPrice();
                fruitList.remove(fruit);
            }
            System.out.println("Order for " + order.getName() + " has been fulfilled");
        }
    }
}
