package com.example.controller.userPanel;

import com.example.controller.*;
import com.example.entity.Account;
import com.example.utils.SystemConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 普通用户面板类，提供用户功能界面
 */
public class UserPanel extends JPanel {

    // 静态桌面面板，用于显示内容面板
    private static final JDesktopPane contentPanel = new JDesktopPane();

    /**
     * 构造方法，初始化用户面板
     * @param account 登录的账户信息
     */
    public UserPanel(Account account) {
        // 设置面板大小
        this.setBounds(0, 0, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 设置内容面板大小并移除所有组件
        contentPanel.setBounds(0, 20, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);
        contentPanel.removeAll();

        // 添加内容面板到用户面板的中心位置
        this.add(contentPanel, BorderLayout.CENTER);

        // 显示账户名称
        this.add(new JLabel(account.getAccountName()));

        // 创建菜单栏和各个菜单项
        JMenuBar menuBar = new JMenuBar();
        JMenu filmMenu = new JMenu("电影查询");
        JMenu ticketMenu = new JMenu("订单管理");
        JMenu passwordMenu = new JMenu("修改密码");
        JMenu logoutMenu = new JMenu("退出登录");

        // 将菜单项添加到菜单栏
        menuBar.add(filmMenu);
        menuBar.add(ticketMenu);
        menuBar.add(passwordMenu);
        menuBar.add(logoutMenu);

        // 添加电影查询菜单的鼠标事件监听器
        filmMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new UserFilmTablePanel(account));
            }
        });

        // 添加订单管理菜单的鼠标事件监听器
        ticketMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContent(new UserTicketTablePanel(account));
            }
        });

        // 添加修改密码菜单的鼠标事件监听器
        passwordMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContent(new PasswordPanel(account));
            }
        });

        // 添加退出登录菜单的鼠标事件监听器
        logoutMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                contentPanel.removeAll();
                MainFrame.setContent(new LoginPanel());
            }
        });

        // 设置布局管理器
        this.setLayout(new BorderLayout());

        // 设置菜单栏大小并添加到用户面板的北部位置
        menuBar.setBounds(0, 0, SystemConstants.FRAME_WIDTH, 50);
        this.add(menuBar, BorderLayout.NORTH);
    }

    /**
     * 设置内容面板的内部框架
     * @param internalFrame 要显示的内部框架
     */
    public static void setContent(JInternalFrame internalFrame) {
        // 设置内部框架大小和可见性
        internalFrame.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);
        internalFrame.setVisible(true);

        // 移除所有内容并重新绘制
        contentPanel.removeAll();
        contentPanel.repaint();

        // 添加新的内部框架
        contentPanel.add(internalFrame);
    }
}
