package com.example.controller.userPanel;

import com.example.controller.LoginPanel;
import com.example.controller.MainFrame;
import com.example.entity.Account;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 修改密码面板类，提供用户修改密码的界面和功能
 */
public class PasswordPanel extends JInternalFrame {

    // 用户服务实例，用于处理用户相关的业务逻辑
    private static final UserService userService = new UserServiceImpl();

    /**
     * 构造方法，初始化修改密码面板
     * @param account 登录的账户信息
     */
    public PasswordPanel(Account account) {
        // 调用父类构造方法，设置内部框架属性
        super("修改密码", true, true, true, true);

        // 创建一个面板用于放置组件
        JPanel panel = new JPanel();
        this.setContentPane(panel);

        // 添加旧密码标签和文本框
        panel.add(new JLabel("旧密码"));
        JTextField oldPasswordField = new JTextField(10);
        this.add(oldPasswordField);

        // 添加新密码标签和密码框
        panel.add(new JLabel("新密码"));
        JPasswordField newPasswordField = new JPasswordField(10);
        panel.add(newPasswordField);

        // 添加提交按钮
        JButton button = new JButton("提交");

        // 为提交按钮添加鼠标点击事件监听器
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 获取输入的旧密码和新密码
                String oldPassword = oldPasswordField.getText();
                char[] newPassword = newPasswordField.getPassword();
                System.out.println(account.getPassword());

                // 验证旧密码是否正确
                if (!oldPassword.equals(account.getPassword())) {
                    JOptionPane.showMessageDialog(
                            button.getParent(),
                            "修改失败，旧密码错误",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else if (newPassword.length == 0) {
                    // 验证新密码是否为空
                    JOptionPane.showMessageDialog(
                            button.getParent(),
                            "修改失败，新密码不能为空",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else if (new String(newPassword).equals(oldPassword)) {
                    // 验证新密码是否与旧密码相同
                    JOptionPane.showMessageDialog(
                            button.getParent(),
                            "修改失败，新密码与旧密码相同",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else if (newPassword.length > 20) {
                    // 验证新密码长度是否超过限制
                    JOptionPane.showMessageDialog(
                            button.getParent(),
                            "修改失败，新密码过长，请重新输入",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    // 更新密码,通过userService.updatePassword()来更新密码
                    if (userService.updatePassword(account, newPassword)) {
                        JOptionPane.showMessageDialog(
                                button.getParent(),
                                "修改成功",
                                "系统提示",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                        MainFrame.setContent(new LoginPanel());
                    } else {
                        JOptionPane.showMessageDialog(
                                button.getParent(),
                                "修改失败，请检查服务器",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
            }
        });

        // 将提交按钮添加到面板
        panel.add(button);

        // 设置面板可见
        this.setVisible(true);
    }
}
