package pl.roseitconsulting.roseitapp.model;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age) {
}
