
package com.example.student_management.controller;

import com.example.student_management.dto.StudentRequestDTO;
import com.example.student_management.dto.StudentResponseDTO;
import com.example.student_management.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(
            @Valid @RequestBody StudentRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.addStudent(dto));
    }

    // READ ALL
    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return service.getAllStudents();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(
            @PathVariable Long id) {

        return service.getStudentById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO dto) {

        return service.updateStudent(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable Long id) {

        service.deleteStudent(id);

        return ResponseEntity.noContent().build();
    }

    // SEARCH
    @GetMapping("/search")
    public List<StudentResponseDTO> searchStudents(
            @RequestParam String name) {

        return service.searchStudents(name);
    }
}
