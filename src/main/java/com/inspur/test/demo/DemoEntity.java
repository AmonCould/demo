package com.inspur.test.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "testdemo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@OptimisticLocking
public class DemoEntity{

    @Id
    @Column(length = 36, name = "id", nullable = false)
    @GenericGenerator(name = "jpa-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "jpa-uuid")
    private String id;//主键

    @Column(length = 128)
    private String code;

    @Column(length = 128)
    private String name;

    @Basic
    private BigDecimal cost;

    @Basic
    private BigDecimal balance;

    @Column(length = 512)
    private String note;
}
