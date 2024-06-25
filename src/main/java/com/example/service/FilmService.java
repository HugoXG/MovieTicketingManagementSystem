package com.example.service;

import com.example.entity.Film;
import com.example.entity.Account;

import java.time.LocalDateTime;
import java.util.List;

public interface FilmService {
    List<Film> findAll(Account account);//通过账户身份来获取电影列表，身份不同展示的内容不同
    List<Film> findById(String id);//通过电影Id来获取电影列表
    List<Film> findByStatus(String status);//通过电影状态来获取电影列表
    List<Film> findByFilmName(String filmName);//通过电影名称来获取电影列表
    List<Film> findByFilmNameAndStatus(String filmName, int status);
    //通过电影名称和状态来获取电影列表，admin展示全部状态的电影，user只展示已上映的电影
    List<Film> findByReleasedDate(String releasedDate);//通过电影上映日期来获取电影列表
    List<Film> findByFilmTime(String filmTime);//通过电影时长来获取电影列表
    List<Film> findByAudience(String audience);//通过观影人数来获取电影列表
    int add(String filmName, int filmTime, LocalDateTime releasedDate);//添加电影
    boolean stopById(int id);//下架电影
    Object[] getColumnNames(Account account);//获取每列的名称
    Object[][] toList(Account account, List<Film> list);//将电影列表转为二维数组并发给前端
}
