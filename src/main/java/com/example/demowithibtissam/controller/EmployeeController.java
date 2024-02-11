package com.example.demowithibtissam.controller;

import com.example.demowithibtissam.model.Employee;
import com.example.demowithibtissam.service.EmailService;
import com.example.demowithibtissam.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmailService emailHandler;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/project")
    public ResponseEntity<String> getEmployeeProject(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(e -> ResponseEntity.ok(e.getProjets())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/project-description")
    public ResponseEntity<String> getEmployeeProjectDescription(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(e -> ResponseEntity.ok(e.getProject_description())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee addedEmployee = employeeService.addEmployee(employee);
        return ResponseEntity.ok(addedEmployee);
    }

    @PutMapping("/{id}/project")
    public ResponseEntity<Void> updateEmployeeProject(@PathVariable Long id, @RequestBody String project) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);

        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            existingEmployee.setProjets(project);
            emailHandler.notifyProject(project);
            employeeService.addEmployee(existingEmployee);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/project-description")
    public ResponseEntity<Void> updateEmployeeProjectDescription(@PathVariable Long id, @RequestBody String projectDescription) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);

        if (employee.isPresent()) {
            Employee existingEmployee = employee.get();
            employeeService.addEmployee(existingEmployee);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }


}