package pl.roseitconsulting.roseitapp.journey;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.roseitconsulting.roseitapp.customer.Customer;
import pl.roseitconsulting.roseitapp.customer.CustomerRegistrationRequest;
import pl.roseitconsulting.roseitapp.customer.CustomerUpdateRequest;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private Faker faker = new Faker();
    private static final String URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        // create customer registration request
        String name = faker.name().fullName();;
        String email = faker.name().lastName() + UUID.randomUUID() + "@gmail.com";
        int age = ThreadLocalRandom.current().nextInt(1, 100);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerIntegrationTest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure customer is present
        Customer expectedCustomer = new Customer(name, email, age);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        // get customer by id
        webTestClient.get()
                .uri(URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteACustomer() {
        // create customer registration request
        String name = faker.name().fullName();;
        String email = faker.name().lastName() + UUID.randomUUID() + "@gmail.com";
        int age = ThreadLocalRandom.current().nextInt(1, 100);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerIntegrationTest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer by id
        webTestClient.delete()
                .uri(URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        webTestClient.get()
                .uri(URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
    @Test
    void canUpdateCustomer() {
        // create customer registration request
        String name = faker.name().fullName();;
        String email = faker.name().lastName() + UUID.randomUUID() + "@gmail.com";
        int age = ThreadLocalRandom.current().nextInt(1, 100);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(name, email, age);

        // send a post request
        webTestClient.post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerIntegrationTest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customer
        List<Customer> allCustomers = webTestClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        var id = allCustomers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = "NewName";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(newName, null, null);

        // update customer by id
        webTestClient.put()
                .uri(URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerUpdateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expected = new Customer(id, newName, email, age);

        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
