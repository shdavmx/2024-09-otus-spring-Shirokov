package ru.otus.hw.security.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "auth", name = "users")
public class LibraryUser implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(targetEntity = LibraryRole.class, fetch = FetchType.EAGER)
    @JoinTable(schema = "auth", name = "users_authorities", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<LibraryRole> userAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getUserAuthorities();
    }
}
