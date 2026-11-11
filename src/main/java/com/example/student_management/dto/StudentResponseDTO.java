
package com.example.student_management.dto;

public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String department;
    private int age;

    public StudentResponseDTO(
            Long id,
            String name,
            String email,
            String department,
            int age) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public int getAge() {
        return age;
    }
}
