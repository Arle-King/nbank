package api.senior_autotest;

import api.BaseTest;
import org.example.BankWidget;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.admin.users.CreateUserResponseDTO;
import org.example.api.models.comparison.ModelAssertions;
import org.example.api.models.customer.profile.UpdateProfileRequestDTO;
import org.example.api.models.customer.profile.UpdateProfileResponseDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.CrudRequest;
import org.example.api.skelethon.requests.ValidatedCrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;
import org.example.common.annotations.ApiVersion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class CustomerNameTest extends BaseTest {
    CreateUserResponseDTO user;

    final static String beginName = null;

    @BeforeEach
    public void precondition() {
        super.precondition();
        user = BankWidget.createUser();

        softAssertions.assertThat(user.getName()).as("Name не соответствует ожидаемому").isEqualTo(beginName);
    }

    @AfterEach
    public void postcondition() {
        BankWidget.deleteUser(user);
    }

    @ApiVersion("with_deletion")
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

    @ApiVersion("with_deletion")
    @MethodSource("provaderNegativeCutomerName")
    @ParameterizedTest
    public void negativeTest(String newName, String errorKey, String errorValue) {
        UpdateProfileRequestDTO updateProfileRequestDTO = UpdateProfileRequestDTO.builder()
                .name(newName)
                .build();

        new CrudRequest(
                RequestSpecs.getUserSpec(user.getUsername(), user.getPassword()),
                Endpoint.UPDATE_PROFILE,
                ResponceSpecs.requestReturnsBadRequest(errorKey, errorValue))
                .update(updateProfileRequestDTO);

        softAssertions.assertThat(BankWidget.getUresById(user.getId()).getName()).as("Name не соответствует ожидаемому").isEqualTo(user.getName());
    }

    public static Stream<Arguments> provaderNegativeCutomerName() {
        return Stream.of(
                Arguments.of( "   ", 422, "name", "Name must contain two words with letters only"),
                Arguments.of( "", 422, "name", "Name must contain two words with letters only"),
                Arguments.of("New", 422, "name", "Name must contain two words with letters only"),
                Arguments.of("New New Name", 422, "name", "Name must contain two words with letters only"),
                Arguments.of("New Name!", 422, "name", "Name must contain two words with letters only"),
                Arguments.of("New Name123", 422, "name", "Name must contain two words with letters only")
        );
    }
}
