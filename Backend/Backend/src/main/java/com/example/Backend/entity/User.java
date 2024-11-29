package com.example.Backend.entity;

import com.example.Backend.entity.Enum.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
//User:id,  Name, email, password
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    @Email(message = "Please enter a valid e-mail address")
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles roles;

    private boolean enable = false;

    @OneToMany(mappedBy = "author")
    private List<Blog> blogs;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Share> shares;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Friend> receiveFriends; //user sent friend request

    @OneToMany(mappedBy = "friend",cascade = CascadeType.ALL)
    private List<Friend> sentFriends; // user received friend request

}
