package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.models.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JwtMapper {

    JwtMapper INSTANCE = Mappers.getMapper(JwtMapper.class);

    LoginUserJwtDTO toDTO(UserDTO userDTO, String accessToken, String refreshToken);

    RefreshToken createEntity(String userEmail, String refreshToken);

}
