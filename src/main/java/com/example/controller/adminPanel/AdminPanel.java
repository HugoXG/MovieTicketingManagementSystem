package com.example.controller.adminPanel;

import com.example.controller.LoginPanel;
import com.example.controller.MainFrame;
import com.example.entity.Account;
import com.example.utils.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 管理员面板类，提供管理员相关功能的界面
 */
public class AdminPanel extends JPanel {

    // 主内容面板，显示不同功能界面
    private static final JDesktopPane contentPanel = new JDesktopPane();

    /**
     * 构造方法，初始化管理员面板
     * @param account 登录的管理员账户信息
     */
    public AdminPanel(Account account) {
        // 设置面板边界
        this.setBounds(0, 0, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);
        contentPanel.setBounds(0, 20, SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);
        contentPanel.removeAll();
        this.add(contentPanel, BorderLayout.CENTER);

        // 添加管理员标签
        this.add(new JLabel("管理员"));

        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu filmMenu = new JMenu("电影信息");
        JMenu ticketMenu = new JMenu("售票信息");
        JMenu systemMenu = new JMenu("系统管理");

        menuBar.add(filmMenu);
        menuBar.add(ticketMenu);
        menuBar.add(systemMenu);

        // 创建退出登录菜单项
        JMenuItem logoutMenu = new JMenuItem("退出登录");
        systemMenu.add(logoutMenu);

        // 为电影信息菜单添加鼠标点击事件监听器
        filmMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setContent(new AdminFilmTablePanel(account));
            }
        });

        // 为售票信息菜单添加鼠标点击事件监听器
        ticketMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setContent(new AdminTicketTablePanel(account));
            }
        });

        // 为退出登录菜单项添加鼠标点击事件监听器
        logoutMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                contentPanel.removeAll();
                MainFrame.setContent(new LoginPanel());
            }
        });

        // 设置面板布局
        this.setLayout(new BorderLayout());
        menuBar.setBounds(0, 0, SystemConstants.FRAME_WIDTH, 50);
        this.add(menuBar, BorderLayout.NORTH);
    }

    /**
     * 设置内容面板显示的内部框架
     * @param internalFrame 要显示的内部框架
     */
    public static void setContent(JInternalFrame internalFrame) {
        internalFrame.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);
        internalFrame.setVisible(true);
        contentPanel.removeAll();
        contentPanel.repaint();
        contentPanel.add(internalFrame);
    }
}
