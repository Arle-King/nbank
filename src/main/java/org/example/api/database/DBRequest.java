package org.example.api.database;

import lombok.Builder;
import lombok.Data;
import org.example.api.configs.Config;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DBRequest {
    private RequestType requestType;
    private String table;
    private List<Condition> conditions;
    private Class<?> extractAsClass;

    public enum RequestType {
        SELECT, INSERT, UPDATE, DELETE
    }

    public <T> T extractAs(Class<T> clazz) {
        this.extractAsClass = clazz;
        return executeQuery(clazz);
    }

    private <T> T executeQuery(Class<T> clazz) {
        String sql = buildSQL();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (conditions != null) {
                for (int i = 0; i < conditions.size(); i++) {
                    statement.setObject(i + 1, conditions.get(i).getValue());
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapToEntity(resultSet, clazz);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }
    }

    private <T> T mapToEntity(ResultSet resultSet, Class<T> clazz) throws SQLException {
        if (!resultSet.next()) {
            return null; // или Optional.empty()
        }

        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                String columnName = toSnakeCase(field.getName()); // преобразуем camelCase в snake_case
                Object value = resultSet.getObject(columnName);

                if (value != null) {
                    field.setAccessible(true);
                    field.set(instance, convertIfNeeded(value, field.getType()));
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Reflection mapping failed for " + clazz.getSimpleName(), e);
        }
    }

    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private Object convertIfNeeded(Object value, Class<?> targetType) {
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }
        // Например, из Long в long или из BigDecimal в Double
        if (targetType == double.class || targetType == Double.class) {
            return ((Number) value).doubleValue();
        }
        if (targetType == long.class || targetType == Long.class) {
            return ((Number) value).longValue();
        }
        // можно добавить другие преобразования
        return value;
    }

    /*
    private UserDAO mapToUserDao(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return UserDAO.builder()
                    .id(resultSet.getLong("id"))
                    .username(resultSet.getString("username"))
                    .password(resultSet.getString("password"))
                    .role(resultSet.getString("role"))
                    .name(resultSet.getString("name"))
                    .build();
        }
        return null;
    }

    private AccountDAO mapToAccountDao(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return AccountDAO.builder()
                    .id(resultSet.getLong("id"))
                    .accountNumber(resultSet.getString("account_number"))
                    .balance(resultSet.getDouble("balance"))
                    .customerId(resultSet.getLong("customer_id"))
                    .build();
        }
        return null;
    }

     */

    private String buildSQL() {
        StringBuilder sql = new StringBuilder();

        switch (requestType) {
            case SELECT:
                sql.append("SELECT * FROM ").append(table);
                if (conditions != null && !conditions.isEmpty()) {
                    sql.append(" WHERE ");
                    for (int i = 0; i < conditions.size(); i++) {
                        if (i > 0) sql.append(" AND ");
                        sql.append(conditions.get(i).getColumn()).append(" ").append(conditions.get(i).getOperator()).append(" ?");
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Request type " + requestType + " not implemented");
        }

        return sql.toString();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                Config.getProperty("db.url"),
                Config.getProperty("db.username"),
                Config.getProperty("db.password")
        );
    }

    public static DBRequestBuilder builder() {
        return new DBRequestBuilder();
    }

    public static class DBRequestBuilder {
        private RequestType requestType;
        private String table;
        private List<Condition> conditions = new ArrayList<>();
        private Class<?> extractAsClass;

        public DBRequestBuilder requestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        public DBRequestBuilder where(Condition condition) {
            this.conditions.add(condition);
            return this;
        }

        public DBRequestBuilder table(String table) {
            this.table = table;
            return this;
        }

        public <T> T extractAs(Class<T> clazz) {
            this.extractAsClass = clazz;
            DBRequest request = DBRequest.builder()
                    .requestType(requestType)
                    .table(table)
                    .conditions(conditions)
                    .extractAsClass(extractAsClass)
                    .build();
            return request.extractAs(clazz);
        }
    }
}
