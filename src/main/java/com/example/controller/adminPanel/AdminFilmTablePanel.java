package com.example.controller.adminPanel;

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
 * 管理员电影列表面板类，提供电影信息的查询、添加和停用功能
 */
public class AdminFilmTablePanel extends JInternalFrame {

    // 电影服务实例，用于处理电影相关的业务逻辑
    private final FilmService filmService = new FilmServiceImpl();

    /**
     * 构造方法，初始化管理员电影列表面板
     * @param account 登录的管理员账户信息
     */
    public AdminFilmTablePanel(Account account) {
        super("电影列表", true, true, true, true);
        this.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 创建不可编辑的电影表格
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setTableModel(table, filmService, account, filmService.toList(account, filmService.findAll(account)));

        JPanel topPanel = new JPanel();

        // 创建单选按钮组，用于选择查询条件
        JRadioButton idRadio = new JRadioButton("ID");
        JRadioButton statusRadio = new JRadioButton("状态");
        JRadioButton filmNameRadio = new JRadioButton("电影名称");
        JRadioButton releasedDateRadio = new JRadioButton("上映日期");
        JRadioButton filmTimeRadio = new JRadioButton("电影时长");
        JRadioButton audienceRadio = new JRadioButton("观影人数");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(idRadio);
        buttonGroup.add(statusRadio);
        buttonGroup.add(filmNameRadio);
        buttonGroup.add(releasedDateRadio);
        buttonGroup.add(filmTimeRadio);
        buttonGroup.add(audienceRadio);
        topPanel.add(idRadio);
        topPanel.add(statusRadio);
        topPanel.add(filmNameRadio);
        topPanel.add(releasedDateRadio);
        topPanel.add(filmTimeRadio);
        topPanel.add(audienceRadio);

        JTextField mainField = new JTextField(20);
        topPanel.add(mainField);

        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        JButton stopBtn = new JButton("停用");
        topPanel.add(searchBtn);
        topPanel.add(addBtn);
        topPanel.add(stopBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 添加查询按钮点击事件
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String input = mainField.getText();
                List<Film> films = null;

                if (input.isEmpty()) {
                    setTableModel(table, filmService, account, filmService.toList(account, filmService.findAll(account)));
                } else {
                    if (idRadio.isSelected()) {
                        films = filmService.findById(input);
                    } else if (statusRadio.isSelected()) {
                        films = filmService.findByStatus(input);
                    } else if (filmNameRadio.isSelected()) {
                        films = filmService.findByFilmName(input);
                    } else if (releasedDateRadio.isSelected()) {
                        films = filmService.findByReleasedDate(input);
                    } else if (filmTimeRadio.isSelected()) {
                        films = filmService.findByFilmTime(input);
                    } else if (audienceRadio.isSelected()) {
                        films = filmService.findByAudience(input);
                    } else {
                        setTableModel(table, filmService, account, filmService.toList(account, filmService.findAll(account)));
                    }
                    setTableModel(table, filmService, account, filmService.toList(account, films));
                }
            }
        });

        // 添加添加按钮点击事件
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminPanel.setContent(new AdminFilmAddPanel(account));
            }
        });

        // 添加停用按钮点击事件
        stopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int id = table.getSelectedRow() + 1;

                if (id > 0) {
                    if (!filmService.stopById(id)) {
                        JOptionPane.showMessageDialog(stopBtn.getParent(),
                                "停用失败，请检查服务器",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    setTableModel(table, filmService, account, filmService.toList(account, filmService.findAll(account)));
                }
            }
        });

        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.add(topPanel, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * 设置表格模型
     * @param table 电影表格
     * @param filmService 电影服务实例
     * @param account 管理员账户信息
     * @param list 表格数据
     */
    public static void setTableModel(JTable table, FilmService filmService, Account account, Object[][] list) {
        TableModel tableModel = new DefaultTableModel(list, filmService.getColumnNames(account));
        table.setModel(tableModel);
    }
}
