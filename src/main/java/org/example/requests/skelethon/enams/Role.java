package org.example.requests.skelethon.enams;

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
