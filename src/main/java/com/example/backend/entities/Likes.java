package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"post", "user"})
@ToString(exclude = {"post", "user"})
@Transactional
@Builder
@Entity
public class Likes {



    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;
    private String like;


    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
