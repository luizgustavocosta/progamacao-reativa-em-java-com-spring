package com.costa.luiz.reactive.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "customers")
public class Customer {

    @Id
    private Long id;
    private String name;
    @Column("middlename")
    private String middleName;
    @Column("lastname")
    private String lastName;
    @Column("becamecustomer")
    private LocalDate becameCustomer;
}
