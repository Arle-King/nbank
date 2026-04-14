package senior_autotest;

import org.assertj.core.api.SoftAssertions;
import org.example.BankWidget;
import org.example.generators.RandomModelGenerator;
import org.example.models.admin.users.CreateUserResponseDTO;
import org.example.models.comparison.ModelAssertions;
import org.example.models.customer.profile.UpdateProfileRequestDTO;
import org.example.models.customer.profile.UpdateProfileResponseDTO;
import org.example.requests.skelethon.enams.Endpoint;
import org.example.requests.skelethon.requests.CrudRequest;
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

    CreateUserResponseDTO user;

    @BeforeEach
    public void precondition() {
        softAssertions = new SoftAssertions();

        user = BankWidget.createUser();

        softAssertions.assertThat(user.getName()).as("Name не соответствует ожидаемому").isEqualTo(null);
    }

    @AfterEach
    public void postcondition() {
        BankWidget.deleteUser(user);
        softAssertions.assertAll();
    }

    @Test
    public void positiveTest() {
        UpdateProfileRequestDTO updateProfileRequestDTO = RandomModelGenerator.generate(UpdateProfileRequestDTO.class);

        UpdateProfileResponseDTO userResponseDTO = new ValidatedCrudRequest<UpdateProfileResponseDTO>(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
                Endpoint.UPDATE_PROFILE,
                ResponceSpecs.requestReturnsOk())
                .update(updateProfileRequestDTO);

        ModelAssertions.assertThatModels(updateProfileRequestDTO, userResponseDTO).match();
    }

    @MethodSource("provaderNegativeCutomerName")
    @ParameterizedTest
    public void negativeTest(String newName) {
        UpdateProfileRequestDTO updateProfileRequestDTO = UpdateProfileRequestDTO.builder()
                .name(newName)
                .build();

        new CrudRequest(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
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
