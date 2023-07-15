package pl.roseitconsulting.roseitapp.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;
    private static final String TABLE_NAME = "customer";

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = String.format("SELECT * FROM %s", TABLE_NAME);
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectAllCustomersById(Integer id) {
        var sql = String.format("SELECT * FROM %s WHERE id = ?", TABLE_NAME);
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = String.format("INSERT INTO %s (name, email, age) VALUES (?, ?, ?)", TABLE_NAME);
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
    }

    @Override
    public boolean isEmailTaken(String email) {
        var sql = String.format("SELECT EXISTS(SELECT * FROM %s WHERE email = ?)", TABLE_NAME);
        return jdbcTemplate.queryForObject(sql, Boolean.class, email);
    }

    @Override
    public boolean isIdTaken(Integer id) {
        var sql = String.format("SELECT EXISTS(SELECT * FROM %s WHERE id = ?)", TABLE_NAME);
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer updatedCustomer) {
        if (updatedCustomer.getName() != null) {
            var sql = String.format("UPDATE %s SET name = ? WHERE id = ?", TABLE_NAME);
            jdbcTemplate.update(sql, updatedCustomer.getName(), updatedCustomer.getId());
        }
        if (updatedCustomer.getEmail() != null) {
            var sql = String.format("UPDATE %s SET email = ? WHERE id = ?", TABLE_NAME);
            jdbcTemplate.update(sql, updatedCustomer.getEmail(), updatedCustomer.getId());
        }
        if (updatedCustomer.getAge() != null) {
            var sql = String.format("UPDATE %s SET age = ? WHERE id = ?", TABLE_NAME);
            jdbcTemplate.update(sql, updatedCustomer.getAge(), updatedCustomer.getId());
        }
    }
}
