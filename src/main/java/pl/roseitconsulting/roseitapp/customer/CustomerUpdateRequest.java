package pl.roseitconsulting.roseitapp.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age) {
}
