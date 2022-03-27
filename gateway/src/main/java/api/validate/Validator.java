package api.validate;

public interface Validator<T> {
    ValidationResult validate(T data);
}
