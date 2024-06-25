package com.example.service.impl;

import com.example.dao.AccountMapper;
import com.example.entity.Account;
import com.example.service.UserService;
import com.example.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

public class UserServiceImpl implements UserService {
    @Override
    public Account getAccount(String username, String password, String role) {
        try(SqlSession sqlSession = MybatisUtil.getSession()) {
            AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
            return accountMapper.getAccount(username, password, role);
        }
    }

    @Override
    public boolean updatePassword(Account account, char[] newPassword) {
        try(SqlSession sqlSession = MybatisUtil.getSession()) {
            AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
            return accountMapper.updatePassword(account.getId(), new String(newPassword));
        }
    }
}
