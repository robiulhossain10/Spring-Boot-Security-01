package com.springsecurity.springbootsecurity01.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer studentId;

    private String studentName;

    private String address;

    private Float semisterCost;

    private Integer semisterNo;

}
