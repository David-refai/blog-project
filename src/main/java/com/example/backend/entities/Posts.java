package com.example.backend.entities;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"comments", "likes"})
@ToString(exclude = {"comments", "likes"})
@Transactional
@Entity
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image", columnDefinition = "TEXT")
    private LocalDate date;

//    one user can have multiple posts
@ManyToOne
@JoinColumn(name = "user_id")
private User author;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private List<Comments> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likes;

}
