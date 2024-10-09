package com.example1.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example1.demo.model.domain.TestDB;

@Repository
public interface TestRepository extends JpaRepository<TestDB, Long> {

    //Find all TestDB entities by a name
    TestDB findByName(String name);

}