package com.example.controller.adminPanel;

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
 * 管理员添加电影票面板类，提供添加电影票信息的界面和功能
 */
public class AdminTicketAddPanel extends JInternalFrame {

    // 电影票服务实例，用于处理电影票相关的业务逻辑
    private static final TicketService ticketService = new TicketServiceImpl();

    /**
     * 构造方法，初始化管理员添加电影票面板
     */
    public AdminTicketAddPanel(Account account) {
        // 设置内部窗口的标题和属性（可关闭、可最小化、可最大化、可移动）
        super("添加电影票", true, true, true, true);
        this.setSize(SystemConstants.FRAME_WIDTH - 20, SystemConstants.FRAME_WEIGHT - 50);

        // 创建主面板，并设置布局
        JPanel panel = new JPanel();
        panel.setSize(100, 50);
        panel.setLayout(new FlowLayout());
        this.setContentPane(panel);

        // 创建水平箱容器和垂直箱容器，用于布局输入框和标签
        Box boxBase = Box.createHorizontalBox();
        Box boxLeft = Box.createVerticalBox();

        // 左侧垂直箱容器添加标签和垂直间距
        boxLeft.add(new JLabel("用户名："));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("电影名称（无需《》）："));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("观影时间："));
        boxLeft.add(Box.createVerticalStrut(8));

        Box boxRight = Box.createVerticalBox();

        // 创建文本框用于输入用户名
        JTextField field_1 = new JTextField(10);
        boxRight.add(field_1);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建文本框用于输入电影名称
        JTextField field_2 = new JTextField(10);
        boxRight.add(field_2);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建日期选择器用于选择观影时间
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(dateEditor);
        boxRight.add(dateSpinner);
        boxRight.add(Box.createVerticalStrut(5));

        //调用静态方法getJButton()获取添加按钮
        JButton button = getJButton(account, field_1, field_2, dateSpinner);

        // 创建并添加提交按钮
        boxRight.add(button);

        // 将左侧和右侧容器添加到基础容器中
        boxBase.add(boxLeft);
        boxBase.add(Box.createHorizontalStrut(8));
        boxBase.add(boxRight);
        panel.add(boxBase);

        // 设置面板可见
        this.setVisible(true);
    }
    private static JButton getJButton(Account account, JTextField field_1, JTextField field_2, JSpinner dateSpinner) {
        JButton button = new JButton("提交");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 获取用户输入的数据
                String userName = field_1.getText();
                String filmName = field_2.getText();
                Date viewDate = (Date) dateSpinner.getValue();

                // 检查输入数据的完整性
                if (userName.isEmpty() || filmName.isEmpty() || viewDate == null) {
                    JOptionPane.showMessageDialog(button.getParent(),
                            "添加失败，用户名、电影名称或观影时间错误",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // 转换日期类型
                    LocalDateTime viewDateTime = viewDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

                    // 调用电影票服务的添加方法
                    int sta = ticketService.add(userName, filmName, viewDateTime);

                    // 根据返回结果显示相应提示
                    if (sta == 0) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加成功!",
                                "系统提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        AdminPanel.setContent(new AdminTicketTablePanel(account));
                    } else if (sta == 1) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加失败，用户名或电影错误",
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
