package pl.roseitconsulting.roseitapp.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.roseitconsulting.roseitapp.dao.CustomerRowMapper;
import pl.roseitconsulting.roseitapp.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CustomerRowMapperTest {

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private CustomerRowMapper customerRowMapper;

    @Test
    public void testMapRow() throws SQLException {
        // Given
        int customerId = 1;
        String customerName = "John Doe";
        String customerEmail = "john.doe@example.com";
        int customerAge = 30;

        when(resultSet.getInt("id")).thenReturn(customerId);
        when(resultSet.getString("name")).thenReturn(customerName);
        when(resultSet.getString("email")).thenReturn(customerEmail);
        when(resultSet.getInt("age")).thenReturn(customerAge);

        // When
        Customer customer = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(1, customerName, customerEmail, customerAge);
        assertThat(customer).isEqualTo(expected);
    }
}
