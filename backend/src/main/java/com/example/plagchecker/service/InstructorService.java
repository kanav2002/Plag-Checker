package com.example.plagchecker.service;

import com.example.plagchecker.model.Instructor;
import com.example.plagchecker.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
    
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }
    
    public Optional<Instructor> getInstructorById(Long id) {
        return instructorRepository.findById(id);
    }
    
    public Optional<Instructor> getInstructorByUsername(String username) {
        return instructorRepository.findByUsername(username);
    }

    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        Optional<Instructor> instructorOpt = instructorRepository.findByUsername(username);
        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            if (instructor.getPassword().equals(oldPassword)) {
                instructor.setPassword(newPassword);
                instructorRepository.save(instructor);
                return true;
            }
        }
        return false;
    }

    public Optional<Instructor> authenticate(String username, String password) {
        return instructorRepository.findByUsername(username)
                .filter(instructor -> instructor.getPassword().equals(password));
    }
}