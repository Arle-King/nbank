import io.restassured.response.Response;
import org.example.UserRole;
import org.example.admin.models.CreateUserRequestDTO;
import org.example.general_models.UserResponseDTO;
import org.example.admin.requests.CreateUserRequest;
import org.example.authentication.models.LoginRequestDTO;
import org.example.authentication.models.LoginResponseDTO;
import org.example.authentication.requests.LoginRequest;
import org.example.generators.RandomData;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginRequestTest {

    CreateUserRequestDTO userRequestDTO;
    UserResponseDTO userResponseDTO;
    LoginRequestDTO userLogin;
    Response response;
    LoginResponseDTO user;

    @BeforeEach
    public void precondition() {
        userRequestDTO = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();

        userResponseDTO = new CreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequestDTO)
                .extract()
                .as(UserResponseDTO.class);

        userLogin = LoginRequestDTO.builder()
                .userName(userRequestDTO.getUserName())
                .password(userRequestDTO.getPassword())
                .build();
    }

    @Test
    public void testLogin() {
        response = new LoginRequest(
                RequestSpecs.getUnAuthSpec(),
                ResponceSpecs.requestReturnsOk())
                .post(userLogin)
                .extract()
                .response();

        user = response.as(LoginResponseDTO.class);
        user.setToken(response.header("Authorization"));
    }
}
