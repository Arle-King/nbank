package org.example.api.models.admin.users;

import lombok.Data;
import lombok.Getter;
import org.example.api.models.BaseModel;

import java.util.ArrayList;

@Data
@Getter
public class AllUsersResponseDTO extends BaseModel {
    private ArrayList<CreateUserResponseDTO> users;
}
