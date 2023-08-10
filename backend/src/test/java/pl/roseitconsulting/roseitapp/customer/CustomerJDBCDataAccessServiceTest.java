package pl.roseitconsulting.roseitapp.customer;

import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.roseitconsulting.roseitapp.AbstractTestcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(getJdbcTemplate(), customerRowMapper);
    }

    @Test
    void itShouldSelectAllCustomers() {
        // Given
        Customer customer = new Customer(FAKER.name().firstName(), FAKER.internet().emailAddress(), 20);
        // When
        underTest.insertCustomer(customer);
        // Then
        assertThat(underTest.selectAllCustomers()).isNotEmpty();
    }

    @Test
    void itShouldSelectCustomerById() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(underTest.selectCustomerById(id)).isPresent().hasValueSatisfying(c -> {
           assertThat(c.getId()).isEqualTo(c.getId());
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void itShouldReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;

        // When
        var actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();

    }

    @Test
    void itShouldReturnTrueWhenEmailTaken() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);
        // When
        // Then
        assertThat(underTest.isEmailTaken(email)).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenEmailIsNotTaken() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);
        // When
        // Then
        assertThat(underTest.isEmailTaken(FAKER.internet().emailAddress())).isFalse();
    }

    @Test
    void itShouldReturnTrueWhenIdTaken() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        // Then
        assertThat(underTest.isIdTaken(id)).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenIdTaken() {
        assertThat(underTest.isIdTaken(-1)).isFalse();
    }

    @Test
    void itShouldDeleteCustomerById() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);
        // When
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        underTest.deleteCustomerById(id);
        // Then
        assertThat(underTest.selectCustomerById(id)).isNotPresent();
    }

    @Test
    void itShouldUpdateCustomerAge() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        int updateAge = 25;
        Customer customerToUpdate = new Customer(null, null, updateAge);
        underTest.updateCustomer(customerToUpdate);
        // Then
        assertThat(underTest.selectCustomerById(id)).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getAge().equals(updateAge));
        });
    }

    @Test
    void itShouldUpdateCustomerName() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        String newName = "AnyName";
        Customer customerToUpdate = new Customer(newName, null, null);
        underTest.updateCustomer(customerToUpdate);
        // Then
        assertThat(underTest.selectCustomerById(id)).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getName().equals(newName));
        });
    }

    @Test
    void itShouldUpdateCustomerEmail() {
        // Given
        String email = FAKER.internet().emailAddress();
        Customer customer = new Customer(FAKER.name().firstName(), email, 20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        String newEmail = "email@com.pl";
        Customer customerToUpdate = new Customer(null, newEmail, null);
        underTest.updateCustomer(customerToUpdate);
        // Then
        assertThat(underTest.selectCustomerById(id)).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getEmail().equals(newEmail));
        });
    }
}