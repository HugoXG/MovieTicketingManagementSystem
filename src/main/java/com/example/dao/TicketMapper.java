package com.example.dao;

import com.example.entity.Ticket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TicketMapper {
    List<Ticket> findAll();
    List<Ticket> findByStatus(@Param("status") int status);
    List<Ticket> findById(@Param("id") int id);
    List<Ticket> findByAccountId(@Param("account_id") int accountId);
    List<Ticket> findByAccountName(@Param("account_name") String accountName);
    List<Ticket> findByPurchaseDate(@Param("purchase_date") String purchaseDate);
    List<Ticket> findByViewDate(@Param("view_date") String viewDate);

    List<Ticket> findByFilmName(@Param("film_name") String filmName);
    boolean add(@Param("account_id") int accountId,
                @Param("film_id") int filmId,
                @Param("view_date") String viewDate);
    boolean stopById(@Param("id") int id);

    List<Ticket> findByStatusAndAccountId(@Param("status") int status, @Param("account_id") int id);

    List<Ticket> findByPurchaseDateAndAccountId(@Param("purchase_date") String purchaseDate, @Param("account_id") int id);

    List<Ticket> findByViewDateAndAccountId(@Param("view_date") String viewDate, @Param("account_id") int id);

    List<Ticket> findByFilmNameAndAccountId(@Param("film_name") String filmName, @Param("account_id") int id);
}
