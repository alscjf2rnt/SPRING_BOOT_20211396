package com.example1.demo.model.service;
import java.util.List;
// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example1.demo.model.domain.TestDB;
import com.example1.demo.model.repository.TestRepository;
import lombok.RequiredArgsConstructor;

@Service // 서비스 계층 명시 스프링 내부 자동 등록됨
@RequiredArgsConstructor // 생성자 생성
public class TestService {

    @Autowired // 객체 주입 자동화
    private TestRepository testRepository;

    public TestDB findByName(String name) {
    return (TestDB) testRepository.findByName(name);
    }

    // 새로운 findAll 메서드 추가
    public List<TestDB> findAll() {
        return testRepository.findAll();
    }

}
