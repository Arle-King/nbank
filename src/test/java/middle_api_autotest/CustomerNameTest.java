package middle_api_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.BankWidget;
import org.example.UserRole;
import org.example.admin.models.CreateUserRequestDTO;
import org.example.admin.requests.CreateUserRequest;
import org.example.customer.models.profile.UpdateProfileRequestDTO;
import org.example.customer.requests.profile.UpdateProfileRequest;
import org.example.general_models.UserResponseDTO;
import org.example.generators.RandomData;
import org.example.specs.RequestSpecs;
import org.example.specs.ResponceSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CustomerNameTest {

    SoftAssertions softAssertions;

    CreateUserRequestDTO userRequest;
    UserResponseDTO user;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();
        userRequest = CreateUserRequestDTO.builder()
                .userName(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(UserRole.USER)
                .build();

        user = BankWidget.getUresById(new CreateUserRequest(
                RequestSpecs.getAdminSpec(),
                ResponceSpecs.entityWasCreated())
                .post(userRequest)
                .extract()
                .as(UserResponseDTO.class).getId());

        softAssertions.assertThat(user.getName()).as("Name не соответствует ожидаемому").isEqualTo(null);
    }

    @AfterEach
    public void postcondition() {
        softAssertions.assertAll();
    }

    @Test
    public void positiveTest() {
        String newName = "new name";
        UpdateProfileRequestDTO updateProfileRequestDTO = UpdateProfileRequestDTO.builder()
                .name(newName)
                .build();

        new UpdateProfileRequest(
                RequestSpecs.getUserSpec(userRequest.getUserName(), userRequest.getPassword()),
                ResponceSpecs.requestReturnsOk())
                .update(updateProfileRequestDTO);

        softAssertions.assertThat(BankWidget.getUresById(user.getId()).getName()).as("Name не соответствует ожидаемому").isEqualTo(newName);
    }

    @MethodSource("provaderNegativeCutomerName")
    @ParameterizedTest
    public void negativeTest(String newName) {
        UpdateProfileRequestDTO updateProfileRequestDTO = UpdateProfileRequestDTO.builder()
                .name(newName)
                .build();

        new UpdateProfileRequest(
                RequestSpecs.getUserSpec(userRequest.getUserName(), userRequest.getPassword()),
                ResponceSpecs.requestReturnsUnprecessableEntity())
                .update(updateProfileRequestDTO);

        softAssertions.assertThat(BankWidget.getUresById(user.getId()).getName()).isEqualTo("Name не соответствует ожидаемому").isEqualTo(newName);
    }

    public static Stream<Arguments> provaderNegativeCutomerName() {
        return Stream.of(
                Arguments.of( "   ", 422),
                Arguments.of( "", 422),
                Arguments.of("New", 422),
                Arguments.of("New New Name", 422),
                Arguments.of("New Name!", 422),
                Arguments.of("New Name123", 422)
        );
    }
}
