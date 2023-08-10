package pl.roseitconsulting.roseitapp.dao;

import org.springframework.stereotype.Repository;
import pl.roseitconsulting.roseitapp.model.Customer;
import pl.roseitconsulting.roseitapp.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
         return customerRepository.findById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean isIdTaken(Integer id) {
        return customerRepository.existsById(id);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
