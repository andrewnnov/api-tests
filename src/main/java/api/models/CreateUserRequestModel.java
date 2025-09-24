package api.models;

import api.configs.Config;
import api.generators.GeneratingRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestModel extends BaseModel {
    @GeneratingRule(regex = "^[A-Za-z0-9]{3,15}$")
    private String username;
    @GeneratingRule(regex = "^[A-Z]{3}[a-z]{3}\\d{3}!@$")
    private String password;
    @GeneratingRule(regex = "^USER$")
    private String role;

    public static CreateUserRequestModel getAdmin() {
        return CreateUserRequestModel.builder()
                .username(Config.getProperty("admin.username"))
                .password(Config.getProperty("admin.password")).build();
    }
}
