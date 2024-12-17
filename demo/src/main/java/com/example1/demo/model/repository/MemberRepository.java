package com.example1.demo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example1.demo.model.domain.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.example1.demo.model.dto.AddMemberRequest;
import com.example1.demo.model.domain.Member; 

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email); 
}
