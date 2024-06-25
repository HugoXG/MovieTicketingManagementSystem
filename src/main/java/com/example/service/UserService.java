package com.example.service;

import com.example.entity.Account;

public interface UserService {
    Account getAccount(String accountName, String password, String role);//获取用户
    boolean updatePassword(Account account, char[] newPassword);//更新密码
}
