package ro.mpp2024;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.io.File;

public class MainBD {
    public static void main(String[] args) {
        Properties props=new Properties();
        File file = new File("bd.config");
        try {
            props.load(new FileReader(file));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        CarRepository carRepo=new CarsDBRepository(props);
        carRepo.add(new Car("Tesla","Model S", 2019));
        System.out.println("Toate masinile din db");
        for(Car car:carRepo.findAll())
            System.out.println(car);

        String manufacturer="Tesla";
        System.out.println("Masinile produse de "+manufacturer);
        for(Car car:carRepo.findByManufacturer(manufacturer))
            System.out.println(car);

        Car updatedCar = new Car("Cineva","Nu avem", 1954);
        carRepo.update(1, updatedCar);
        System.out.println("Toate masinile din db");
        for(Car car:carRepo.findAll())
            System.out.println(car);

    }
}
