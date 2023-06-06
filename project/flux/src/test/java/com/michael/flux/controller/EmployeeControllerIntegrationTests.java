package com.michael.flux.controller;

import com.michael.flux.payload.request.EmployeeDto;
import com.michael.flux.repository.EmployeeRepository;
import com.michael.flux.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTests {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository repository;

    private EmployeeDto employeeDto;
    private String id = "employeeId";

    @BeforeEach
    public void setup() {
        repository.deleteAll().subscribe();
        employeeDto = EmployeeDto.builder()
                .id(id)
                .firstName("Michael")
                .lastName("Royf")
                .email("michael@gmail.com")
                .build();

    }


    @DisplayName("JUnit test for save Employee method")
    @Test
    public void testSaveEmployee() {
        //given - precondition or setup
        webTestClient.post().uri("/api/v1/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
        // when -action or the behavior we are going to test

        //then - verify the output
    }


    @DisplayName("JUnit test for get employee by id method")
    @Test
    public void testGetSingleEmployee() {
        //given - precondition or setup
        EmployeeDto employee1 = EmployeeDto.builder()
                .id("12345")
                .firstName("Dasha")
                .lastName("Petrova")
                .email("dasha@gmail.com")
                .build();
        EmployeeDto savedEmployee = employeeService.saveEmployee(employee1).block();


        // when -action or the behavior we are going to test

        webTestClient.get().uri("api/v1/employees/{employeeId}", Collections.singletonMap("employeeId", savedEmployee.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.firstName").isEqualTo(employee1.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employee1.getLastName())
                .jsonPath("$.email").isEqualTo(employee1.getEmail());

        //then - verify the output
    }


    @DisplayName("JUnit test for get all Employees method")
    @Test
    public void getAllEmployees() {
        //given - precondition or setup
        webTestClient.get().uri("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
        // when -action or the behavior we are going to test

        //then - verify the output
    }


    @DisplayName("JUnit test for update Employee method")
    @Test
    public void testUpdateEmployee() {
        //given - precondition or setup
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();
        EmployeeDto updatedEmployee = EmployeeDto.builder()
                .firstName("MICHAEL")
                .lastName("ROYF")
                .email("MICHAL@GMAIL.COM")
                .build();

        webTestClient.put().uri("/api/v1/employees/update/{employeeId}", Collections.singletonMap("employeeId", savedEmployee.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedEmployee), EmployeeDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
                .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
                .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
        // when -action or the behavior we are going to test

        //then - verify the output
    }


    @DisplayName("JUnit test for delete Employee method")
    @Test
    public void testDeleteEmployee() {
        //given - precondition or setup
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();



        // when -action or the behavior we are going to test

        webTestClient.delete().uri("/api/v1/employees/delete/{employeeId}", Collections.singletonMap("employeeId", savedEmployee.getId()))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);



        //then - verify the output
    }


}
