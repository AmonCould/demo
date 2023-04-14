package com.inspur.test.demo;
import com.inspur.test.demo.DemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository<DemoEntity,String> {
}
