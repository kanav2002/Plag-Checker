package com.example.plagchecker.service;

import com.example.plagchecker.model.Instructor;
import com.example.plagchecker.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

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
    void createInstructor_ValidInstructor_ShouldReturnSavedInstructor() {
        // Given
        Instructor newInstructor = new Instructor("mike_wilson", "password789", "Mike", "Wilson");
        Instructor savedInstructor = new Instructor("mike_wilson", "password789", "Mike", "Wilson");
        savedInstructor.setId(3L);

        given(instructorRepository.save(newInstructor)).willReturn(savedInstructor);

        // When
        Instructor result = instructorService.createInstructor(newInstructor);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getUsername()).isEqualTo("mike_wilson");
        assertThat(result.getFirstName()).isEqualTo("Mike");
        assertThat(result.getLastName()).isEqualTo("Wilson");
        
        verify(instructorRepository, times(1)).save(newInstructor);
    }

    @Test
    void createInstructor_NullInstructor_ShouldThrowException() {
        // Given
        given(instructorRepository.save(null)).willThrow(new IllegalArgumentException("Instructor cannot be null"));

        // When & Then
        assertThatThrownBy(() -> instructorService.createInstructor(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Instructor cannot be null");

        verify(instructorRepository, times(1)).save(null);
    }

    @Test
    void createInstructor_RepositoryThrowsException_ShouldPropagateException() {
        // Given
        Instructor invalidInstructor = new Instructor("", "", "", "");
        given(instructorRepository.save(invalidInstructor))
                .willThrow(new RuntimeException("Database constraint violation"));

        // When & Then
        assertThatThrownBy(() -> instructorService.createInstructor(invalidInstructor))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database constraint violation");

        verify(instructorRepository, times(1)).save(invalidInstructor);
    }

    // Test cases for getAllInstructors() method
    @Test
    void getAllInstructors_InstructorsExist_ShouldReturnAllInstructors() {
        // Given
        List<Instructor> instructors = Arrays.asList(instructor1, instructor2);
        given(instructorRepository.findAll()).willReturn(instructors);

        // When
        List<Instructor> result = instructorService.getAllInstructors();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(instructor1, instructor2);
        
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void getAllInstructors_NoInstructors_ShouldReturnEmptyList() {
        // Given
        given(instructorRepository.findAll()).willReturn(Collections.emptyList());

        // When
        List<Instructor> result = instructorService.getAllInstructors();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void getAllInstructors_RepositoryThrowsException_ShouldPropagateException() {
        // Given
        given(instructorRepository.findAll()).willThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThatThrownBy(() -> instructorService.getAllInstructors())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection error");

        verify(instructorRepository, times(1)).findAll();
    }

    // Test cases for getInstructorById() method
    @Test
    void getInstructorById_InstructorExists_ShouldReturnInstructor() {
        // Given
        given(instructorRepository.findById(1L)).willReturn(Optional.of(instructor1));

        // When
        Optional<Instructor> result = instructorService.getInstructorById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(instructor1);
        assertThat(result.get().getUsername()).isEqualTo("john_doe");
        
        verify(instructorRepository, times(1)).findById(1L);
    }

    @Test
    void getInstructorById_InstructorNotFound_ShouldReturnEmptyOptional() {
        // Given
        given(instructorRepository.findById(999L)).willReturn(Optional.empty());

        // When
        Optional<Instructor> result = instructorService.getInstructorById(999L);

        // Then
        assertThat(result).isNotPresent();
        
        verify(instructorRepository, times(1)).findById(999L);
    }

    @Test
    void getInstructorById_NullId_ShouldHandleGracefully() {
        // Given
        given(instructorRepository.findById(null)).willReturn(Optional.empty());

        // When
        Optional<Instructor> result = instructorService.getInstructorById(null);

        // Then
        assertThat(result).isNotPresent();
        
        verify(instructorRepository, times(1)).findById(null);
    }

    // Test cases for getInstructorByUsername() method
    @Test
    void getInstructorByUsername_InstructorExists_ShouldReturnInstructor() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));

        // When
        Optional<Instructor> result = instructorService.getInstructorByUsername("john_doe");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(instructor1);
        assertThat(result.get().getId()).isEqualTo(1L);
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    void getInstructorByUsername_InstructorNotFound_ShouldReturnEmptyOptional() {
        // Given
        given(instructorRepository.findByUsername("nonexistent")).willReturn(Optional.empty());

        // When
        Optional<Instructor> result = instructorService.getInstructorByUsername("nonexistent");

        // Then
        assertThat(result).isNotPresent();
        
        verify(instructorRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void getInstructorByUsername_NullUsername_ShouldHandleGracefully() {
        // Given
        given(instructorRepository.findByUsername(null)).willReturn(Optional.empty());

        // When
        Optional<Instructor> result = instructorService.getInstructorByUsername(null);

        // Then
        assertThat(result).isNotPresent();
        
        verify(instructorRepository, times(1)).findByUsername(null);
    }

    @Test
    void getInstructorByUsername_EmptyUsername_ShouldHandleGracefully() {
        // Given
        given(instructorRepository.findByUsername("")).willReturn(Optional.empty());

        // When
        Optional<Instructor> result = instructorService.getInstructorByUsername("");

        // Then
        assertThat(result).isNotPresent();
        
        verify(instructorRepository, times(1)).findByUsername("");
    }

    // Integration-style test to verify method interactions
    @Test
    void createAndRetrieveInstructor_ShouldWorkTogether() {
        // Given
        Instructor newInstructor = new Instructor("test_user", "password", "Test", "User");
        Instructor savedInstructor = new Instructor("test_user", "password", "Test", "User");
        savedInstructor.setId(5L);

        given(instructorRepository.save(newInstructor)).willReturn(savedInstructor);
        given(instructorRepository.findById(5L)).willReturn(Optional.of(savedInstructor));

        // When
        Instructor created = instructorService.createInstructor(newInstructor);
        Optional<Instructor> retrieved = instructorService.getInstructorById(created.getId());

        // Then
        assertThat(created).isNotNull();
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getUsername()).isEqualTo("test_user");
        
        verify(instructorRepository, times(1)).save(newInstructor);
        verify(instructorRepository, times(1)).findById(5L);
    }

    // Test cases for updatePassword() method
    @Test
    void updatePassword_ValidCredentials_ShouldReturnTrue() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));
        given(instructorRepository.save(instructor1)).willReturn(instructor1);

        // When
        boolean result = instructorService.updatePassword("john_doe", "password123", "newPassword456");

        // Then
        assertThat(result).isTrue();
        assertThat(instructor1.getPassword()).isEqualTo("newPassword456");
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, times(1)).save(instructor1);
    }

    @Test
    void updatePassword_InvalidOldPassword_ShouldReturnFalse() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));

        // When
        boolean result = instructorService.updatePassword("john_doe", "wrongPassword", "newPassword456");

        // Then
        assertThat(result).isFalse();
        assertThat(instructor1.getPassword()).isEqualTo("password123"); // Should remain unchanged
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_UserNotFound_ShouldReturnFalse() {
        // Given
        given(instructorRepository.findByUsername("nonexistent")).willReturn(Optional.empty());

        // When
        boolean result = instructorService.updatePassword("nonexistent", "password123", "newPassword456");

        // Then
        assertThat(result).isFalse();
        
        verify(instructorRepository, times(1)).findByUsername("nonexistent");
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_NullUsername_ShouldReturnFalse() {
        // Given
        given(instructorRepository.findByUsername(null)).willReturn(Optional.empty());

        // When
        boolean result = instructorService.updatePassword(null, "password123", "newPassword456");

        // Then
        assertThat(result).isFalse();
        
        verify(instructorRepository, times(1)).findByUsername(null);
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_EmptyUsername_ShouldReturnFalse() {
        // Given
        given(instructorRepository.findByUsername("")).willReturn(Optional.empty());

        // When
        boolean result = instructorService.updatePassword("", "password123", "newPassword456");

        // Then
        assertThat(result).isFalse();
        
        verify(instructorRepository, times(1)).findByUsername("");
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_NullOldPassword_ShouldReturnFalse() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));

        // When
        boolean result = instructorService.updatePassword("john_doe", null, "newPassword456");

        // Then
        assertThat(result).isFalse();
        assertThat(instructor1.getPassword()).isEqualTo("password123"); // Should remain unchanged
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_EmptyOldPassword_ShouldReturnFalse() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));

        // When
        boolean result = instructorService.updatePassword("john_doe", "", "newPassword456");

        // Then
        assertThat(result).isFalse();
        assertThat(instructor1.getPassword()).isEqualTo("password123"); // Should remain unchanged
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_NullNewPassword_ShouldUpdateToNull() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));
        given(instructorRepository.save(instructor1)).willReturn(instructor1);

        // When
        boolean result = instructorService.updatePassword("john_doe", "password123", null);

        // Then
        assertThat(result).isTrue();
        assertThat(instructor1.getPassword()).isNull();
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, times(1)).save(instructor1);
    }

    @Test
    void updatePassword_EmptyNewPassword_ShouldUpdateToEmpty() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));
        given(instructorRepository.save(instructor1)).willReturn(instructor1);

        // When
        boolean result = instructorService.updatePassword("john_doe", "password123", "");

        // Then
        assertThat(result).isTrue();
        assertThat(instructor1.getPassword()).isEmpty();
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, times(1)).save(instructor1);
    }

    @Test
    void updatePassword_SamePassword_ShouldUpdateSuccessfully() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));
        given(instructorRepository.save(instructor1)).willReturn(instructor1);

        // When
        boolean result = instructorService.updatePassword("john_doe", "password123", "password123");

        // Then
        assertThat(result).isTrue();
        assertThat(instructor1.getPassword()).isEqualTo("password123");
        
        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, times(1)).save(instructor1);
    }

    @Test
    void updatePassword_RepositoryFindThrowsException_ShouldPropagateException() {
        // Given
        given(instructorRepository.findByUsername("john_doe"))
            .willThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThatThrownBy(() -> instructorService.updatePassword("john_doe", "password123", "newPassword456"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database connection error");

        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, never()).save(any(Instructor.class));
    }

    @Test
    void updatePassword_RepositorySaveThrowsException_ShouldPropagateException() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));
        given(instructorRepository.save(instructor1))
            .willThrow(new RuntimeException("Database save error"));

        // When & Then
        assertThatThrownBy(() -> instructorService.updatePassword("john_doe", "password123", "newPassword456"))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Database save error");

        verify(instructorRepository, times(1)).findByUsername("john_doe");
        verify(instructorRepository, times(1)).save(instructor1);
    }

    @Test
    void updatePassword_MultipleConsecutiveUpdates_ShouldWorkCorrectly() {
        // Given
        given(instructorRepository.findByUsername("john_doe")).willReturn(Optional.of(instructor1));
        given(instructorRepository.save(instructor1)).willReturn(instructor1);

        // When - First update
        boolean result1 = instructorService.updatePassword("john_doe", "password123", "newPassword456");
        
        // Update the instructor object state to reflect the new password
        instructor1.setPassword("newPassword456");
        
        // When - Second update
        boolean result2 = instructorService.updatePassword("john_doe", "newPassword456", "finalPassword789");

        // Then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(instructor1.getPassword()).isEqualTo("finalPassword789");
        
        verify(instructorRepository, times(2)).findByUsername("john_doe");
        verify(instructorRepository, times(2)).save(instructor1);
    }

    // Integration test with realistic workflow
    @Test
    void updatePassword_CompleteWorkflow_ShouldWorkEndToEnd() {
        // Given - Create a fresh instructor for this test
        Instructor testInstructor = new Instructor("test_user", "originalPassword", "Test", "User");
        testInstructor.setId(10L);
        
        given(instructorRepository.findByUsername("test_user")).willReturn(Optional.of(testInstructor));
        given(instructorRepository.save(testInstructor)).willReturn(testInstructor);

        // When
        boolean result = instructorService.updatePassword("test_user", "originalPassword", "superSecureNewPassword123!");

        // Then
        assertThat(result).isTrue();
        assertThat(testInstructor.getPassword()).isEqualTo("superSecureNewPassword123!");
        
        verify(instructorRepository, times(1)).findByUsername("test_user");
        verify(instructorRepository, times(1)).save(testInstructor);
    }
}