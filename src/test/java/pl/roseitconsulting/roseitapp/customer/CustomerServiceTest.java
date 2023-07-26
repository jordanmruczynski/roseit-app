package pl.roseitconsulting.roseitapp.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.roseitconsulting.roseitapp.exception.DuplicateResourceException;
import pl.roseitconsulting.roseitapp.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void itShouldGetAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void itShouldGetCustomer() {
        // Given
        int id = 10;
        Customer customer = new Customer("Imie", "adresemail@domena.pl", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomer(id);
        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void itShouldThrowExceptionWhenGetCustomer() {
        // Given
        int id = 10;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("Customer with id %s does not exist", id));
    }



    @Test
    void itShouldAddNewCustomer() {
        // Given
        String email = "imie@gmail.com";

        when(customerDao.isEmailTaken(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        // When
        underTest.addNewCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        assertThat(customerArgumentCaptor.getValue().getId()).isNull();
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(request.name());
        assertThat(customerArgumentCaptor.getValue().getEmail()).isEqualTo(request.email());
        assertThat(customerArgumentCaptor.getValue().getAge()).isEqualTo(request.age());
    }

    @Test
    void itShouldThrowExceptionWhenAddNewCustomer() {
        // Given
        String email = "imie@gmail.com";

        when(customerDao.isEmailTaken(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Alex", email, 19);

        // When
        assertThatThrownBy(() -> underTest.addNewCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already taken");

        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void itShouldDeleteCustomer() {
        // Given
        int id = 1;

        when(customerDao.isIdTaken(id)).thenReturn(false);
        // When
        underTest.deleteCustomer(id);
        // Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void itShouldThrowExceptionDeleteCustomer() {
        // Given
        int id = 1;

        when(customerDao.isIdTaken(id)).thenReturn(true);
        // When
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Id already taken");
        // Then
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        int id = 10;
        Customer customer = new Customer("Imie", "adresemail@domena.pl", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String email = "newemail@domena.pl";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("NewName", email, 20);

        when(customerDao.isEmailTaken(email)).thenReturn(false);
        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        assertThat(customerArgumentCaptor.getValue().getAge()).isEqualTo(updateRequest.age());
        assertThat(customerArgumentCaptor.getValue().getEmail()).isEqualTo(updateRequest.email());
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void itShouldThrowExceptionUpdateCustomer() {
        // Given
        int id = 10;
        Customer customer = new Customer("Imie", "adresemail@domena.pl", 25);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String email = "newemail@domena.pl";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("NewName", email, 20);

        when(customerDao.isEmailTaken(email)).thenReturn(true);
        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already taken");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void itShouldThrowExceptionChangesUpdateCustomer() {
        // Given
        int id = 10;
        int age = 25;
        String imie = "Imie";
        String email = "adresemail@domena.pl";

        Customer customer = new Customer(imie, email, age);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(imie, email, age);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(IllegalStateException.class);

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }
}