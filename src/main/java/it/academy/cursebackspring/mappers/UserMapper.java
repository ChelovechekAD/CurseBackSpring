package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.request.UpdateUserDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.dto.response.UsersDTO;
import it.academy.cursebackspring.enums.RoleEnum;
import it.academy.cursebackspring.models.Role;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    static void mergeEntityAndDTO(User user, UpdateUserDTO userDTO) {
        user.setAddress(Address.builder()
                .building(userDTO.getBuilding())
                .city(userDTO.getCity())
                .street(userDTO.getStreet())
                .build());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setId(userDTO.getId());
    }

    @Mappings({
            @Mapping(source = "user.address.building", target = "building"),
            @Mapping(source = "user.address.street", target = "street"),
            @Mapping(source = "user.address.city", target = "city"),
            @Mapping(source = "user.roleSet", target = "roles")
    })
    UserDTO toDTOFromEntity(User user);

    @Mappings({
            @Mapping(target = "address.building", source = "building"),
            @Mapping(target = "address.street", source = "street"),
            @Mapping(target = "address.city", source = "city"),
    })
    User toEntityFromRegDTO(RegUserDTO dto);

    default UsersDTO toUsersDTOFromEntityList(Page<User> userList, Long count) {
        List<UserDTO> userDTOList = userList.stream()
                .map(UserMapper.INSTANCE::toDTOFromEntity)
                .toList();
        return new UsersDTO(userDTOList, count);
    }

    default Set<String> map(Set<Role> value) {
        return value.stream()
                .map(this::mapRoleToString)
                .collect(Collectors.toSet());
    }

    default String mapRoleToString(Role role) {
        return role != null && role.getRole() != null ? role.getRole().name() : null;
    }

    default Set<Role> mapStringsToRoles(Set<String> values) {
        return values.stream()
                .map(this::mapStringToRole)
                .collect(Collectors.toSet());
    }

    default Role mapStringToRole(String role) {
        if (role == null) {
            return null;
        }
        Role r = new Role();
        r.setRole(RoleEnum.valueOf(role));
        return r;
    }

}
