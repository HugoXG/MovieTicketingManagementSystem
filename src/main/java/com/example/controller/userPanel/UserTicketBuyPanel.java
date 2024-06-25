package com.example.controller.userPanel;

import com.example.entity.Account;
import com.example.service.TicketService;
import com.example.service.impl.TicketServiceImpl;
import com.example.utils.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 用户购票面板类，提供电影票购买功能
 */
public class UserTicketBuyPanel extends JInternalFrame {

    // 票务服务实例，用于处理票务相关的业务逻辑
    private static final TicketService ticketService = new TicketServiceImpl();

    /**
     * 构造方法，初始化用户购票面板
     * @param account 登录的账户信息
     */
    public UserTicketBuyPanel(Account account) {
        // 调用父类构造方法，设置内部框架属性
        super("购买电影票", true, true, true, true);

        // 设置面板大小
        this.setSize(SystemConstants.FRAME_WIDTH - 20, SystemConstants.FRAME_WEIGHT - 50);

        // 创建主面板并设置布局
        JPanel panel = new JPanel();
        panel.setSize(100, 50);
        panel.setLayout(new FlowLayout());
        this.setContentPane(panel);

        // 创建基础盒子布局
        Box boxBase = Box.createHorizontalBox();
        Box boxLeft = Box.createVerticalBox();

        // 添加标签
        boxLeft.add(new JLabel("电影名称（无需《》）："));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("观影时间："));
        boxLeft.add(Box.createVerticalStrut(8));

        // 创建右侧盒子布局
        Box boxRight = Box.createVerticalBox();
        JTextField field_1 = new JTextField(10);
        boxRight.add(field_1);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建日期选择器
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(dateEditor);
        boxRight.add(dateSpinner);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建提交按钮并添加事件监听器
        JButton button = getjButton(account, field_1, dateSpinner);
        boxRight.add(button);
        boxBase.add(boxLeft);
        boxBase.add(Box.createHorizontalStrut(8));
        boxBase.add(boxRight);
        panel.add(boxBase);

        // 设置面板可见
        this.setVisible(true);
    }

    /**
     * 创建提交按钮并添加点击事件
     * @param account 登录的账户信息
     * @param field_1 电影名称输入框
     * @param dateSpinner 观影时间选择器
     * @return 返回设置好的提交按钮
     */
    private static JButton getjButton(Account account, JTextField field_1, JSpinner dateSpinner) {
        JButton button = new JButton("提交");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String filmName = field_1.getText();
                Date viewDate = (Date) dateSpinner.getValue();

                // 检查输入
                if (filmName.isEmpty() || viewDate == null) {
                    JOptionPane.showMessageDialog(button.getParent(),
                            "购买失败，请填写所要观看的电影以及观影日期",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // 将Date转换为LocalDateTime
                    LocalDateTime viewDateTime = viewDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    // 调用服务ticketService.add()进行购票操作
                    int sta = ticketService.add(account.getAccountName(), filmName, viewDateTime);
                    if (sta == 0) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加成功!",
                                "系统提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        UserPanel.setContent(new UserTicketTablePanel(account));
                    } else if (sta == 1) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加失败，请检查电影名称是否有误",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    } else if (sta == 2) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加失败，该电影观影人数已满",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    } else if (sta == 3) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加失败，该电影未上映",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        return button;
    }
}
