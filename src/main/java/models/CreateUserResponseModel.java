package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponseModel extends BaseModel {
    private long id;
    private String username;
    private String password;
    private String name;
    private String role;
    private List<String> accounts;
}
