package model;

import java.io.FileNotFoundException;
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
            Object object = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) object;
            jsonArray.forEach(o -> parseAndAddFruit((JSONObject) o));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void parseAndAddFruit(JSONObject object) {
        Fruit fruit = new Fruit();
        JSONObject jsonObject = (JSONObject) object.get("fruit");
        fruit.setFruitType((Fruit.FruitType.valueOf((String) jsonObject.get("fruitType"))));
        fruit.setExpirationPeriod(Integer.parseInt((String) jsonObject.get("expirationPeriod")));
        fruit.setSupplyDate(LocalDate.parse((CharSequence) jsonObject.get("supplyDate")));
        fruit.setPrice(Double.parseDouble((String) jsonObject.get("price")));
        fruitList.add(fruit);
    }

    public void save(String jsonPath) {
        try (FileWriter file = new FileWriter(jsonPath)) {
            file.write(fruitList.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
                        Fruit.FruitType.valueOf(fruitType)))
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
                        Fruit.FruitType.valueOf(fruitType)))
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
                        Fruit.FruitType.valueOf(fruitType)))
                .collect(Collectors.toList());
    }

    public void sell(String jsonPath) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(jsonPath)) {
            Object object = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) object;
            List<Order> orderList =
                    (List<Order>) jsonArray
                            .stream()
                            .map(o -> parseToOrder((JSONObject) o))
                            .collect(Collectors.toList());
            for (Order o: orderList) {
                checkWarehouse(o);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Order parseToOrder(JSONObject object) {
        Order order = new Order();
        JSONObject jsonObject = (JSONObject) object.get("client");
        order.setName((String) jsonObject.get("name"));
        order.setFruitType((Fruit.FruitType.valueOf((String) jsonObject.get("type"))));
        order.setFruitQuantity(Integer.parseInt((String) jsonObject.get("count")));
        return order;
    }

    private void checkWarehouse(Order order) {
        int availableFruits =
                (int) fruitList
                        .stream()
                        .filter(fruit -> fruit.getFruitType().equals(order.getFruitType()))
                        .count();
        if (availableFruits > order.getFruitQuantity()) {
            for (int i = order.getFruitQuantity(); i > 0; i--) {
                Fruit fruit = fruitList
                        .stream()
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
