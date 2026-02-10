package com.example.plagchecker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InstructorTest {

    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
    }

    // Test cases for default constructor
    @Test
    void defaultConstructor_ShouldCreateInstructorWithNullValues() {
        // When
        Instructor newInstructor = new Instructor();

        // Then
        assertThat(newInstructor.getId()).isNull();
        assertThat(newInstructor.getUsername()).isNull();
        assertThat(newInstructor.getPassword()).isNull();
        assertThat(newInstructor.getFirstName()).isNull();
        assertThat(newInstructor.getLastName()).isNull();
    }

    // Test cases for parameterized constructor
    @Test
    void parameterizedConstructor_ValidParameters_ShouldCreateInstructorWithSetValues() {
        // When
        Instructor instructor = new Instructor("john_doe", "password123", "John", "Doe");

        // Then
        assertThat(instructor.getId()).isNull(); // ID is not set in constructor
        assertThat(instructor.getUsername()).isEqualTo("john_doe");
        assertThat(instructor.getPassword()).isEqualTo("password123");
        assertThat(instructor.getFirstName()).isEqualTo("John");
        assertThat(instructor.getLastName()).isEqualTo("Doe");
    }

    @Test
    void parameterizedConstructor_NullParameters_ShouldAcceptNullValues() {
        // When
        Instructor instructor = new Instructor(null, null, null, null);

        // Then
        assertThat(instructor.getId()).isNull();
        assertThat(instructor.getUsername()).isNull();
        assertThat(instructor.getPassword()).isNull();
        assertThat(instructor.getFirstName()).isNull();
        assertThat(instructor.getLastName()).isNull();
    }

    @Test
    void parameterizedConstructor_EmptyStrings_ShouldAcceptEmptyValues() {
        // When
        Instructor instructor = new Instructor("", "", "", "");

        // Then
        assertThat(instructor.getUsername()).isEmpty();
        assertThat(instructor.getPassword()).isEmpty();
        assertThat(instructor.getFirstName()).isEmpty();
        assertThat(instructor.getLastName()).isEmpty();
    }

    // Test cases for ID getter and setter
    @Test
    void setId_ValidId_ShouldSetId() {
        // When
        instructor.setId(1L);

        // Then
        assertThat(instructor.getId()).isEqualTo(1L);
    }

    @Test
    void setId_NullId_ShouldAcceptNull() {
        // Given
        instructor.setId(1L);

        // When
        instructor.setId(null);

        // Then
        assertThat(instructor.getId()).isNull();
    }

    // Test cases for username getter and setter
    @Test
    void setUsername_ValidUsername_ShouldSetUsername() {
        // When
        instructor.setUsername("jane_smith");

        // Then
        assertThat(instructor.getUsername()).isEqualTo("jane_smith");
    }

    @Test
    void setUsername_NullUsername_ShouldAcceptNull() {
        // Given
        instructor.setUsername("initial_username");

        // When
        instructor.setUsername(null);

        // Then
        assertThat(instructor.getUsername()).isNull();
    }

    @Test
    void setUsername_EmptyUsername_ShouldAcceptEmpty() {
        // When
        instructor.setUsername("");

        // Then
        assertThat(instructor.getUsername()).isEmpty();
    }

    @Test
    void setUsername_UsernameWithSpecialCharacters_ShouldAcceptSpecialChars() {
        // When
        instructor.setUsername("user_123@domain.com");

        // Then
        assertThat(instructor.getUsername()).isEqualTo("user_123@domain.com");
    }

    // Test cases for password getter and setter
    @Test
    void setPassword_ValidPassword_ShouldSetPassword() {
        // When
        instructor.setPassword("securePassword123!");

        // Then
        assertThat(instructor.getPassword()).isEqualTo("securePassword123!");
    }

    @Test
    void setPassword_NullPassword_ShouldAcceptNull() {
        // Given
        instructor.setPassword("initial_password");

        // When
        instructor.setPassword(null);

        // Then
        assertThat(instructor.getPassword()).isNull();
    }

    @Test
    void setPassword_EmptyPassword_ShouldAcceptEmpty() {
        // When
        instructor.setPassword("");

        // Then
        assertThat(instructor.getPassword()).isEmpty();
    }

    // Test cases for firstName getter and setter
    @Test
    void setFirstName_ValidFirstName_ShouldSetFirstName() {
        // When
        instructor.setFirstName("Alice");

        // Then
        assertThat(instructor.getFirstName()).isEqualTo("Alice");
    }

    @Test
    void setFirstName_NullFirstName_ShouldAcceptNull() {
        // Given
        instructor.setFirstName("Initial");

        // When
        instructor.setFirstName(null);

        // Then
        assertThat(instructor.getFirstName()).isNull();
    }

    @Test
    void setFirstName_EmptyFirstName_ShouldAcceptEmpty() {
        // When
        instructor.setFirstName("");

        // Then
        assertThat(instructor.getFirstName()).isEmpty();
    }

    @Test
    void setFirstName_FirstNameWithSpaces_ShouldAcceptSpaces() {
        // When
        instructor.setFirstName("Mary Jane");

        // Then
        assertThat(instructor.getFirstName()).isEqualTo("Mary Jane");
    }

    // Test cases for lastName getter and setter
    @Test
    void setLastName_ValidLastName_ShouldSetLastName() {
        // When
        instructor.setLastName("Johnson");

        // Then
        assertThat(instructor.getLastName()).isEqualTo("Johnson");
    }

    @Test
    void setLastName_NullLastName_ShouldAcceptNull() {
        // Given
        instructor.setLastName("Initial");

        // When
        instructor.setLastName(null);

        // Then
        assertThat(instructor.getLastName()).isNull();
    }

    @Test
    void setLastName_EmptyLastName_ShouldAcceptEmpty() {
        // When
        instructor.setLastName("");

        // Then
        assertThat(instructor.getLastName()).isEmpty();
    }

    @Test
    void setLastName_LastNameWithHyphen_ShouldAcceptHyphen() {
        // When
        instructor.setLastName("Smith-Johnson");

        // Then
        assertThat(instructor.getLastName()).isEqualTo("Smith-Johnson");
    }

    // Integration tests for complete object state
    @Test
    void completeInstructorObject_AllFieldsSet_ShouldMaintainState() {
        // When
        instructor.setId(10L);
        instructor.setUsername("complete_user");
        instructor.setPassword("password456");
        instructor.setFirstName("Complete");
        instructor.setLastName("User");

        // Then
        assertThat(instructor.getId()).isEqualTo(10L);
        assertThat(instructor.getUsername()).isEqualTo("complete_user");
        assertThat(instructor.getPassword()).isEqualTo("password456");
        assertThat(instructor.getFirstName()).isEqualTo("Complete");
        assertThat(instructor.getLastName()).isEqualTo("User");
    }

    @Test
    void modifyingFields_ShouldUpdateCorrectly() {
        // Given
        instructor.setUsername("original_username");
        instructor.setFirstName("Original");

        // When
        instructor.setUsername("updated_username");
        instructor.setFirstName("Updated");

        // Then
        assertThat(instructor.getUsername()).isEqualTo("updated_username");
        assertThat(instructor.getFirstName()).isEqualTo("Updated");
    }

    // Test field independence
    @Test
    void settingOneField_ShouldNotAffectOtherFields() {
        // Given
        instructor.setUsername("test_user");
        instructor.setPassword("test_pass");
        instructor.setFirstName("Test");
        instructor.setLastName("User");

        // When
        instructor.setUsername("updated_user");

        // Then
        assertThat(instructor.getUsername()).isEqualTo("updated_user");
        assertThat(instructor.getPassword()).isEqualTo("test_pass");
        assertThat(instructor.getFirstName()).isEqualTo("Test");
        assertThat(instructor.getLastName()).isEqualTo("User");
    }

    // Boundary value tests
    @Test
    void setFields_VeryLongStrings_ShouldAcceptLongStrings() {
        // Given
        String longString = "A".repeat(1000);

        // When
        instructor.setUsername(longString);
        instructor.setPassword(longString);
        instructor.setFirstName(longString);
        instructor.setLastName(longString);

        // Then
        assertThat(instructor.getUsername()).isEqualTo(longString);
        assertThat(instructor.getPassword()).isEqualTo(longString);
        assertThat(instructor.getFirstName()).isEqualTo(longString);
        assertThat(instructor.getLastName()).isEqualTo(longString);
    }

    @Test
    void setFields_WhitespaceStrings_ShouldAcceptWhitespace() {
        // When
        instructor.setUsername("   ");
        instructor.setPassword("\t\n");
        instructor.setFirstName(" ");
        instructor.setLastName("  ");

        // Then
        assertThat(instructor.getUsername()).isEqualTo("   ");
        assertThat(instructor.getPassword()).isEqualTo("\t\n");
        assertThat(instructor.getFirstName()).isEqualTo(" ");
        assertThat(instructor.getLastName()).isEqualTo("  ");
    }
}