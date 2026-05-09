package org.example.api.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDAO {
    private Long id;
    private String accountNumber;
    private Double balance;
    private Long customerId;
}
