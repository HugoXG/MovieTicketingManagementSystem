package com.example.service.impl;

import com.example.dao.FilmMapper;
import com.example.dao.TicketMapper;
import com.example.entity.Film;
import com.example.entity.Account;
import com.example.entity.Ticket;
import com.example.service.FilmService;
import com.example.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 电影服务实现类，实现了FilmService接口，提供电影相关的业务逻辑
 */
public class FilmServiceImpl implements FilmService {
//    资源在 try 代码块执行完毕后会自动关闭
//    获取一个 MyBatis 的 SqlSession 对象
//    SqlSession 是 MyBatis 中用于执行映射的 SQL 语句、提交或回滚事务和获取映射器（Mapper）实例的关键接口
//    try (SqlSession sqlSession = MybatisUtil.getSession()) {
//        FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);
          //通过 sqlSession 获取了一个 FilmMapper 类的实例
//    }
//    另外两个Service实现类同理

    /**
     * 根据账号角色获取所有电影列表
     * @param account 当前用户账号
     * @return 电影列表
     */
    @Override
    public List<Film> findAll(Account account) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //判断用户身份并返回相应的数据
            if (account.getRole().equals("admin")) {
                return filmMapper.findAll(); // 调用filmMapper接口的findAll()方法，管理员获取所有电影
            } else {
                return filmMapper.findByStatus(1); // 普通用户获取上映中的电影
            }
        }
    }

    /**
     * 根据电影ID查找电影
     * @param id 电影ID
     * @return 电影列表
     */
    @Override
    public List<Film> findById(String id) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findById(Integer.parseInt(id));
        }
    }

    /**
     * 根据电影状态查找电影
     * @param status 电影状态
     * @return 电影列表
     */
    @Override
    public List<Film> findByStatus(String status) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findByStatus(Integer.parseInt(status));
        }
    }

    /**
     * 根据电影名称模糊查找电影
     * @param filmName 电影名称
     * @return 电影列表
     */
    @Override
    public List<Film> findByFilmName(String filmName) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findByFilmNameLike(filmName);
        }
    }

    /**
     * 根据电影名称和状态查找电影
     * @param filmName 电影名称
     * @param status 电影状态
     * @return 电影列表
     */
    @Override
    public List<Film> findByFilmNameAndStatus(String filmName, int status) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findByFilmNameAndStatus(filmName, status);
        }
    }

    /**
     * 根据上映日期查找电影
     * @param releasedDate 上映日期
     * @return 电影列表
     */
    @Override
    public List<Film> findByReleasedDate(String releasedDate) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findByReleasedDate(releasedDate);
        }
    }

    /**
     * 根据电影时长查找电影
     * @param filmTime 电影时长
     * @return 电影列表
     */
    @Override
    public List<Film> findByFilmTime(String filmTime) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findByFilmTime(filmTime);
        }
    }

    /**
     * 根据观影人数查找电影
     * @param audience 观影人数
     * @return 电影列表
     */
    @Override
    public List<Film> findByAudience(String audience) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //调用接口
            return filmMapper.findByAudience(Integer.parseInt(audience));
        }
    }

    /**
     * 添加电影
     * @param filmName 电影名称
     * @param filmTime 电影时长
     * @param releasedDate 上映日期
     * @return 添加结果 0: 成功, 1: 电影已存在, 2: 上映日期已过
     * 1、首先获取前端发送来的数据（电影名称，电影时长，上映日期）
     * 2、通过filmMapper.findByFilmNameEquals方法根据电影名称来获取电影实例
     * 3、判断电影实例是否存在，如果存在并且电影已上映则返回1，如果上映时间早于本地时间则返回3，否则返回0
     */
    @Override
    public int add(String filmName, int filmTime, LocalDateTime releasedDate) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            //获取本地时间
            LocalDateTime now = LocalDateTime.now();

            Film film = filmMapper.findByFilmNameEquals("《" + filmName + "》");

            if (film != null && film.getStatus() == 1) {
                return 1; // 电影已存在
            } else if (releasedDate.isBefore(now)) {
                return 2; // 上映日期已过
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String releasedDateTime = releasedDate.format(formatter);
                filmMapper.add("《" + filmName + "》", filmTime + "min", releasedDateTime);
                return 0; // 添加成功
            }
        }
    }

    /**
     * 根据ID停映电影
     * @param id 电影ID
     * @return 是否成功停映
     *
     * 一部电影有多张电影票
     * 通过id下架电影，关于此电影的电影票也会同时停用
     */
    @Override
    public boolean stopById(int id) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);

            //调用ticketMapper.findByFilmName获取此电影相关的电影票列表
            List<Ticket> ticketList = ticketMapper.findByFilmName(filmMapper.findById(id).get(0).getFilmName());

            //for循环停用电影票，为了保险起见，期间判断电影票是否停用失败
            for (Ticket ticket : ticketList) {
                if (!ticketMapper.stopById(ticket.getId())) {
                    return false; // 电影票停用失败
                }
            }
            return filmMapper.stopById(id); // 电影停映成功
        }
    }

    /**
     * 获取表格列名
     * @param account 当前用户账号
     * @return 列名数组
     *
     * 根据用户身份返回不同的列名称，为什么要用Object[]一维数组，因为前端要这样的，不关我事
     */
    @Override
    public Object[] getColumnNames(Account account) {
        String role = account.getRole();
        if (role.equals("admin")) {
            return new Object[]{"序号", "ID", "状态", "电影名称", "上映时间", "电影时长", "观影人数"};
        } else {
            return new Object[]{"序号", "电影名称", "上映时间", "电影时长", "观影人数"};
        }
    }

    /**
     * 将电影列表转换为表格显示的二维数组
     * @param account 当前用户账号
     * @param list 电影列表
     * @return 二维数组
     *
     * 根据用户身份返回不同的电影列表，为什么要用Object[][]二维数组，因为前端要这样的，不关我事
     */
    @Override
    public Object[][] toList(Account account, List<Film> list) {
        String role = account.getRole();//获取用户身份
        Object[][] result = new Object[list.size()][]; //新建二维数组，大小为电影个数
        int rowNum = 1;//rowNum表示序号

        //判断身份
        if (role.equals("admin")) {
            for (int i = 0; i < list.size(); i++, rowNum++) {
                //从列表中逐个获取Film实例
                Film film = list.get(i);

                //添加到二维数组
                result[i] = new Object[]{
                        rowNum,
                        film.getId(),
                        film.getStatus(),
                        film.getFilmName(),
                        film.getReleasedDate(),
                        film.getFilmTime(),
                        film.getAudience()
                };
            }
        } else {
            for (int i = 0; i < list.size(); i++, rowNum++) {
                //从列表中逐个获取Film实例
                Film film = list.get(i);

                //添加到二维数组
                result[i] = new Object[]{
                        rowNum,
                        film.getFilmName(),
                        film.getReleasedDate(),
                        film.getFilmTime(),
                        film.getAudience()
                };
            }
        }
        return result;
    }
}
