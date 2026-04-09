import org.example.generators.RandomData;
import org.example.admin.models.CreateUserRequestDTO;
import org.example.general_models.UserResponseDTO;
import org.example.UserRole;
import org.example.admin.requests.CreateUserRequest;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateUserTest {

    CreateUserRequestDTO userRequestDTO;
    UserResponseDTO userResponseDTO;

    @BeforeEach
    public void precondition() {
        userRequestDTO = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();
    }

    @Test
    public void test() {
        userResponseDTO = new CreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequestDTO)
                .extract()
                .as(UserResponseDTO.class);
    }
}
