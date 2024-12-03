package com.hust.Ecommerce.entities.inventory;

import com.hust.Ecommerce.entities.BaseEntity;

import com.hust.Ecommerce.entities.product.Variant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "inventories")
public class Inventory extends BaseEntity {

    @Column(name = "available")
    private Integer available;

    @Column(name = "sold")
    private Integer sold;

    @OneToOne
    @JoinColumn(name = "variant_id", referencedColumnName = "id")
    private Variant variant;
}
