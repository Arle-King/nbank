package org.example.requests.skelethon.enams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.models.BaseModel;
import org.example.models.accoints.accounts.CreateAccountRequestDTO;
import org.example.models.accoints.accounts.CreateAccountResponseDTO;
import org.example.models.accoints.accounts.DeleteAccountRequestDTO;
import org.example.models.accoints.accounts.DeleteAccountResponseDTO;
import org.example.models.accoints.deposit.DepositRequestDTO;
import org.example.models.accoints.deposit.DepositResponseDTO;
import org.example.models.accoints.transfer.TransferRequestDTO;
import org.example.models.accoints.transfer.TransferResponseDTO;
import org.example.models.admin.users.*;
import org.example.models.authentication.login.LoginRequestDTO;
import org.example.models.authentication.login.LoginResponseDTO;
import org.example.models.customer.profile.GetCustomerProfileRequestDTO;
import org.example.models.customer.profile.GetCustomerProfileResponseDTO;
import org.example.models.customer.profile.UpdateProfileRequestDTO;
import org.example.models.customer.profile.UpdateProfileResponseDTO;

@Getter
@AllArgsConstructor
public enum Endpoint {

    CREATE_USER(
            "/admin/users",
            CreateUserRequestDTO.class,
            CreateUserResponseDTO.class
    ),

    DELETE_USER(
            "/admin/users",
            DeleteUserRequestDTO.class,
            DeleteUserResponseDTO.class
    ),

    GET_ALL_USERS(
            "/admin/users",
            AllUsersRequestDTO.class,
            CreateUserResponseDTO.class
    ),

    CREATE_ACCOUNT(
            "/accounts",
            CreateAccountRequestDTO.class,
            CreateAccountResponseDTO.class
    ),

    DELETE_ACCOUNT(
            "/accounts",
            DeleteAccountRequestDTO.class,
            DeleteAccountResponseDTO.class
    ),

    TRANSFER(
            "/accounts/transfer",
            TransferRequestDTO.class,
            TransferResponseDTO.class
    ),

    DEPOSIT(
            "/accounts/deposit",
            DepositRequestDTO.class,
            DepositResponseDTO.class
    ),

    LOGIN(
            "/auth/login",
            LoginRequestDTO.class,
            LoginResponseDTO.class
    ),

    UPDATE_PROFILE(
            "/customer/profile",
            UpdateProfileRequestDTO.class,
            UpdateProfileResponseDTO.class
    ),

    GET_PROFILE(
            "/profile",
            GetCustomerProfileRequestDTO.class,
            GetCustomerProfileResponseDTO.class
    );

    private final String endpoint;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
