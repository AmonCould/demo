package com.inspur.test.demo;
import com.inspur.test.demo.DemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoRepository extends JpaRepository<DemoEntity,String> {
    @Override
    List<DemoEntity> findAllById(Iterable<String> iterable);
}
