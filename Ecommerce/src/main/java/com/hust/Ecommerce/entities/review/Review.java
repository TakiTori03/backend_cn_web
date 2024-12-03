package com.hust.Ecommerce.entities.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hust.Ecommerce.entities.BaseEntity;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review extends BaseEntity {

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "content")
    private String content;

    @Column(name = "reply")
    private String reply;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

}
