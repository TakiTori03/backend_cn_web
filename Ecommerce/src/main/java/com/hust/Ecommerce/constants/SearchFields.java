package com.hust.Ecommerce.constants;

import java.util.List;

public interface SearchFields {
        List<String> USER = List.of(
                        "email",
                        "name",
                        "phoneNumber",
                        "address");

        List<String> ROLE = List.of(
                        "name",
                        "permission");

        List<String> CATEGORY = List.of(
                        "name",
                        "slug",
                        "description");

        List<String> REVIEW = List.of(
                        "user.name",
                        "user.email",
                        "product.name",
                        "product.slug",
                        "content");

        List<String> CLIENT_PRODUCT = List.of(
                        "name",
                        "slug",
                        "category.name",
                        "brand");

        List<String> INVENTORY = List.of(
                        "amount",
                        "available",
                        "sold",
                        "product.name",
                        "product.category",
                        "product.brand");
        List<String> BRAND = List.of(
                        "name",
                        "description");

        List<String> PRODUCT = List.of(
                        "name",
                        "slug",
                        "description",
                        // "categoryList.name",
                        "brand.name",
                        "unit",
                        "model",
                        "status",
                        "price"

        );
        List<String> IMAGE = List.of(
                        "name",
                        "type");

        List<String> BLOG = List.of(
                        "title",
                        "user.name",
                        "user.email");

        List<String> VARIANT = List.of(
                        "product.name",
                        "product.code",
                        "sku");

        List<String> MESSAGE = List.of(
                        "createdAt",
                        "room.id");

        List<String> ROOM = List.of(
                        "id",
                        "name",
                        "user.id");

        List<String> ORDER = List.of(
                        "code",
                        "toName",
                        "toPhone",
                        "toAddress",
                        "user.email",
                        "user.name");
}
