package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories() {
        List<CategoryListResponse> categoryList = new ArrayList<>();;
        List<CategoryEntity> allcategories = categoryService.getAllCategories();
        if (allcategories.size() > 0) {
            for (CategoryEntity categoryEntity : allcategories) {
                CategoryListResponse categoryListResponse = new CategoryListResponse();
                categoryListResponse.setCategoryName(categoryEntity.getCategoryName());
                categoryListResponse.setId(UUID.fromString(categoryEntity.getUuid()));
                categoryList.add(categoryListResponse);
            }
        }
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();

        categoriesListResponse.setCategories(categoryList);

        return new ResponseEntity<>(categoriesListResponse, HttpStatus.OK);
    }

     @CrossOrigin
     @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
     public ResponseEntity<CategoryDetailsResponse> getCategoryDetail(@PathVariable("category_id") final String categoryUuid) throws CategoryNotFoundException {

         CategoryEntity categoryEntity = categoryService.getCategoryById(categoryUuid);

         CategoryDetailsResponse categoryDetails = new CategoryDetailsResponse();
         categoryDetails.setCategoryName(categoryEntity.getCategoryName());
         categoryDetails.setId(UUID.fromString(categoryEntity.getUuid()));

         List<ItemEntity> itemEntities = categoryEntity.getItems();
         List<ItemList> itemLists = new ArrayList<>();

         for (ItemEntity itemEntity : itemEntities) {
             ItemList itemList = new ItemList();
             itemList.setItemName(itemEntity.getItemName());
             itemList.setPrice(itemEntity.getPrice());
             itemList.setId(UUID.fromString(itemEntity.getUuid()));
             if (!itemEntity.getType().equals("0")) {
                 itemList.setItemType(ItemList.ItemTypeEnum.valueOf("NON_VEG"));
                 }
             else {
                 itemList.setItemType(ItemList.ItemTypeEnum.valueOf("VEG"));
             }
                 itemLists.add(itemList);
         }

         categoryDetails.setItemList(itemLists);

         return new ResponseEntity<CategoryDetailsResponse>(categoryDetails, HttpStatus.OK);
     }
}
