import java.time.Instant;
import java.util.Date;
import model.ShopWarehouse;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = "src/main/java/order/order1.json";
        ShopWarehouse warehouse = new ShopWarehouse();
        warehouse.addFruit(path);
        System.out.println(ShopWarehouse.getFruitList());
        String path2 = "src/main/java/order/order2.json";
        warehouse.addFruit(path2);
        System.out.println(ShopWarehouse.getFruitList());
        String path3 = "src/main/java/order/orders_list.json";
        warehouse.save(path3);
        System.out.println(ShopWarehouse.getFruitList());
        Instant instant = Instant.now();
        Date date = Date.from(instant);
        System.out.println(warehouse.getSpoiledFruits(date));
        System.out.println(warehouse.getAvailableFruits(date));
        System.out.println(warehouse.getSpoiledFruits(date, "BANANA"));
        System.out.println(warehouse.getAvailableFruits(date, "APPLE"));
        System.out.println(warehouse.getAddedFruits(date));
        System.out.println(warehouse.getAddedFruits(date, "BANANA"));
        String path1 = "src/main/java/order/client.json";
        System.out.println(ShopWarehouse.getFruitList());
        System.out.println(ShopWarehouse.getMoneyBalance());
        warehouse.sell(path1);
        System.out.println(ShopWarehouse.getFruitList());
        System.out.println(ShopWarehouse.getMoneyBalance());
    }
}
