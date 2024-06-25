package com.example.controller;

import com.example.utils.SystemConstants;
import javax.swing.*;

/**
 * 主框架类，负责创建和显示主窗口
 */
public class MainFrame {

    // 创建一个静态的 JFrame 实例，用于电影售票管理系统的主窗口
    public static final JFrame frame = new JFrame("电影售票管理系统");

    /**
     * 应用程序的入口点，设置主窗口的属性并显示
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置窗口大小
        frame.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 设置窗口布局为空布局
        frame.setLayout(null);

        // 窗口居中显示
        frame.setLocationRelativeTo(null);

        // 显示窗口
        frame.setVisible(true);

        // 设置窗口内容面板为登录面板
        frame.setContentPane(new LoginPanel());

        // 设置窗口关闭操作
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 设置窗口的内容面板
     * @param panel 要设置的内容面板
     */
    public static void setContent(JPanel panel) {
        frame.setContentPane(panel);
    }
}

