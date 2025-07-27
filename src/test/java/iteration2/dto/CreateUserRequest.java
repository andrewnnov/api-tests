package iteration2.dto;

public class CreateUserRequest {
    public String username;
    public String password;
    public String role;

    public CreateUserRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
