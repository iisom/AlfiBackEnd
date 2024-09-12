package com.galvanize.jwtclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriesController.class)
public class CategoriesControllerTests {

    @MockBean
    CategoryService categoryService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

 // GET: /api/categories
    // GET: /api/categories  Returns a list of all categories
    @Test
    @WithMockUser(roles = {"User"})
    void getCategories_noParams_returnsAllCategories() throws Exception {
        List<Cat> listOfCategories = new ArrayList<>();
        listOfCategories.add(new Cat("Electronics", 1234L));
        listOfCategories.add(new Cat("Kitchen", 1235L));
        listOfCategories.add(new Cat("Yard", 1236L));
        when(categoryService.getCategorys()).thenReturn(listOfCategories);
        mockMvc.perform(get("/api/admin/categories")
                    .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(3)));
    }

    // POST: /api/categories
    // POST: /api/categories Adds a new category

    @Test
    @WithMockUser(roles = {"Admin"})
    void addCategory_returnsCategory() throws Exception {
        Cat cat = new Cat("Appliances", 1237L);
        when(categoryService.addCategory(any(Cat.class))).thenReturn(cat);
        mockMvc.perform(post("/api/admin/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .content(mapper.writeValueAsString(cat)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Appliances"))
                .andExpect(jsonPath("$.id").value(1237L));
    }

    // DELETE: /api/admin/categories
    // DELETE: /api/admin/categories deletes a category
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteCategory_exists_returns202() throws Exception {
        mockMvc.perform(delete("/api/admin/categories/Kitchen")
                        .with(csrf()))
                .andExpect(status().isAccepted());
        verify(categoryService).deleteCategory(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteCategory_doesnotExists_returnsNotFound() throws Exception {
        doThrow(new CatNotFoundException("That is not a valid Category location.")).when(categoryService).deleteCategory(anyString());
        mockMvc.perform(delete("/api/admin/categories/NOTHING")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateCategory_valid_Returns_UpdatedCategoryName() throws Exception {
        List<Cat> listOfCategories = new ArrayList<>();
        Cat updatedCategory = new Cat("Yard",1236L );
        when(categoryService.updateCategory(anyString(), any())).thenReturn(updatedCategory);
        updatedCategory.setName("Outdoors");
        when(categoryService.updateCategory(anyString(), any()))
                .thenReturn(updatedCategory);
        mockMvc.perform(patch("/api/admin/categories/Yard")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content("{\"name\":\"Yard\"}"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").value("Outdoors"))
            .andExpect(jsonPath("id").value("1236"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateCategory_invalid_RequestReturns_throwsException() throws Exception {
        when(categoryService.updateCategory(anyString(), any())).thenThrow(new CatNotFoundException("Category not found"));
        mockMvc.perform(patch("/api/admin/categories/Food")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf())
                    .content("{\"name\":\"Electronics\"}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}