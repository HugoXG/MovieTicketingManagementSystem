package com.example.controller;

import com.example.controller.adminPanel.AdminPanel;
import com.example.controller.userPanel.UserPanel;
import com.example.entity.Account;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import com.example.utils.SystemConstants;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 登录面板类，负责用户登录界面和功能
 */
public class LoginPanel extends JPanel {

    // 用户服务实例，用于处理用户相关的业务逻辑
    private static final UserService userService = new UserServiceImpl();

    /**
     * 构造方法，初始化登录面板
     */
    public LoginPanel() {
        // 设置面板大小
        this.setBounds(0, 0, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 添加用户名标签和文本框
        this.add(new JLabel("用户名"));
        JTextField nameField = new JTextField(10);
        this.add(nameField);

        // 添加密码标签和密码框
        this.add(new JLabel("密码"));
        JPasswordField passwordField = new JPasswordField(10);
        this.add(passwordField);

        // 添加管理员和普通用户单选按钮
        JRadioButton adminRadio = new JRadioButton("管理员");
        JRadioButton userRadio = new JRadioButton("普通用户");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(adminRadio);
        buttonGroup.add(userRadio);
        this.add(adminRadio);
        this.add(userRadio);

        // 添加登录按钮
        JButton loginBtn = new JButton("登录");
        this.add(loginBtn);

        // 为登录按钮添加鼠标点击事件监听器
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 获取输入的用户名和密码
                String username = nameField.getText();
                String password = new String(passwordField.getPassword());
                String role;
                Account account;

                // 判断用户选择的身份，并获取相应账户信息
                if (adminRadio.isSelected()) {
                    role = "admin";
                    //通过userService获取用户
                    account = userService.getAccount(username, password, role);
                } else if (userRadio.isSelected()) {
                    role = "user";
                    //通过userService获取用户
                    account = userService.getAccount(username, password, role);
                } else {
                    // 未选择身份时，弹出提示信息
                    JOptionPane.showMessageDialog(
                            loginBtn.getParent(),
                            "请选择身份",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // 检查账户信息是否正确
                if (account == null) {
                    // 用户名或密码错误时，弹出提示信息
                    JOptionPane.showMessageDialog(
                            loginBtn.getParent(),
                            "用户名或密码错误",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    // 账户信息正确，打开相应面板
                    openPanel(account);
                }
            }
        });
    }

    /**
     * 打开相应角色的面板
     * @param account 登录的账户信息
     */
    public static void openPanel(Account account) {
        if (account.getRole().equals("admin")) {
            // 打开管理员面板
            MainFrame.setContent(new AdminPanel(account));
        } else {
            // 打开普通用户面板
            MainFrame.setContent(new UserPanel(account));
        }
    }
}
