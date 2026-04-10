package senior_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.BankWidget;
import org.example.generators.RandomData;
import org.example.models.admin.users.CreateUserRequestDTO;
import org.example.models.admin.users.CreateUserResponseDTO;
import org.example.models.customer.profile.UpdateProfileRequestDTO;
import org.example.requests.skelethon.requests.CrudRequest;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.enams.Role;
import org.example.requests.skelethon.requests.ValidatedCrudRequest;
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
    CreateUserResponseDTO user;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();
        userRequest = CreateUserRequestDTO.builder()
                .username(RandomData.getRandomUserName())
                .password(RandomData.getRandomPassword())
                .role(Role.USER)
                .build();

        user = BankWidget.getUresById(new ValidatedCrudRequest<CreateUserResponseDTO>(
                RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(userRequest).getId());

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

        new CrudRequest(
                RequestSpecs.getUserSpec(userRequest.getUsername(), userRequest.getPassword()),
                Endpoint.UPDATE_PROFILE,
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

        new CrudRequest(
                RequestSpecs.getUserSpec(userRequest.getUsername(), userRequest.getPassword()),
                Endpoint.UPDATE_PROFILE,
                ResponceSpecs.requestReturnsBadRequest())
                .update(updateProfileRequestDTO);

        softAssertions.assertThat(BankWidget.getUresById(user.getId()).getName()).as("Name не соответствует ожидаемому").isEqualTo(user.getName());
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
