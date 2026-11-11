
package com.example.student_management.service;

import com.example.student_management.dto.StudentRequestDTO;
import com.example.student_management.dto.StudentResponseDTO;
import com.example.student_management.exception.StudentNotFoundException;
import com.example.student_management.model.Student;
import com.example.student_management.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // Convert Entity to Response DTO
    private StudentResponseDTO convertToDTO(Student student) {

        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getDepartment(),
                student.getAge()
        );
    }

    // Add Student
    public StudentResponseDTO addStudent(StudentRequestDTO dto) {

        Student student = new Student();

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setDepartment(dto.getDepartment());
        student.setAge(dto.getAge());

        Student savedStudent = repository.save(student);

        return convertToDTO(savedStudent);
    }

    // Get All Students
    public List<StudentResponseDTO> getAllStudents() {

        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Get Student By ID
    public StudentResponseDTO getStudentById(Long id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        return convertToDTO(student);
    }

    // Update Student
    public StudentResponseDTO updateStudent(
            Long id, StudentRequestDTO dto) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setDepartment(dto.getDepartment());
        student.setAge(dto.getAge());

        Student updatedStudent = repository.save(student);

        return convertToDTO(updatedStudent);
    }

    // Delete Student
    public void deleteStudent(Long id) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with ID " + id + " not found"
                        )
                );

        repository.delete(student);
    }

    // Search Student
    public List<StudentResponseDTO> searchStudents(String name) {

        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
}
