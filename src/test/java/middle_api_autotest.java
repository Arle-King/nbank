import org.example.generators.RandomData;
import org.example.models.users.CreateUserRequestDTO;
import org.example.models.users.CreateUserResponseDTO;
import org.example.models.users.UserRole;
import org.example.requests.AdminCreateUserRequest;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.Test;

public class middle_api_autotest {

    @Test
    public void adminAuthTest() {
        CreateUserRequestDTO userRequestDTO = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();

        CreateUserResponseDTO createUserResponseDTO = new AdminCreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequestDTO)
                .extract()
                .as(CreateUserResponseDTO.class);

        System.out.println(createUserResponseDTO);

    }
}
