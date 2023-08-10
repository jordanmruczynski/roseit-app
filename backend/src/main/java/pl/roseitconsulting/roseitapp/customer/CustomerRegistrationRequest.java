package pl.roseitconsulting.roseitapp.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age) {
}
