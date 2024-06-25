package com.example.service.impl;

import com.example.dao.AccountMapper;
import com.example.dao.FilmMapper;
import com.example.dao.TicketMapper;
import com.example.entity.Account;
import com.example.entity.Film;
import com.example.entity.Ticket;
import com.example.service.TicketService;
import com.example.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TicketServiceImpl implements TicketService {
    @Override
    public List<Ticket> findAll(Account account) {
        try(SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);

            int accountId = account.getId();
            String role = account.getRole();

            //根据用户身份获取票列表
            if (role.equals("admin")) { // 如果是管理员角色
                return ticketMapper.findAll(); // 返回所有票据
            } else {
                return ticketMapper.findByAccountId(accountId); // 返回该账户ID的票据
            }
        }
    }

    // 根据票据ID查找票据
    @Override
    public List<Ticket> findById(String id) {
        try(SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findById(Integer.parseInt(id)); // 将字符串ID转换为整数，并查询
        }
    }

    // 根据票据状态查找票据
    @Override
    public List<Ticket> findByStatus(String status) {
        try(SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByStatus(Integer.parseInt(status)); // 将字符串状态转换为整数，并查询
        }
    }

    // 根据账户ID查找票据
    @Override
    public List<Ticket> findByAccountId(String accountId) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByAccountId(Integer.parseInt(accountId)); // 将字符串账户ID转换为整数，并查询
        }
    }

    // 根据账户名称查找票据
    @Override
    public List<Ticket> findByAccountName(String accountName) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByAccountName(accountName); // 直接使用账户名称查询
        }
    }

    // 根据购票日期查找票据
    @Override
    public List<Ticket> findByPurchaseDate(String purchaseDate) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByPurchaseDate(purchaseDate); // 直接使用购票日期查询
        }
    }

    // 根据观影日期查找票据
    @Override
    public List<Ticket> findByViewDate(String viewDate) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByViewDate(viewDate); // 直接使用观影日期查询
        }
    }

    // 根据电影名称查找票据
    @Override
    public List<Ticket> findByFilmName(String movieName) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByFilmName(movieName); // 直接使用电影名称查询
        }
    }

    // 添加票据记录
    @Override
    public int add(String accountName, String filmName, LocalDateTime viewDate) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);
            AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);


            Account account = accountMapper.getAccountByName(accountName); // 根据账户名称获取账户
            Film film = filmMapper.findByFilmNameEquals("《"+filmName+"》"); // 根据电影名称获取电影

            if (account == null || film == null) {
                return 1; // 如果账户或电影不存在，返回1
            } else {
                if (film.getAudience() >= 50) {
                    return 2; // 如果观众数超过50，返回2
                } else if (viewDate.isBefore(film.getReleasedDate())){
                    return 3; // 如果观影日期在电影发布日期之前，返回3
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String viewDateTime = viewDate.format(formatter); // 格式化观影日期时间
                    ticketMapper.add(account.getId(), film.getId(), viewDateTime); // 添加票据记录
                    filmMapper.updateAudience(film.getAudience()+1, film.getId()); // 更新电影的观众数
                    return 0; // 成功添加，返回0
                }
            }
        }
    }

    // 根据票据ID停止服务
    @Override
    public int stopById(int id) {
        try (SqlSession sqlSession = MybatisUtil.getSession()){
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            FilmMapper filmMapper = sqlSession.getMapper(FilmMapper.class);

            LocalDateTime stopTime = LocalDateTime.now(); // 获取当前时间
            LocalDateTime viewTime = ticketMapper.findById(id).get(0).getViewDate(); // 获取票据的观影时间
            Film film = filmMapper.findByFilmNameEquals(ticketMapper.findById(id).get(0).getFilmName()); // 获取相关电影信息

            if (ticketMapper.findById(id).get(0).getStatus() == 0) {
                return 1; // 如果票据状态为0（未使用），返回1
            } else if (stopTime.isAfter(viewTime)) {
                return 2; // 如果当前时间晚于观影时间，返回2
            } else {
                ticketMapper.stopById(id);
                filmMapper.updateAudience(film.getAudience()-1, film.getId()); // 减少电影的观众数
                return 0; // 成功停止服务，返回0
            }
        }
    }

    // 根据账户角色获取列名
    @Override
    public Object[] getColumnNames(Account account) {
        String role = account.getRole();
        if (role.equals("admin")) { // 如果是管理员
            return new Object[]{"序号", "ID", "状态", "用户ID", "用户名称","购票时间", "观影时间", "电影"}; // 返回更详细的列信息
        } else {
            return new Object[]{"序号", "状态","购票时间", "观影时间", "电影"}; // 返回普通用户的列信息
        }
    }

    // 将票据列表转换为对象数组，用于显示在表格中，和FilmServiceImpl中的add方法同理
    @Override
    public Object[][] toList(Account account, List<Ticket> list) {
        String role = account.getRole();
        Object[][] result = new Object[list.size()][];
        int cnt = 1;
        if (role.equals("admin")) { // 如果是管理员
            for (int i = 0; i < list.size(); i++, cnt++) {
                Ticket ticket = list.get(i);
                result[i] = new Object[]{
                        cnt,
                        ticket.getId(),
                        ticket.getStatus(),
                        ticket.getAccountId(),
                        ticket.getAccountName(),
                        ticket.getPurchaseDate(),
                        ticket.getViewDate(),
                        ticket.getFilmName()
                };
            }
        } else {
            for (int i = 0; i < list.size(); i++, cnt++) {
                Ticket ticket = list.get(i);
                String status;
                if (ticket.getStatus() == 1) {
                    status = "可用"; // 状态为1表示票据可用
                } else {
                    status = "已退票"; // 否则为已退票
                }
                result[i] = new Object[]{
                        cnt,
                        status,
                        ticket.getPurchaseDate(),
                        ticket.getViewDate(),
                        ticket.getFilmName()
                };
            }
        }
        return result;
    }


    //下面四个方法主要用于普通用户的电影票查询功能

    //根据电影票状态和账户获取电影票
    @Override
    public List<Ticket> findByStatusAndAccount(String status, Account account) {
        try (SqlSession sqlSession = MybatisUtil.getSession()){
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            if (status.equals("可用")) {
                return ticketMapper.findByStatusAndAccountId(1, account.getId());
            } else if (status.equals("已退票")){
                return ticketMapper.findByStatusAndAccountId(0, account.getId());
            } else {
                return null;
            }
        }
    }

    //根据购买日期和账户获取电影票
    @Override
    public List<Ticket> findByPurchaseDateAndAccount(String purchaseDate, Account account) {
        try (SqlSession sqlSession = MybatisUtil.getSession()){
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByPurchaseDateAndAccountId(purchaseDate, account.getId());
        }
    }

    //根据观影日期和账户获取电影票
    @Override
    public List<Ticket> findByViewDateAndAccount(String viewDate, Account account) {
        try (SqlSession sqlSession = MybatisUtil.getSession()){
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByViewDateAndAccountId(viewDate, account.getId());
        }
    }

    //根据电影名称和账户获取电影票
    @Override
    public List<Ticket> findByFilmNameAndAccount(String filmName, Account account) {
        try (SqlSession sqlSession = MybatisUtil.getSession()){
            TicketMapper ticketMapper = sqlSession.getMapper(TicketMapper.class);
            return ticketMapper.findByFilmNameAndAccountId(filmName, account.getId());
        }
    }
}
