package pl.roseitconsulting.roseitapp.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.roseitconsulting.roseitapp.AbstractTestcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldExistsByEmail() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.save(customer);
        // When
        boolean expected = underTest.existsByEmail(email);
        // Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldReturnFalseExistsByEmail() {
        // Given
        String email = FAKER.internet().emailAddress();
        // When
        boolean expected = underTest.existsByEmail(email);
        // Then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldExistsById() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        boolean expected = underTest.existsById(id);
        // Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldReturnFalseExistsById() {
        // Given
        String email = FAKER.internet().emailAddress();

        // When
        boolean expected = underTest.existsById(-1);
        // Then
        assertThat(expected).isFalse();
    }
}