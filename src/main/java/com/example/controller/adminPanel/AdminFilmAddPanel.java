package com.example.controller.adminPanel;

import com.example.entity.Account;
import com.example.service.FilmService;
import com.example.service.impl.FilmServiceImpl;
import com.example.utils.SystemConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 管理员添加电影面板类，提供添加电影信息的界面和功能
 */
public class AdminFilmAddPanel extends JInternalFrame {

    // 电影服务实例，用于处理电影相关的业务逻辑
    private static final FilmService filmService = new FilmServiceImpl();

    /**
     * 构造方法，初始化管理员添加电影面板
     * @param account 登录的管理员账户信息
     */
    public AdminFilmAddPanel(Account account) {
        super("添加电影", true, true, true, true);
        this.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        JPanel panel = new JPanel();
        panel.setSize(100, 50);
        panel.setLayout(new FlowLayout());
        this.setContentPane(panel);

        // 创建水平箱容器和垂直箱容器，用于布局输入框和标签
        Box boxBase = Box.createHorizontalBox();
        Box boxLeft = Box.createVerticalBox();

        // 添加标签和垂直间距
        boxLeft.add(new JLabel("电影名称（无需《》）："));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("电影时长："));
        boxLeft.add(Box.createVerticalStrut(8));
        boxLeft.add(new JLabel("上映时间："));
        boxLeft.add(Box.createVerticalStrut(8));

        Box boxRight = Box.createVerticalBox();

        // 创建文本框用于输入电影名称
        JTextField field_1 = new JTextField(10);
        boxRight.add(field_1);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建文本框用于输入电影时长
        JTextField field_2 = new JTextField(10);
        boxRight.add(field_2);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建日期选择器用于选择上映时间
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(dateEditor);
        boxRight.add(dateSpinner);
        boxRight.add(Box.createVerticalStrut(5));

        // 创建提交按钮
        JButton button = getJButton(account, field_1, field_2, dateSpinner);

        // 将按钮添加到右侧垂直箱容器中
        boxRight.add(button);
        // 将左侧和右侧容器添加到基础容器中
        boxBase.add(boxLeft);
        boxBase.add(Box.createHorizontalStrut(8));
        boxBase.add(boxRight);
        // 将基础容器添加到主面板中
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
                String filmName = field_1.getText();
                String filmTime = field_2.getText();
                Date releaseDate = (Date) dateSpinner.getValue();

                // 检查输入数据的完整性
                if (filmName.isEmpty() || filmTime.isEmpty() || releaseDate == null) {
                    JOptionPane.showMessageDialog(button.getParent(),
                            "添加失败，请确认数据是否有误",
                            "系统提示",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    // 转换日期类型
                    LocalDateTime releaseDateTime = releaseDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    // 调用电影服务的添加方法
                    int stu = AdminFilmAddPanel.filmService.add(filmName, Integer.parseInt(filmTime), releaseDateTime);

                    // 根据返回结果显示相应提示
                    if (stu == 0) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加成功!",
                                "系统提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        AdminPanel.setContent(new AdminFilmTablePanel(account));
                    } else if (stu == 1) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加失败，已有该电影",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    } else if (stu == 2) {
                        JOptionPane.showMessageDialog(button.getParent(),
                                "添加失败，请检查上映日期",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        return button;
    }
}
