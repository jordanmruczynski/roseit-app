package pl.roseitconsulting.roseitapp.model;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age) {
}
