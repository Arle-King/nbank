package org.example.ui.enams;

import lombok.Getter;

@Getter
public enum BankAlert {

    DEPOSIT_SUCCESS("Successfully deposited $%s to account %s!"),

    DEPOSIT_LESS_5000("Please deposit less or equal to 5000$"),

    CUSTOM_NAME_SUCCESS("Name updated successfully!"),

    CUSTOM_NAME_ERROR("Name must contain two words with letters only"),

    MAKE_A_TRANSFER_ERROR("Error: Invalid transfer: insufficient funds or invalid accounts"),

    MAKE_A_TRANSFER_SUCCESS("Successfully transferred $%s to account %s!");

    private final String massage;

    BankAlert(String massage) {
        this.massage = massage;
    }

    public String format(Object... args) {
        return String.format(massage, args);
    }
}
