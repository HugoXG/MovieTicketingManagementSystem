package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    int id;
    int status;
    String filmName;
    LocalDateTime releasedDate;
    String filmTime;
    int audience;
}
