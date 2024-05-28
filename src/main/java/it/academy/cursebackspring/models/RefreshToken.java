package it.academy.cursebackspring.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "refresh_token")
public class RefreshToken implements Serializable {

    @Id
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "refresh_token", unique = true, nullable = false)
    private String refreshToken;

}
