package com.engrisk.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    public static final int CAPACITY = 35;

    private Long id;

    private Exam exam;

    private String name;

    public Room(Exam exam, String name) {
        this.exam = exam;
        this.name = name;
    }
}