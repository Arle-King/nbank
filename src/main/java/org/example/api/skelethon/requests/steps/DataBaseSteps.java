package org.example.api.skelethon.requests.steps;

import org.example.api.configs.Config;
import org.example.api.dao.AccountDAO;
import org.example.api.dao.UserDAO;
import org.example.api.database.Condition;
import org.example.api.database.DBRequest;
import org.example.common.helpers.StepLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseSteps {

    public static UserDAO getUserByUsername(String username) {
        return StepLogger.log("Get user from database by username: " + username, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table("customers")
                    .where(Condition.equalTo("username", username))
                    .extractAs(UserDAO.class);
        });
    }

    public static UserDAO getUserById(Long id) {
        return StepLogger.log("Get user from database by ID: " + id, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table("customers")
                    .where(Condition.equalTo("id", id))
                    .extractAs(UserDAO.class);
        });
    }

    public static UserDAO getUserByRole(String role) {
        return StepLogger.log("Get user from database by role: " + role, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table("customers")
                    .where(Condition.equalTo("role", role))
                    .extractAs(UserDAO.class);
        });
    }

    public static AccountDAO getAccountByAccountNumber(String accountNumber) {
        return StepLogger.log("Get account from database by account number: " + accountNumber, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table("accounts")
                    .where(Condition.equalTo("account_number", accountNumber))
                    .extractAs(AccountDAO.class);
        });
    }

    public static AccountDAO getAccountById(Long id) {
        return StepLogger.log("Get account from database by ID: " + id, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table("accounts")
                    .where(Condition.equalTo("id", id))
                    .extractAs(AccountDAO.class);
        });
    }

    public static AccountDAO getAccountByCustomerId(Long customerId) {
        return StepLogger.log("Get account from database by customer ID: " + customerId, () -> {
            return DBRequest.builder()
                    .requestType(DBRequest.RequestType.SELECT)
                    .table("customers")
                    .where(Condition.equalTo("customer_id", customerId))
                    .extractAs(AccountDAO.class);
        });
    }

    public static void updateAccountBalance(Long accountId, Double newBalance) {
        StepLogger.log("Update account balance in database for account ID: " + accountId + " to: " + newBalance, () -> {
            try (Connection connection = DriverManager.getConnection(
                    Config.getProperty("db.url"),
                    Config.getProperty("db.username"),
                    Config.getProperty("db.password"))) {

                String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDouble(1, newBalance);
                    statement.setLong(2, accountId);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new RuntimeException("No account found with ID: " + accountId);
                    }

                    return rowsAffected;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to update account balance", e);
            }
        });
    }
}
