// package com.hust.Ecommerce.configuration;

// import java.util.function.Consumer;

// import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.function.HandlerFunction;
// import org.springframework.web.servlet.function.RouterFunction;
// import org.springframework.web.servlet.function.ServerResponse;

// import com.hust.Ecommerce.controllers.GenericController;

// @Configuration
// public class GenericControllerDocumentationConfig {

// HandlerFunction<ServerResponse> handler = request ->
// ServerResponse.ok().build();

// private RouterFunction<ServerResponse> generateRoute(String resource) {
// return SpringdocRouteBuilder.route()
// .GET("/api/" + resource, handler, operation(resource, "getAllResources"))
// .GET("/api/" + resource + "/{id}", handler, operation(resource,
// "getResource"))
// .POST("/api/" + resource, handler, operation(resource, "createResource"))
// .PUT("/api/" + resource + "/{id}", handler, operation(resource,
// "updateResource"))
// .DELETE("/api/" + resource + "/{id}", handler, operation(resource,
// "deleteResource"))
// .DELETE("/api/" + resource, handler, operation(resource, "deleteResources"))
// .build();
// }

// private Consumer<Builder> operation(String resource, String handlerMethod) {
// return ops ->
// ops.operationId(resource).beanClass(GenericController.class).beanMethod(handlerMethod);
// }

// @Bean
// public RouterFunction<ServerResponse> route() {
// return generateRoute("users")
// .and(generateRoute("categories"))
// .and(generateRoute("brands"))
// .and(generateRoute("products"))
// .and(generateRoute("variants"))
// .and(generateRoute("images"))
// .and(generateRoute("orders"))
// .and(generateRoute("reviews"))
// .and(generateRoute("payment-methods"))
// .and(generateRoute("rooms"));
// }
// }
