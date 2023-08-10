package pl.roseitconsulting.roseitapp.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.roseitconsulting.roseitapp.dao.CustomerJPADataAccessService;
import pl.roseitconsulting.roseitapp.model.Customer;
import pl.roseitconsulting.roseitapp.repository.CustomerRepository;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
     //   autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void itShouldSelectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void itShouldSelectCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void itShouldInsertCustomer() {
        // Given
        Customer customer = new Customer("Imie", "adresemail@domena.pl", 25);
        // When
        underTest.insertCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void itShouldIsEmailTaken() {
        // Given
        String email = "adresemail@domena.pl";
        // When
        underTest.isEmailTaken(email);
        // Then
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    void itShouldIsIdTaken() {
        // Given
        int id = 1;
        // When
        underTest.isIdTaken(id);
        // Then
        verify(customerRepository).existsById(id);
    }

    @Test
    void itShouldDeleteCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.deleteCustomerById(id);
        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        Customer customer = new Customer("Imie", "adresemail@domena.pl", 25);
        // When
        underTest.updateCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }
}