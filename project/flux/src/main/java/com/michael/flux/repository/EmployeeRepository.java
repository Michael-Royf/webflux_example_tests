package com.michael.flux.repository;

import com.michael.flux.entity.Employee;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface EmployeeRepository  extends ReactiveCrudRepository<Employee, String> {




}
