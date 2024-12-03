package com.hust.Ecommerce.controllers;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.constants.SearchFields;
import com.hust.Ecommerce.dtos.authentication.UserRequest;
import com.hust.Ecommerce.dtos.authentication.UserResponse;
import com.hust.Ecommerce.dtos.chat.RoomRequest;
import com.hust.Ecommerce.dtos.chat.RoomResponse;
import com.hust.Ecommerce.dtos.client.order.OrderRequest;
import com.hust.Ecommerce.dtos.client.order.OrderResponse;
import com.hust.Ecommerce.dtos.general.ImageRequest;
import com.hust.Ecommerce.dtos.general.ImageResponse;
import com.hust.Ecommerce.dtos.inventory.InventoryRequest;
import com.hust.Ecommerce.dtos.inventory.InventoryResponse;
import com.hust.Ecommerce.dtos.product.BlogRequest;
import com.hust.Ecommerce.dtos.product.BlogResponse;
import com.hust.Ecommerce.dtos.product.BrandRequest;
import com.hust.Ecommerce.dtos.product.BrandResponse;
import com.hust.Ecommerce.dtos.product.CategoryRequest;
import com.hust.Ecommerce.dtos.product.CategoryResponse;
import com.hust.Ecommerce.dtos.product.ProductRequest;
import com.hust.Ecommerce.dtos.product.ProductResponse;
import com.hust.Ecommerce.dtos.product.VariantRequest;
import com.hust.Ecommerce.dtos.product.VariantResponse;
import com.hust.Ecommerce.dtos.review.ReviewRequest;
import com.hust.Ecommerce.dtos.review.ReviewResponse;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.chat.Room;
import com.hust.Ecommerce.entities.general.Image;
import com.hust.Ecommerce.entities.inventory.Inventory;
import com.hust.Ecommerce.entities.order.Order;
import com.hust.Ecommerce.entities.product.Blog;
import com.hust.Ecommerce.entities.product.Brand;
import com.hust.Ecommerce.entities.product.Category;
import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.mappers.authentication.UserMapper;
import com.hust.Ecommerce.mappers.chat.RoomMapper;
import com.hust.Ecommerce.mappers.general.ImageMapper;
import com.hust.Ecommerce.mappers.inventory.InventoryMapper;
import com.hust.Ecommerce.mappers.order.OrderMapper;
import com.hust.Ecommerce.mappers.product.BlogMapper;
import com.hust.Ecommerce.mappers.product.BrandMapper;
import com.hust.Ecommerce.mappers.product.CategoryMapper;
import com.hust.Ecommerce.mappers.product.ProductMapper;
import com.hust.Ecommerce.mappers.product.VariantMapper;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.repositories.chat.RoomRepository;
import com.hust.Ecommerce.repositories.general.ImageRepository;
import com.hust.Ecommerce.repositories.inventory.InventoryRepository;
import com.hust.Ecommerce.repositories.order.OrderRepository;
import com.hust.Ecommerce.repositories.product.BlogRepository;
import com.hust.Ecommerce.repositories.product.BrandRepository;
import com.hust.Ecommerce.repositories.product.CategoryRepository;
import com.hust.Ecommerce.repositories.product.ProductRepository;
import com.hust.Ecommerce.repositories.product.VariantRepository;
import com.hust.Ecommerce.services.CrudService;
import com.hust.Ecommerce.services.GenericService;
import com.hust.Ecommerce.services.order.OrderService;
import com.hust.Ecommerce.services.review.ReviewService;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GenericMappingRegister {

        private final ApplicationContext context;
        private final RequestMappingHandlerMapping handlerMapping;

        // ham tao api
        private <I, O> void register(String resource,
                        GenericController<I, O> controller,
                        CrudService<Long, I, O> service,
                        Class<I> requestType) throws NoSuchMethodException {
                RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
                options.setPatternParser(new PathPatternParser());

                controller.setCrudService(service);
                controller.setRequestType(requestType);

                handlerMapping.registerMapping(
                                RequestMappingInfo.paths("/api" + resource)
                                                .methods(RequestMethod.GET)
                                                .produces(MediaType.APPLICATION_JSON_VALUE)
                                                .options(options)
                                                .build(),
                                controller,
                                controller.getClass().getMethod("getAllResources", int.class, int.class,
                                                String.class, String.class, String.class, boolean.class));

                handlerMapping.registerMapping(
                                RequestMappingInfo.paths("/api" + resource + "/{id}")
                                                .methods(RequestMethod.GET)
                                                .produces(MediaType.APPLICATION_JSON_VALUE)
                                                .options(options)
                                                .build(),
                                controller,
                                controller.getClass().getMethod("getResource", Long.class));

                handlerMapping.registerMapping(
                                RequestMappingInfo.paths("/api" + resource)
                                                .methods(RequestMethod.POST)
                                                .consumes(MediaType.APPLICATION_JSON_VALUE)
                                                .produces(MediaType.APPLICATION_JSON_VALUE)
                                                .options(options)
                                                .build(),
                                controller,
                                controller.getClass().getMethod("createResource", JsonNode.class));

                handlerMapping.registerMapping(
                                RequestMappingInfo.paths("/api" + resource + "/{id}")
                                                .methods(RequestMethod.PUT)
                                                .consumes(MediaType.APPLICATION_JSON_VALUE)
                                                .produces(MediaType.APPLICATION_JSON_VALUE)
                                                .options(options)
                                                .build(),
                                controller,
                                controller.getClass().getMethod("updateResource", Long.class, JsonNode.class));

                handlerMapping.registerMapping(
                                RequestMappingInfo.paths("/api" + resource + "/{id}")
                                                .methods(RequestMethod.DELETE)
                                                .options(options)
                                                .build(),
                                controller,
                                controller.getClass().getMethod("deleteResource", Long.class));

                handlerMapping.registerMapping(
                                RequestMappingInfo.paths("/api" + resource)
                                                .methods(RequestMethod.DELETE)
                                                .consumes(MediaType.APPLICATION_JSON_VALUE)
                                                .options(options)
                                                .build(),
                                controller,
                                controller.getClass().getMethod("deleteResources", List.class));
        }

        // controllers
        private GenericController<UserRequest, UserResponse> userController;
        private GenericController<CategoryRequest, CategoryResponse> categoryController;
        private GenericController<ProductRequest, ProductResponse> productController;
        private GenericController<BrandRequest, BrandResponse> brandController;
        private GenericController<ImageRequest, ImageResponse> imageController;
        private GenericController<ReviewRequest, ReviewResponse> reviewController;
        private GenericController<BlogRequest, BlogResponse> blogController;
        private GenericController<InventoryRequest, InventoryResponse> inventoryController;
        private GenericController<VariantRequest, VariantResponse> variantController;
        private GenericController<RoomRequest, RoomResponse> roomController;
        private GenericController<OrderRequest, OrderResponse> orderController;
        // services
        private GenericService<User, UserRequest, UserResponse> userService;
        private GenericService<Category, CategoryRequest, CategoryResponse> categoryService;
        private GenericService<Product, ProductRequest, ProductResponse> productService;
        private GenericService<Brand, BrandRequest, BrandResponse> brandService;
        private GenericService<Image, ImageRequest, ImageResponse> imageService;
        private GenericService<Blog, BlogRequest, BlogResponse> blogService;
        private GenericService<Inventory, InventoryRequest, InventoryResponse> inventoryService;
        private GenericService<Variant, VariantRequest, VariantResponse> variantService;
        private GenericService<Room, RoomRequest, RoomResponse> roomService;

        @PostConstruct
        public void registerControllers() throws NoSuchMethodException {
                register("/users", userController, userService.init(
                                context.getBean(UserRepository.class),
                                context.getBean(UserMapper.class),
                                SearchFields.USER,
                                ResourceName.USER), UserRequest.class);

                register("/categories", categoryController, categoryService.init(
                                context.getBean(CategoryRepository.class),
                                context.getBean(CategoryMapper.class),
                                SearchFields.CATEGORY,
                                ResourceName.CATEGORY), CategoryRequest.class);

                register("/brands", brandController, brandService.init(
                                context.getBean(BrandRepository.class),
                                context.getBean(BrandMapper.class),
                                SearchFields.BRAND,
                                ResourceName.BRAND), BrandRequest.class);
                // tao san pham + anh kem (khong su dung anh cu trong db)
                register("/products", productController, productService.init(
                                context.getBean(ProductRepository.class),
                                context.getBean(ProductMapper.class),
                                SearchFields.PRODUCT,
                                ResourceName.PRODUCT), ProductRequest.class);

                // tao inventory cho tung bien the
                register("/inventories", inventoryController, inventoryService.init(
                                context.getBean(InventoryRepository.class),
                                context.getBean(InventoryMapper.class),
                                SearchFields.INVENTORY,
                                ResourceName.INVENTORY), InventoryRequest.class);

                // tao anh doc lap
                register("/images", imageController, imageService.init(
                                context.getBean(ImageRepository.class),
                                context.getBean(ImageMapper.class),
                                SearchFields.IMAGE,
                                ResourceName.IMAGE), ImageRequest.class);

                // crud review basic, not customized
                register("/reviews", reviewController, context.getBean(ReviewService.class), ReviewRequest.class);

                register("/blogs", blogController, blogService.init(
                                context.getBean(BlogRepository.class),
                                context.getBean(BlogMapper.class),
                                SearchFields.BLOG,
                                ResourceName.BLOG), BlogRequest.class);

                register("/variants", variantController, variantService.init(
                                context.getBean(VariantRepository.class),
                                context.getBean(VariantMapper.class),
                                SearchFields.VARIANT,
                                ResourceName.VARIANT), VariantRequest.class);

                register("/rooms", roomController, roomService.init(
                                context.getBean(RoomRepository.class),
                                context.getBean(RoomMapper.class),
                                SearchFields.ROOM,
                                ResourceName.ROOM), RoomRequest.class);

                // crud order , customized
                register("/orders", orderController, context.getBean(OrderService.class), OrderRequest.class);
        }

}
