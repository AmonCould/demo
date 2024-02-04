package com.inspur.test.demo;
import com.inspur.test.demo.DemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemoRepository extends JpaRepository<DemoEntity,String> {
    @Override
    List<DemoEntity> findAllById(Iterable<String> iterable);

    @Query("SELECT demo FROM DemoEntity demo WHERE demo.id=:id")
    List<DemoEntity>findCostByID(@Param("id") String id);

    @Query(value = "SELECT ID, BALANCE,CODE,COST,NAME,NOTE FROM TESTDEMO WHERE  ID = '?1'",nativeQuery = true)
    List<DemoEntity> findCostByIDNoEntity(String id);
}
