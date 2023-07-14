package pl.roseitconsulting.roseitapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import pl.roseitconsulting.roseitapp.customer.Customer;
import pl.roseitconsulting.roseitapp.customer.CustomerRepository;

@SpringBootApplication
public class RoseitAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoseitAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            System.out.println("Hello world!");
            Customer alex = new Customer(
                    "Alex", "alex@gmail.com", 21
            );
            Customer jamila = new Customer(
                    "Jamila", "jamila@gmail.com", 25
            );
            customerRepository.save(alex);
            customerRepository.save(jamila);
        };
    }
}
