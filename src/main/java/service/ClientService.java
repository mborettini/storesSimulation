package service;

import entity.Client;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static service.ClientOrderService.*;

public class ClientService {

    private static final Random r = new Random();

    private static final ArrayList<String> names = new ArrayList<>(List.of("Liam", "Noah", "Oliver", "Elijah", "William", "James",
            "Benjamin", "Lucas", "Henry", "Alexander", "Mason", "Michael", "Ethan", "Daniel", "Jacob", "Logan", "Jackson",
            "Levi", "Sebastian", "Mateo", "Jack", "Owen", "Theodore", "Aiden", "Samuel", "Joseph", "John", "David", "Wyatt",
            "Matthew", "Luke", "Asher", "Carter", "Julian", "Grayson", "Olivia", "Emma", "Ava", "Charlotte", "Sophia",
            "Amelia", "Isabella", "Mia", "Evelyn", "Harper", "Camila", "Gianna", "Abigail", "Luna", "Ella", "Elizabeth",
            "Sofia", "Emily", "Avery", "Mila", "Scarlett", "Eleanor", "Madison", "Layla", "Penelope", "Aria", "Chloe",
            "Grace", "Ellie", "Nora", "Hazel", "Zoey", "Riley", "Victoria", "Lily", "Aurora", "Violet", "Nova", "Hannah",
            "Emilia", "Zoe", "Stella", "Everly", "Isla", "Leah", "Lillian", "Addison", "Willow", "Lucy", "Paisley",
            "Natalie", "Naomi", "Eliana", "Brooklyn", "Elena", "Aubrey", "Claire", "Ivy", "Kinsley", "Audrey"));

    private static final ArrayList<String> surnames = new ArrayList<>(List.of("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia",
            "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzales", "Wilson", "Anderson", "Thomas",
            "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark",
            "Ramirez", "Lewis", "Robinson", "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen",
            "Hill", "Flores", "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter",
            "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
            "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson",
            "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson"));

    private static final ArrayList<String> cities = new ArrayList<>(List.of("Chicago", "Los Angeles", "New York"));

    private static final ArrayList<String> streets = new ArrayList<>(List.of("Second", "Third", "First", "Park", "Main"));

    public static ArrayList<Client> generateClients() {
        ArrayList<Client> clients = new ArrayList<>(1000);

        Map<Long, Double> shoppingEvaluation = Stream.of(new Object[][]{
                {1L, 0.0},
                {2L, 0.0},
                {3L, 0.0},
        }).collect(Collectors.toMap(data -> (Long) data[0], data -> (Double) data[1]));

        for (int i = 1; i < 1001; i++) {
            String name = names.get(r.nextInt(surnames.size()));
            String surname = surnames.get(r.nextInt(surnames.size()));
            Long phone = (long) (100000000 + r.nextInt(90000000));
            String city = cities.get(r.nextInt(cities.size()));
            String street = streets.get(r.nextInt(streets.size()));
            double myG;
            do {
                myG = r.nextGaussian() * 20 + 65;
            } while (myG < 20.0 || myG > 100.0);

            clients.add(
                    new Client(
                            (long) i,
                            name,
                            surname,
                            (int) myG,
                            generateShoppingDay(myG, 0),
                            determineProductForClient(myG),
                            generateNumberOfProductsToBuy(myG),
                            (long) generateStoreForShopping(),
                            0,
                            shoppingEvaluation,
                            name + "." + surname + "@gmail.com",
                            phone,
                            city,
                            street
                    ));
        }
        return clients;
    }
}
