package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    int id;
    int status;
    int accountId;
    String accountName;
    LocalDateTime purchaseDate;
    LocalDateTime viewDate;
    String filmName;
}
