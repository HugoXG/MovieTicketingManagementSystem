package com.example.controller.adminPanel;

import com.example.entity.Account;
import com.example.entity.Ticket;
import com.example.service.TicketService;
import com.example.service.impl.TicketServiceImpl;
import com.example.utils.SystemConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 管理员订单列表面板类，提供显示和管理订单的界面和功能
 */
public class AdminTicketTablePanel extends JInternalFrame {
    // 电影票服务实例，用于处理电影票相关的业务逻辑
    private final TicketService ticketService = new TicketServiceImpl();

    /**
     * 构造方法，初始化管理员订单列表面板
     * @param account 当前管理员账号
     */
    public AdminTicketTablePanel(Account account) {
        super("订单列表", true, true, true, true);
        this.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 创建不可编辑的表格
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // 设置表格模型
        setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));

        JPanel topPanel = new JPanel();

        // 创建单选按钮用于选择查询条件
        JRadioButton idRadio = new JRadioButton("ID");
        JRadioButton statusRadio = new JRadioButton("状态");
        JRadioButton userIdRadio = new JRadioButton("用户ID");
        JRadioButton userNameRadio = new JRadioButton("用户名称");
        JRadioButton purchaseDataRadio = new JRadioButton("购票时间");
        JRadioButton viewDataRadio = new JRadioButton("观影时间");
        JRadioButton filmNameRadio = new JRadioButton("电影");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(idRadio);
        buttonGroup.add(statusRadio);
        buttonGroup.add(userIdRadio);
        buttonGroup.add(userNameRadio);
        buttonGroup.add(purchaseDataRadio);
        buttonGroup.add(viewDataRadio);
        buttonGroup.add(filmNameRadio);
        topPanel.add(idRadio);
        topPanel.add(statusRadio);
        topPanel.add(userIdRadio);
        topPanel.add(userNameRadio);
        topPanel.add(purchaseDataRadio);
        topPanel.add(viewDataRadio);
        topPanel.add(filmNameRadio);

        topPanel.add(new JLabel(":"));
        // 输入框用于输入查询条件
        JTextField mainField = new JTextField(20);
        topPanel.add(mainField);

        // 创建按钮用于查询、添加和停用
        JButton searchBtn = new JButton("查询");
        JButton addBtn = new JButton("添加");
        JButton stopBtn = new JButton("停用");

        topPanel.add(searchBtn);
        topPanel.add(addBtn);
        topPanel.add(stopBtn);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 查询按钮点击事件
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String input = mainField.getText();
                List<Ticket> tickets = null;

                if (input.isEmpty()) {
                    setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));
                } else {
                    if (idRadio.isSelected()) {
                        tickets = ticketService.findById(input);
                    } else if (statusRadio.isSelected()) {
                        tickets = ticketService.findByStatus(input);
                    } else if (userIdRadio.isSelected()) {
                        tickets = ticketService.findByAccountId(input);
                    } else if (userNameRadio.isSelected()) {
                        tickets = ticketService.findByAccountName(input);
                    } else if (purchaseDataRadio.isSelected()) {
                        tickets = ticketService.findByPurchaseDate(input);
                    } else if (viewDataRadio.isSelected()) {
                        tickets = ticketService.findByViewDate(input);
                    } else if (filmNameRadio.isSelected()) {
                        tickets = ticketService.findByFilmName(input);
                    } else {
                        setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));
                    }
                    setTableModel(table, ticketService, account, ticketService.toList(account, tickets));
                }
            }
        });

        // 添加按钮点击事件
        addBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminPanel.setContent(new AdminTicketAddPanel(account));
            }
        });

        // 停用按钮点击事件
        stopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum = table.getSelectedRow();

                if (rowNum > -1) {
                    List<Ticket> ticketList = ticketService.findAll(account);
                    Ticket ticket = ticketList.get(rowNum);
                    int sta = ticketService.stopById(ticket.getId());

                    // 根据返回结果显示相应提示
                    if (sta == 0) {
                        JOptionPane.showMessageDialog(stopBtn.getParent(),
                                "停用成功",
                                "系统提示",
                                JOptionPane.INFORMATION_MESSAGE);
                        setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));
                    } else if (sta == 1) {
                        JOptionPane.showMessageDialog(stopBtn.getParent(),
                                "请勿重复停用",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    } else if (sta == 2) {
                        JOptionPane.showMessageDialog(stopBtn.getParent(),
                                "停用失败，观影时间已过",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE);
                    }
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
     * @param table JTable实例
     * @param ticketService 电影票服务实例
     * @param account 当前管理员账号
     * @param list 电影票列表
     */
    public static void setTableModel(JTable table,
                                     TicketService ticketService,
                                     Account account,
                                     Object[][] list) {
        TableModel tableModel = new DefaultTableModel(list, ticketService.getColumnNames(account));
        table.setModel(tableModel);
    }
}
