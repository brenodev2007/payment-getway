package com.brenodev.payment_getway.Entity;
import com.brenodev.payment_getway.Enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "api_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public ApiUser(
            String username,
            String password,
            Role role
    ) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}