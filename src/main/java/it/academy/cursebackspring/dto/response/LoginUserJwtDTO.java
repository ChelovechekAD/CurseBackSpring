package it.academy.cursebackspring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUserJwtDTO {

    @Builder.Default
    private String type = "Bearer";
    private String accessToken;
    private String refreshToken;
    private UserDTO userDTO;

}
