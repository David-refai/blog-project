package com.example.backend.entities;


import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"posts", "comments", "likes"})
@ToString(exclude = {"posts", "comments", "likes"})
@Transactional
@Builder
@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name")
    private String name;

    @NotNull.List({
        @NotNull(message = "Email cannot be null"),
        @NotNull(message = "Email cannot be empty")
    })
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;

//    validate password
    @NotNull.List({
        @NotNull(message = "Password cannot be null"),
        @NotNull(message = "Password cannot be empty")
    })
    @Range(min = 3, max = 16, message = "Password must be between 8 and 20 characters")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter, one lowercase letter and one number")
    @Column(name = "password")
    private String password;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "date" , columnDefinition = "DATE")
    private LocalDate date;

    @Column(name = "role")
    private String role;

    // One person can have multiple posts
    @OneToMany(mappedBy = "user")
    private List<Posts> posts;

    // One person can write multiple comments
    @OneToMany(mappedBy = "user")
    private List<Comments> comments;

    // One person can like multiple posts
    @OneToMany(mappedBy = "user")
    private List<Likes> likes;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role);
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

}

