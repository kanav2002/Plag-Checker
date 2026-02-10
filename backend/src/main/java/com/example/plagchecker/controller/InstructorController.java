package com.example.plagchecker.controller;

import com.example.plagchecker.model.Instructor;
import com.example.plagchecker.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/instructors")
@CrossOrigin(origins = "http://localhost:3000")
public class InstructorController {
    
    @Autowired
    private InstructorService instructorService;
    
    @PostMapping
    public ResponseEntity<Instructor> createInstructor(@RequestBody Instructor instructor) {
        try {
            Instructor createdInstructor = instructorService.createInstructor(instructor);
            return ResponseEntity.ok(createdInstructor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        List<Instructor> instructors = instructorService.getAllInstructors();
        return ResponseEntity.ok(instructors);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        Optional<Instructor> instructor = instructorService.getInstructorById(id);
        return instructor.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<Instructor> getInstructorByUsername(@PathVariable String username) {
        Optional<Instructor> instructor = instructorService.getInstructorByUsername(username);
        return instructor.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
}