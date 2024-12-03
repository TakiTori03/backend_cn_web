package com.hust.Ecommerce.controllers;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.services.CrudService;

import lombok.Setter;

@Component
@Setter
@Scope("prototype")
public class GenericController<I, O> {

    private CrudService<Long, I, O> crudService;
    private Class<I> requestType;

    public ResponseEntity<ApiResponse<ListResponse<O>>> getAllResources(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT) String sort,
            @RequestParam(name = "filter", required = false) @Nullable String filter,
            @RequestParam(name = "search", required = false) @Nullable String search,
            @RequestParam(name = "all", required = false) boolean all) {
        return ResponseEntity.ok(ApiResponse.<ListResponse<O>>builder()
                .success(true)
                .payload(crudService.findAll(page, size, sort, filter, search, all))
                .build());
    }

    public ResponseEntity<ApiResponse<O>> getResource(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.<O>builder()
                .payload(crudService.findById(id))
                .success(true)
                .build());
    }

    public ResponseEntity<ApiResponse<O>> createResource(@RequestBody JsonNode request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<O>builder()
                .success(true)
                .payload(crudService.save(request, requestType))
                .build());
    }

    public ResponseEntity<ApiResponse<O>> updateResource(@PathVariable("id") Long id, @RequestBody JsonNode request) {
        return ResponseEntity.ok(ApiResponse.<O>builder()
                .success(true)
                .payload(crudService.save(id, request, requestType))
                .build());
    }

    public ResponseEntity<ApiResponse<Void>> deleteResource(@PathVariable("id") Long id) {
        crudService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).build());
    }

    public ResponseEntity<ApiResponse<Void>> deleteResources(@RequestBody List<Long> ids) {
        crudService.delete(ids);
        return ResponseEntity.ok(ApiResponse.<Void>builder().success(true).build());
    }

}
