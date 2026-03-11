package inventorymanager.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateRequest {
    private String role;
    private String userId;
}
