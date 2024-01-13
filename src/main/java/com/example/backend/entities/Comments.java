package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDate;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "post"})
@ToString(exclude = {"user", "post"})
@Builder
@Transactional
@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;

//    one user can have comments on multiple posts
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    one post can have multiple comments
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;

}
