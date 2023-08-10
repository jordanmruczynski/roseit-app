package pl.roseitconsulting.roseitapp.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.roseitconsulting.roseitapp.model.CustomerRegistrationRequest;
import pl.roseitconsulting.roseitapp.model.CustomerUpdateRequest;
import pl.roseitconsulting.roseitapp.dao.CustomerDao;
import pl.roseitconsulting.roseitapp.exception.DuplicateResourceException;
import pl.roseitconsulting.roseitapp.exception.ResourceNotFoundException;
import pl.roseitconsulting.roseitapp.model.Customer;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Customer with id %s does not exist", id)
                ));
    }

    public void addNewCustomer(CustomerRegistrationRequest customerRegistrationService) {
        // check if email exists
        if (customerDao.isEmailTaken(customerRegistrationService.email())) {
            throw new DuplicateResourceException("Email already taken");
        }
        // create new customer
        Customer customer = new Customer(
                customerRegistrationService.name(),
                customerRegistrationService.email(),
                customerRegistrationService.age()
        );
        // save new customer
        customerDao.insertCustomer(customer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerDao.isIdTaken(id)) {
            throw new DuplicateResourceException("Id already taken");
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(id);

        boolean changes = false;
        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.isEmailTaken(updateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (!changes) {
            throw new IllegalStateException("No changes");
        }

        customerDao.updateCustomer(customer);
    }
}
