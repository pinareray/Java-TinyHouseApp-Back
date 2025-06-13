package com.example.tinyhouse.entities.concretes;

import com.example.tinyhouse.entities.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "houseList", "reservationList", "commentList"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter
    @Getter
    private int id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "first_name")
    @Setter
    @Getter
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(name = "last_name")
    @Setter
    @Getter
    private String lastName;

    @Email
    @Column(name = "email", unique = true, nullable = false)
    @Setter
    @Getter
    private String email;

    @Column(name = "password_hash")
    @Setter
    @Getter
    private String passwordHash;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Setter
    @Getter
    private UserRole role;

    @Column(name = "is_active")
    @Setter
    @Getter
    private boolean isActive = true;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<House> houseList = new ArrayList<>();

    @OneToMany(mappedBy = "renter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    public List<House> getHouseList() {
        return houseList;
    }

    public void setHouseList(List<House> houseList) {
        this.houseList = houseList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

}
