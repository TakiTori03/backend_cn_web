package com.hust.Ecommerce.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hust.Ecommerce.entities.BaseEntity;
import com.hust.Ecommerce.entities.authentication.Role;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.chat.Room;
import com.hust.Ecommerce.entities.order.Order;
import com.hust.Ecommerce.entities.order.OrderVariantKey;
import com.hust.Ecommerce.entities.product.Brand;
import com.hust.Ecommerce.entities.product.Category;
import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.repositories.authentication.RoleRepository;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.repositories.product.CategoryRepository;
import com.hust.Ecommerce.repositories.product.ProductRepository;
import com.hust.Ecommerce.repositories.product.VariantRepository;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUtils {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VariantRepository variantRepository;

    @Named("hashPassword")
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public abstract Category mapToCategory(Long id);

    // public abstract List<Category> mapToCategoryList(List<Long> categoryIds);

    public abstract Brand mapToBrand(Long id);

    public abstract Room mapToRoom(Long id);

    public List<Category> mapToCategoryList(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new ArrayList<>();
        }
        return categoryRepository.findAllById(categoryIds);
    }

    public Product mapToProduct(Long id) {
        return productRepository.getById(id);
    }

    public User mapToUser(Long id) {
        return userRepository.getById(id);
    }

    public Role mapToRole(String name) {
        return roleRepository.getById(name);
    }

    public Variant mapToVariant(Long id) {
        return variantRepository.getById(id);
    }

    @AfterMapping
    @Named("attachProduct")
    public Product attachProduct(@MappingTarget Product product) {
        product.getImages().forEach(image -> image.setProduct(product));

        product.getCategories().forEach(category -> {
            if (!category.getProducts().contains(product)) {
                category.getProducts().add(product); // Thêm product vào danh sách của category
            }
        });
        product.getVariants().forEach(variant -> variant.setProduct(product));
        product.setCategories(attachList(product.getCategories(), categoryRepository));
        return product;
    }

    // @AfterMapping
    // @Named("attachUser")
    // public User attachUser(@MappingTarget User user) {
    // return user.setRoles(attachSet(user.getRole(), roleRepository));
    // }

    @AfterMapping
    @Named("attachOrder")
    public Order attachOrder(@MappingTarget Order order) {
        order.getOrderVariants().forEach(orderVariant -> {
            orderVariant.setOrderVariantKey(new OrderVariantKey(order.getId(), orderVariant.getVariant().getId()));
            orderVariant.setOrder(order);
        });
        return order;
    }

    private <E extends BaseEntity> List<E> attachList(List<E> entities, JpaRepository<E, Long> repository) {
        List<E> detachedSet = Optional.ofNullable(entities).orElseGet(ArrayList::new);
        List<E> attachedSet = new ArrayList();

        for (E entity : detachedSet) {
            if (entity.getId() != null) {
                repository.findById(entity.getId()).ifPresent(attachedSet::add);
            } else {
                attachedSet.add(entity);
            }
        }

        return attachedSet;
    }

    private <E extends BaseEntity> Set<E> attachSet(Set<E> entities, JpaRepository<E, Long> repository) {
        Set<E> detachedSet = Optional.ofNullable(entities).orElseGet(HashSet::new);
        Set<E> attachedSet = new HashSet<>();

        for (E entity : detachedSet) {
            if (entity.getId() != null) {
                repository.findById(entity.getId()).ifPresent(attachedSet::add);
            } else {
                attachedSet.add(entity);
            }
        }

        return attachedSet;
    }
}
