package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.enums.RoleEnum;
import it.academy.cursebackspring.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepos extends CrudRepository<Role, Short> {

    Role getByRole(RoleEnum roleEnum);

}
