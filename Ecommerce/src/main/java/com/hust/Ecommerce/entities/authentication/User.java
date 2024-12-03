package com.hust.Ecommerce.entities.authentication;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.entities.BaseEntity;
import com.hust.Ecommerce.entities.chat.Message;
import com.hust.Ecommerce.entities.chat.Room;
import com.hust.Ecommerce.entities.order.Order;

import com.hust.Ecommerce.entities.product.Blog;
import com.hust.Ecommerce.entities.review.Review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Pattern(regexp = AppConstants.PHONE_NUMBER)
    @Column(name = "phone_number", unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Size(max = 256)
    @Column(name = "avatar", length = 256)
    private String avatar;

    // trang thai user (0): NOT_ACTIVATED, (1): ACTIVATED, (2): BANNED
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(min = 2, max = 10)
    @Column(name = "language", length = 10, nullable = false)
    private String language;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @ManyToOne
    @JoinColumn(name = "role_name")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Blog> blogs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> tokens = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Room room;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Message> messages = new ArrayList<>();

}
