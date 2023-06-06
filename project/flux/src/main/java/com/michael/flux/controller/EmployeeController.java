package com.michael.flux.controller;

import com.michael.flux.payload.request.EmployeeDto;
import com.michael.flux.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    //Build Reactive Save Employee REST API

    @PostMapping("/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.saveEmployee(employeeDto);
    }

    //Build Reactive Get Single Employee REST API

    @GetMapping("/{employeeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<EmployeeDto> getEmployeeById(@PathVariable("employeeId") String employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    //Build Reactive Get All Employees REST API
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


    //Build Reactive Update Employee REST API
    @PutMapping("/update/{employeeId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<EmployeeDto> updateEmployee(@PathVariable("employeeId") String employeeId,
                                            @RequestBody EmployeeDto employeeDto) {
        return employeeService.updateEmployee(employeeId, employeeDto);
    }

    //Build Reactive Delete Employee REST API

    @DeleteMapping("/delete/{employeeId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable("employeeId") String employeeId){
        return employeeService.deleteEmployee(employeeId);
    }

}
