package com.example.plagchecker.repository;

import com.example.plagchecker.model.Instructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class InstructorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InstructorRepository instructorRepository;

    private Instructor instructor1;
    private Instructor instructor2;

    @BeforeEach
    void setUp() {
        instructor1 = new Instructor("john_doe", "password123", "John", "Doe");
        instructor2 = new Instructor("kanav_singla", "password456", "Kanav", "Singla");
    }

    // Test cases for save() method (inherited from JpaRepository)
    @Test
    void save_ValidInstructor_ShouldPersistInstructor() {
        // When
        Instructor savedInstructor = instructorRepository.save(instructor1);
        entityManager.flush();

        // Then
        assertThat(savedInstructor).isNotNull();
        assertThat(savedInstructor.getId()).isNotNull();
        assertThat(savedInstructor.getUsername()).isEqualTo("john_doe");
        assertThat(savedInstructor.getFirstName()).isEqualTo("John");
        assertThat(savedInstructor.getLastName()).isEqualTo("Doe");
    }

    @Test
    void save_DuplicateUsername_ShouldThrowException() {
        // Given
        instructorRepository.save(instructor1);
        entityManager.flush();
        entityManager.clear();

        Instructor duplicateInstructor = new Instructor("john_doe", "differentPassword", "Jane", "Smith");

        // When & Then
        assertThatThrownBy(() -> {
            instructorRepository.save(duplicateInstructor);
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void save_NullRequiredFields_ShouldThrowException() {
        // Given
        Instructor invalidInstructor = new Instructor(null, null, null, null);

        // When & Then
        assertThatThrownBy(() -> {
            instructorRepository.save(invalidInstructor);
            entityManager.flush();
        }).isInstanceOf(Exception.class);
    }

    // Test cases for findAll() method
    @Test
    void findAll_MultipleInstructors_ShouldReturnAllInstructors() {
        // Given
        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        entityManager.flush();

        // When
        List<Instructor> instructors = instructorRepository.findAll();

        // Then
        assertThat(instructors).hasSize(2);
        assertThat(instructors).extracting(Instructor::getUsername)
                .containsExactlyInAnyOrder("john_doe", "kanav_singla");
    }

    @Test
    void findAll_NoInstructors_ShouldReturnEmptyList() {
        // When
        List<Instructor> instructors = instructorRepository.findAll();

        // Then
        assertThat(instructors).isEmpty();
    }

    // Test cases for findById() method
    @Test
    void findById_ExistingInstructor_ShouldReturnInstructor() {
        // Given
        Instructor savedInstructor = instructorRepository.save(instructor1);
        entityManager.flush();

        // When
        Optional<Instructor> found = instructorRepository.findById(savedInstructor.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john_doe");
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void findById_NonExistentInstructor_ShouldReturnEmpty() {
        // When
        Optional<Instructor> found = instructorRepository.findById(999L);

        // Then
        assertThat(found).isNotPresent();
    }

    // Test cases for findByUsername() method (custom query method)
    @Test
    void findByUsername_ExistingUsername_ShouldReturnInstructor() {
        // Given
        instructorRepository.save(instructor1);
        entityManager.flush();

        // When
        Optional<Instructor> found = instructorRepository.findByUsername("john_doe");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("john_doe");
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    void findByUsername_NonExistentUsername_ShouldReturnEmpty() {
        // Given
        instructorRepository.save(instructor1);
        entityManager.flush();

        // When
        Optional<Instructor> found = instructorRepository.findByUsername("nonexistent");

        // Then
        assertThat(found).isNotPresent();
    }

    @Test
    void findByUsername_NullUsername_ShouldReturnEmpty() {
        // When
        Optional<Instructor> found = instructorRepository.findByUsername(null);

        // Then
        assertThat(found).isNotPresent();
    }

    @Test
    void findByUsername_EmptyUsername_ShouldReturnEmpty() {
        // When
        Optional<Instructor> found = instructorRepository.findByUsername("");

        // Then
        assertThat(found).isNotPresent();
    }

    @Test
    void findByUsername_CaseInsensitive_ShouldWork() {
        // Given
        instructorRepository.save(instructor1);
        entityManager.flush();

        // When
        Optional<Instructor> found = instructorRepository.findByUsername("JOHN_DOE");

        // Then - depends on database collation, typically case-sensitive
        assertThat(found).isNotPresent(); // H2 is case-sensitive by default
    }

    // Test cases for deleteById() method
    @Test
    void deleteById_ExistingInstructor_ShouldRemoveInstructor() {
        // Given
        Instructor savedInstructor = instructorRepository.save(instructor1);
        entityManager.flush();
        Long instructorId = savedInstructor.getId();

        // When
        instructorRepository.deleteById(instructorId);
        entityManager.flush();

        // Then
        Optional<Instructor> found = instructorRepository.findById(instructorId);
        assertThat(found).isNotPresent();
    }

    @Test
    void deleteById_NonExistentInstructor_ShouldNotThrowException() {
        // When & Then (should not throw exception)
        instructorRepository.deleteById(999L);
        entityManager.flush();
    }

    // Test cases for count() method
    @Test
    void count_MultipleInstructors_ShouldReturnCorrectCount() {
        // Given
        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        entityManager.flush();

        // When
        long count = instructorRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void count_NoInstructors_ShouldReturnZero() {
        // When
        long count = instructorRepository.count();

        // Then
        assertThat(count).isZero();
    }

    // Integration test for complex scenarios
    @Test
    void saveAndFindByUsername_CompleteWorkflow_ShouldWork() {
        // Given & When
        Instructor savedInstructor = instructorRepository.save(instructor1);
        entityManager.flush();
        Optional<Instructor> foundById = instructorRepository.findById(savedInstructor.getId());
        Optional<Instructor> foundByUsername = instructorRepository.findByUsername("john_doe");

        // Then
        assertThat(foundById).isPresent();
        assertThat(foundByUsername).isPresent();
        assertThat(foundById.get()).isEqualTo(foundByUsername.get());
        assertThat(foundByUsername.get().getId()).isEqualTo(savedInstructor.getId());
    }

    // Test database constraints
    @Test
    void save_UsernameUniquenessConstraint_ShouldBeEnforced() {
        // Given
        instructorRepository.save(new Instructor("same_username", "pass1", "First", "User"));
        entityManager.flush();
        entityManager.clear();

        // When & Then
        assertThatThrownBy(() -> {
            instructorRepository.save(new Instructor("same_username", "pass2", "Second", "User"));
            entityManager.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    // Test that checks if entity relationships and cascading work (if you add them later)
    @Test
    void save_InstructorWithLongNames_ShouldHandleCorrectly() {
        // Given
        String longFirstName = "A".repeat(50);
        String longLastName = "B".repeat(50);
        Instructor instructorWithLongNames = new Instructor("long_names_user", "password", longFirstName, longLastName);

        // When
        Instructor saved = instructorRepository.save(instructorWithLongNames);
        entityManager.flush();

        // Then
        Optional<Instructor> found = instructorRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo(longFirstName);
        assertThat(found.get().getLastName()).isEqualTo(longLastName);
    }
}