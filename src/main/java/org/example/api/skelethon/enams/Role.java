package org.example.api.skelethon.enams;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN(
            "ADMIN"
    ),

    USER(
            "USER"
    );
    private final String role;
}
