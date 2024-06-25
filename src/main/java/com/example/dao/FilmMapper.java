package com.example.dao;

import com.example.entity.Film;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FilmMapper {
    List<Film> findAll();
    List<Film> findById(@Param("id") int id);
    List<Film> findByStatus(@Param("status") int status);
    Film findByFilmNameEquals(@Param("film_name_equals") String filmName);
    List<Film> findByFilmNameLike(@Param("film_name_like") String filmName);
    List<Film> findByFilmNameAndStatus(@Param("film_name") String filmName, @Param("status") int status);
    List<Film> findByReleasedDate(@Param("released_date") String releasedDate);
    List<Film> findByFilmTime(@Param("film_time") String filmTime);
    List<Film> findByAudience(@Param("audience") int audience);
    boolean updateAudience(@Param("audience") int audience, @Param("id") int id);

    boolean add(@Param("film_name") String filmName,
                @Param("film_time")String filmTime,
                @Param("released_date")String releasedDate);

    boolean stopById(@Param("id") int id);
}
