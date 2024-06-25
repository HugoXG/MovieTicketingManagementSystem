package com.example.service;

import com.example.entity.Ticket;
import com.example.entity.Account;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {
    List<Ticket> findAll(Account account);//根据账户查找电影票，不同的账户显示的内容不一样
    List<Ticket> findById(String id);//根据电影票Id获取票列表
    List<Ticket> findByStatus(String status);//根据票状态获取票列表
    List<Ticket> findByAccountId(String accountId);//根据用户Id获取票列表
    List<Ticket> findByAccountName(String accountName);//根据用户名称获取票列表
    List<Ticket> findByPurchaseDate(String purchaseDate);//根据购买日期获取票列表
    List<Ticket> findByViewDate(String viewDate);//根据观影日期获取票列表
    List<Ticket> findByFilmName(String movieName);//根据电影名称获取票列表
    int add(String accountName, String filmName, LocalDateTime viewDate);//添加电影票
    int stopById(int id);//停用电影票
    Object[] getColumnNames(Account account);//根据用户身份获取每列名称
    Object[][] toList(Account account, List<Ticket> list);//将票列表转成二维数组

    //以下是主要负责传输有关普通用户信息的方法
    List<Ticket> findByStatusAndAccount(String status, Account account);
    List<Ticket> findByPurchaseDateAndAccount(String purchaseDate, Account account);
    List<Ticket> findByViewDateAndAccount(String viewDate, Account account);
    List<Ticket> findByFilmNameAndAccount(String filmName, Account account);
}
