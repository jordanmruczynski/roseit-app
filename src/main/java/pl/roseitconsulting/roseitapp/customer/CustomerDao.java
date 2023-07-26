package pl.roseitconsulting.roseitapp.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean isEmailTaken(String email);
    boolean isIdTaken(Integer id);
    void deleteCustomerById(Integer id);
    void updateCustomer(Customer customer);
}
