package org.example.api.skelethon.requests.steps;

import org.example.BankWidget;
import org.example.api.generators.RandomModelGenerator;
import org.example.api.models.admin.users.CreateUserRequestDTO;
import org.example.api.models.admin.users.CreateUserResponseDTO;
import org.example.api.skelethon.enams.Endpoint;
import org.example.api.skelethon.requests.ValidatedCrudRequest;
import org.example.api.specs.RequestSpecs;
import org.example.api.specs.ResponceSpecs;

import java.util.List;

public class AdminSteps {
    public static CreateUserRequestDTO createUser() {
        CreateUserRequestDTO userRequest = RandomModelGenerator.generate(CreateUserRequestDTO.class);
        new ValidatedCrudRequest<CreateUserResponseDTO>(
                RequestSpecs.getAdminSpec(),
                Endpoint.CREATE_USER,
                ResponceSpecs.entityWasCreated())
                .post(userRequest);

        RequestSpecs.getUserSpec(userRequest.getUsername(), userRequest.getPassword());

        return userRequest;
    }

    public static List<CreateUserResponseDTO> getAllUsers() {
        return BankWidget.getAllUsers();
    }

    public static void deleteUser(CreateUserRequestDTO user) {
        BankWidget.deleteUser(user.getUsername());
    }
}
