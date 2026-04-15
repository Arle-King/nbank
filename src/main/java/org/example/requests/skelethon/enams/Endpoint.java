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
import org.example.models.accoints.transactions.TransactionsRequestDTO;
import org.example.models.accoints.transfer.TransferRequestDTO;
import org.example.models.accoints.transfer.TransferResponseDTO;
import org.example.models.admin.users.*;
import org.example.models.authentication.login.LoginRequestDTO;
import org.example.models.authentication.login.LoginResponseDTO;
import org.example.models.customer.accounts.GetCustomerAccount;
import org.example.models.customer.profile.GetCustomerProfileRequestDTO;
import org.example.models.customer.profile.GetCustomerProfileResponseDTO;
import org.example.models.customer.profile.UpdateProfileRequestDTO;
import org.example.models.customer.profile.UpdateProfileResponseDTO;

@Getter
@AllArgsConstructor
public enum Endpoint {
    //Accounts
    CREATE_ACCOUNT(
            "/accounts",
            CreateAccountRequestDTO.class,
            CreateAccountResponseDTO.class
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

    GET_TRANSACTIONS(
            "/accounts/id#1/transactions",
            TransactionsRequestDTO.class,
            TransferResponseDTO.class
    ),

    DELETE_ACCOUNT(
            "/accounts/id#1",
            DeleteAccountRequestDTO.class,
            DeleteAccountResponseDTO.class
    ),

    //Admin
    GET_ALL_USERS(
            "/admin/users",
            AllUsersRequestDTO.class,
            AllUsersResponseDTO.class
    ),

    CREATE_USER(
            "/admin/users",
            CreateUserRequestDTO.class,
            CreateUserResponseDTO.class
    ),

    DELETE_USER(
            "/admin/users/id#1",
            DeleteUserRequestDTO.class,
            DeleteUserResponseDTO.class
    ),

    //Authentication
    LOGIN(
            "/auth/login",
            LoginRequestDTO.class,
            LoginResponseDTO.class
    ),

    //Customer
    UPDATE_PROFILE(
            "/customer/profile",
            UpdateProfileRequestDTO.class,
            UpdateProfileResponseDTO.class
    ),

    GET_PROFILE(
            "/profile",
            GetCustomerProfileRequestDTO.class,
            GetCustomerProfileResponseDTO.class
    ),

    GET_CUSTOMER_ACCOUNTS(
          "/customer/accounts",
            null,
            GetCustomerAccount.class
    );

    private final String endpoint;
    private final Class<? extends BaseModel> requestModel;
    private final Class<? extends BaseModel> responseModel;
}
