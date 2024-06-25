package com.example.controller.userPanel;

import com.example.entity.Account;
import com.example.entity.Film;
import com.example.service.FilmService;
import com.example.service.impl.FilmServiceImpl;
import com.example.utils.SystemConstants;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 用户电影表面板类，提供电影列表显示和查询功能
 */
public class UserFilmTablePanel extends JInternalFrame {

    // 电影服务实例，用于处理电影相关的业务逻辑
    private final FilmService filmService = new FilmServiceImpl();

    /**
     * 构造方法，初始化用户电影表面板
     * @param account 登录的账户信息
     */
    public UserFilmTablePanel(Account account) {
        // 调用父类构造方法，设置内部框架属性
        super("电影列表", true, true, true, true);

        // 设置面板大小
        this.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 创建不可编辑的表格
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 设置表格模型
        setTableModel(table, filmService, account, filmService.toList(account, filmService.findAll(account)));

        // 创建顶部面板，用于放置查询组件
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("电影名称："));
        JTextField mainField = new JTextField(20);
        topPanel.add(mainField);

        // 创建查询按钮和购票按钮
        JButton searchBtn = new JButton("查询");
        JButton buyBtn = new JButton("购票");
        topPanel.add(searchBtn);
        topPanel.add(buyBtn);

        // 创建包含表格的面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 为查询按钮添加鼠标点击事件监听器
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String input = mainField.getText();
                List<Film> films;

                // 根据输入进行查询
                if (input.isEmpty()) {
                    setTableModel(table, filmService, account, filmService.toList(account, filmService.findAll(account)));
                } else {
                    //通过filmService.findByFilmNameAndStatus()来查询电影列表
                    films = filmService.findByFilmNameAndStatus(input, 1);
                    setTableModel(table, filmService, account, filmService.toList(account, films));
                }
            }
        });

        // 为购票按钮添加鼠标点击事件监听器
        buyBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                UserPanel.setContent(new UserTicketBuyPanel(account));
            }
        });

        // 添加表格头部和表格本身到面板
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 添加组件到主面板
        this.add(topPanel, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        this.add(panel, BorderLayout.CENTER);

        // 设置面板可见
        this.setVisible(true);
    }

    /**
     * 设置表格模型
     * @param table 表格
     * @param filmService 电影服务实例
     * @param account 登录的账户信息
     * @param list 表格数据
     */
    public static void setTableModel(JTable table, FilmService filmService, Account account, Object[][] list) {
        TableModel tableModel = new DefaultTableModel(list, filmService.getColumnNames(account));
        table.setModel(tableModel);
    }
}
