package com.example.dao;

import com.example.entity.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    Account getAccount(@Param("account_name") String accountName,
                    @Param("password") String password,
                    @Param("role") String role);
    boolean updatePassword(@Param("id") int id, @Param("password") String newPassword);
    Account getAccountByName(@Param("account_name")String accountName);

}
