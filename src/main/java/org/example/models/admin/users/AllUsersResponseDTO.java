package org.example.models.admin.users;

import lombok.Data;
import lombok.Getter;
import org.example.models.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class AllUsersResponseDTO extends BaseModel {
    private ArrayList<CreateUserResponseDTO> users;
}
