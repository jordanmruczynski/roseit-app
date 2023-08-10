package pl.roseitconsulting.roseitapp;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import pl.roseitconsulting.roseitapp.customer.Customer;
import pl.roseitconsulting.roseitapp.customer.CustomerRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class RoseitAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoseitAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            System.out.println("Hello world!");
            Faker faker = new Faker();
            Random random = ThreadLocalRandom.current();
            Customer customer = new Customer(
                    faker.name().firstName(),
                    faker.internet().emailAddress(),
                    random.nextInt(16, 80)
            );
            customerRepository.save(customer);
        };
    }
}
