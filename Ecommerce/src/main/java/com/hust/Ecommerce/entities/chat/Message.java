package com.hust.Ecommerce.entities.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hust.Ecommerce.entities.BaseEntity;
import com.hust.Ecommerce.entities.authentication.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "message")
public class Message extends BaseEntity {
    @Column(name = "content", nullable = false)
    private String content;

    // (1) : complaints ; (2) : response
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    private Room room;

    @OneToOne(mappedBy = "lastMessage", cascade = CascadeType.ALL)
    private Room roomFlat;
}
