package com.hust.Ecommerce.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hust.Ecommerce.entities.inventory.Inventory;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.mappers.projection.SimpleProductInventory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ProjectionRepository {

    private EntityManager em;

    public List<SimpleProductInventory> findSimpleProductInventories(List<Long> productIds) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SimpleProductInventory> query = cb.createQuery(SimpleProductInventory.class);

        Root<Variant> variant = query.from(Variant.class);
        Join<Variant, Inventory> inventory = variant.join("inventory");

        query.select(cb.construct(
                SimpleProductInventory.class,
                variant.get("product").get("id"),
                cb.sum(inventory.get("available")),
                cb.sum(inventory.get("sold"))));

        query.where(variant.get("product").get("id").in(productIds));
        query.groupBy(variant.get("product").get("id"));

        return em.createQuery(query).getResultList();
    }
}
