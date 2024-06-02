package it.academy.cursebackspring.models;

import it.academy.cursebackspring.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,
            columnDefinition = "enum(ROLE_ADMIN, ROLE_DEFAULT_USER)")
    private RoleEnum role;

    @Override
    public String getAuthority() {
        return role.name();
    }
}
