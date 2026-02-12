package com.example.plagchecker.controller;

import com.example.plagchecker.model.Instructor;
import com.example.plagchecker.service.InstructorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InstructorController.class)
class InstructorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstructorService instructorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Instructor instructor1;
    private Instructor instructor2;

    @BeforeEach
    void setUp() {
        instructor1 = new Instructor();
        instructor1.setId(1L);
        instructor1.setUsername("john_doe");
        instructor1.setPassword("password123");
        instructor1.setFirstName("John");
        instructor1.setLastName("Doe");

        instructor2 = new Instructor();
        instructor2.setId(2L);
        instructor2.setUsername("kanav_singla");
        instructor2.setPassword("password456");
        instructor2.setFirstName("Kanav");
        instructor2.setLastName("Singla");
    }

    // Test cases for createInstructor() method
    @Test
    void createInstructor_ValidInstructor_ShouldReturnCreatedInstructor() throws Exception {
        // Given
        Instructor newInstructor = new Instructor("mike_wilson", "password789", "Mike", "Wilson");
        Instructor createdInstructor = new Instructor("mike_wilson", "password789", "Mike", "Wilson");
        createdInstructor.setId(3L);

        given(instructorService.createInstructor(any(Instructor.class))).willReturn(createdInstructor);

        // When & Then
        mockMvc.perform(post("/api/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newInstructor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))  // Fixed: removed extra 'd'
                .andExpect(jsonPath("$.username", is("mike_wilson")))
                .andExpect(jsonPath("$.firstName", is("Mike")))
                .andExpect(jsonPath("$.lastName", is("Wilson")));
    }

    @Test
    void createInstructor_ServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Given
        Instructor invalidInstructor = new Instructor("", "", "", "");
        given(instructorService.createInstructor(any(Instructor.class)))
                .willThrow(new RuntimeException("Username already exists"));

        // When & Then
        mockMvc.perform(post("/api/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidInstructor)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createInstructor_InvalidJson_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/instructors")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }

    // Test cases for getAllInstructors() method
    @Test
    void getAllInstructors_InstructorsExist_ShouldReturnInstructorsList() throws Exception {
        // Given
        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);
        given(instructorService.getAllInstructors()).willReturn(instructors);

        // When & Then
        mockMvc.perform(get("/api/instructors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("john_doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("kanav_singla")));  // Fixed: changed to match test data
    }

    @Test
    void getAllInstructors_NoInstructors_ShouldReturnEmptyList() throws Exception {
        // Given
        given(instructorService.getAllInstructors()).willReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/instructors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Test cases for getInstructorById() method
    @Test
    void getInstructorById_InstructorExists_ShouldReturnInstructor() throws Exception {
        // Given
        given(instructorService.getInstructorById(1L)).willReturn(Optional.of(instructor1));

        // When & Then
        mockMvc.perform(get("/api/instructors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("john_doe")))
                .andExpect(jsonPath("$.firstName", is("John")))  // Fixed: removed extra 'd'
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void getInstructorById_InstructorNotFound_ShouldReturnNotFound() throws Exception {
        // Given
        given(instructorService.getInstructorById(anyLong())).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/instructors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getInstructorById_InvalidId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/instructors/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePassword_ValidCredentials_ShouldReturnSuccessMessage() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "oldPassword", "password123",
            "newPassword", "newPassword456"
        );
        given(instructorService.updatePassword("john_doe", "password123", "newPassword456"))
            .willReturn(true);

        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));
    }

    @Test
    void updatePassword_InvalidOldPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "oldPassword", "wrongPassword",
            "newPassword", "newPassword456"
        );
        given(instructorService.updatePassword("john_doe", "wrongPassword", "newPassword456"))
            .willReturn(false);

        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid old password"));
    }

    @Test
    void updatePassword_NonExistentUser_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "oldPassword", "password123",
            "newPassword", "newPassword456"
        );
        given(instructorService.updatePassword("nonexistent", "password123", "newPassword456"))
            .willReturn(false);

        // When & Then
        mockMvc.perform(put("/api/instructors/password/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid old password"));
    }

    @Test
    void updatePassword_EmptyOldPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "oldPassword", "",
            "newPassword", "newPassword456"
        );
        given(instructorService.updatePassword("john_doe", "", "newPassword456"))
            .willReturn(false);

        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid old password"));
    }

    @Test
    void updatePassword_EmptyNewPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "oldPassword", "password123",
            "newPassword", ""
        );
        given(instructorService.updatePassword("john_doe", "password123", ""))
            .willReturn(false);

        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid old password"));
    }

    @Test
    void updatePassword_MissingOldPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "newPassword", "newPassword456"
        );

        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePassword_MissingNewPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        Map<String, String> passwordRequest = Map.of(
            "oldPassword", "password123"
        );

        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePassword_InvalidJsonBody_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePassword_EmptyJsonBody_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/instructors/password/john_doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}