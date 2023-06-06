package com.michael.flux.controller;

import com.michael.flux.payload.request.EmployeeDto;
import com.michael.flux.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;
    private EmployeeDto employeeDto;
    private String id = "employeeId";

    @BeforeEach
    public void setup() {
        //  employeeRepository.deleteAll();
        employeeDto = EmployeeDto.builder()
                .id(id)
                .firstName("Michael")
                .lastName("Royf")
                .email("michael@gmail.com")
                .build();
    }


    @DisplayName("JUnit test for save Employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        //given - precondition or setup
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));
        //when -action or the behavior we are going to test
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/v1/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();
        //then - verify the output
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }


    @DisplayName("JUnit test for get employee by ID method")
    @Test
    public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {
        //given - precondition or setup
        BDDMockito.given(employeeService.getEmployee(id))
                .willReturn(Mono.just(employeeDto));
        // when -action or the behavior we are going to test
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/v1/employees/{employeeId}", Collections.singletonMap("employeeId", id))
                .exchange();
        //then - verify the output

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }


    @DisplayName("JUnit test for get all Employees method")
    @Test
    public void givenListOfEmployees_whenGetAllEmployee_thenReturnListOfEmployees() {
        //given - precondition or setup
        EmployeeDto employeeDto1 = EmployeeDto.builder()
                .id("employeeId1")
                .firstName("Anna")
                .lastName("White")
                .email("anna@gmail.com")
                .build();
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        employeeDtoList.add(employeeDto);
        employeeDtoList.add(employeeDto1);

        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(employeeDtoList);

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeDtoFlux);

        // when -action or the behavior we are going to test

        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/v1/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then - verify the output

        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }


    @DisplayName("JUnit test for update Employee method")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() {
        //given - precondition or setup
        BDDMockito.given(employeeService.updateEmployee(
                        ArgumentMatchers.any(String.class),
                        ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));
        // when -action or the behavior we are going to test
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/v1/employees/update/{employeeId}", Collections.singletonMap("employeeId", id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();
        //then - verify the output

        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }


    @DisplayName("JUnit test for delete Employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing() {
        //given - precondition or setup
        Mono<Void> voidMono = Mono.empty();
        BDDMockito.given(employeeService.deleteEmployee(id))
                .willReturn(voidMono);

        // when -action or the behavior we are going to test
        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/v1/employees/delete/{employeeId}", Collections.singletonMap("employeeId", id))
                .exchange();
        //then - verify the output

        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

}