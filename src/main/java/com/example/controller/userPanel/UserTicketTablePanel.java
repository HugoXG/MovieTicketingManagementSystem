package com.example.controller.userPanel;

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
 * 用户订单表面板类，提供订单列表显示和查询功能
 */
public class UserTicketTablePanel extends JInternalFrame {

    // 票务服务实例，用于处理票务相关的业务逻辑
    private final TicketService ticketService = new TicketServiceImpl();

    /**
     * 构造方法，初始化用户订单表面板
     * @param account 登录的账户信息
     */
    public UserTicketTablePanel(Account account) {
        // 调用父类构造方法，设置内部框架属性
        super("订单列表", true, true, true, true);

        // 设置面板大小
        this.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 创建不可编辑的表格
        JTable table = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 设置表格模型
        setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));

        // 创建顶部面板，用于放置查询组件
        JPanel topPanel = new JPanel();

        // 添加单选按钮和文本框
        JRadioButton statusRadio = new JRadioButton("状态");
        JRadioButton purchaseDataRadio = new JRadioButton("购票时间");
        JRadioButton viewDataRadio = new JRadioButton("观影时间");
        JRadioButton filmNameRadio = new JRadioButton("电影");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(statusRadio);
        buttonGroup.add(purchaseDataRadio);
        buttonGroup.add(viewDataRadio);
        buttonGroup.add(filmNameRadio);
        topPanel.add(statusRadio);
        topPanel.add(purchaseDataRadio);
        topPanel.add(viewDataRadio);
        topPanel.add(filmNameRadio);

        topPanel.add(new JLabel(":"));
        JTextField mainField = new JTextField(20);
        topPanel.add(mainField);

        // 创建查询按钮和退票按钮
        JButton searchBtn = new JButton("查询");
        JButton retBtn = new JButton("退票");
        topPanel.add(searchBtn);
        topPanel.add(retBtn);

        // 创建包含表格的面板
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(SystemConstants.FRAME_WIDTH, SystemConstants.FRAME_WEIGHT);

        // 为查询按钮添加鼠标点击事件监听器
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String input = mainField.getText();
                List<Ticket> tickets = null;

                // 根据输入进行查询
                if (input.isEmpty()) {
                    setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));
                } else {
                    if (statusRadio.isSelected()) {
                        tickets = ticketService.findByStatusAndAccount(input, account);
                    } else if (purchaseDataRadio.isSelected()) {
                        tickets = ticketService.findByPurchaseDateAndAccount(input, account);
                    } else if (viewDataRadio.isSelected()) {
                        tickets = ticketService.findByViewDateAndAccount(input, account);
                    } else if (filmNameRadio.isSelected()) {
                        tickets = ticketService.findByFilmNameAndAccount(input, account);
                    } else {
                        setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));
                    }
                    setTableModel(table, ticketService, account, ticketService.toList(account, tickets));
                }
            }
        });

        // 为退票按钮添加鼠标点击事件监听器
        retBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum = table.getSelectedRow();

                if (rowNum > -1) {
                    List<Ticket> ticketList = ticketService.findAll(account);
                    Ticket ticket = ticketList.get(rowNum);
                    //通过ticketService.stopById()来获取相应的Status
                    int sta = ticketService.stopById(ticket.getId());
                    if (sta == 0) {
                        JOptionPane.showMessageDialog(
                                retBtn.getParent(),
                                "退票成功",
                                "系统提示",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else if (sta == 1) {
                        JOptionPane.showMessageDialog(
                                retBtn.getParent(),
                                "请勿重复退票",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else if (sta == 2) {
                        JOptionPane.showMessageDialog(
                                retBtn.getParent(),
                                "退票失败，观影时间已过",
                                "系统提示",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                    setTableModel(table, ticketService, account, ticketService.toList(account, ticketService.findAll(account)));
                }
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
     * @param ticketService 票务服务实例
     * @param account 登录的账户信息
     * @param list 表格数据
     */
    public static void setTableModel(JTable table, TicketService ticketService, Account account, Object[][] list) {
        TableModel tableModel = new DefaultTableModel(list, ticketService.getColumnNames(account));
        table.setModel(tableModel);
    }
}
