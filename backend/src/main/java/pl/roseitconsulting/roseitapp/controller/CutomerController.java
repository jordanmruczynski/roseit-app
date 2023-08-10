package pl.roseitconsulting.roseitapp.controller;

import org.springframework.web.bind.annotation.*;
import pl.roseitconsulting.roseitapp.model.CustomerRegistrationRequest;
import pl.roseitconsulting.roseitapp.services.CustomerService;
import pl.roseitconsulting.roseitapp.model.CustomerUpdateRequest;
import pl.roseitconsulting.roseitapp.model.Customer;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CutomerController {

    private final CustomerService customerService;

    public CutomerController(CustomerService customers) {
        this.customerService = customers;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> getCustomerService() {
        return customerService.getAllCustomers();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Customer getCustomer(@PathVariable("id") Integer id) {
        return customerService.getCustomer(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addNewCustomer(request);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public void updateCustomer(@PathVariable("id") Integer id, @RequestBody CustomerUpdateRequest request) {
        customerService.updateCustomer(id, request);
    }
}
