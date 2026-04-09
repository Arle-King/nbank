package org.example.admin.models;

import lombok.Data;
import org.example.general_models.UserResponseDTO;

import java.util.ArrayList;

@Data
public class AllUsersResponseDTO extends ArrayList<UserResponseDTO> {
    //private List<UserResponseDTO> userList;
}
