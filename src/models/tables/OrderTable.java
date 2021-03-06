package models.tables;

import gui.dialogs.MakePayment;
import gui.dialogs.OrderDetail;
import models.db.Customer;
import models.db.Order;
import models.db.Payment;
import utils.Connection;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * William Trent Holliday
 * 4/16/15
 */
public class OrderTable  extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private static final String[] COLUMN_NAMES = new String[] {"ID", "Order Date", "Total Price", "Balance", "", ""};
    private static final Class<?>[] COLUMN_TYPES = new Class<?>[] {int.class, String.class, Long.class, double.class, JButton.class,  JButton.class};
    private static ArrayList<Order> orderList = new ArrayList<Order>();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private int customerID;

    public OrderTable(Customer customer) {
        // Load all the payments
        Payment.getAllPayments();
        customerID = customer.getCustomerID();
        ResultSet orderResults = Connection.getResultsFromQuery("select * from orders where customerid = ?", String.valueOf(customerID));
        addOrdersToList(orderResults);
    }

    public void updateOrders() {
        ResultSet orderResults = Connection.getResultsFromQuery("select * from orders where customerid = ? and orderid > ?", String.valueOf(customerID), String.valueOf(orderList.size()));
        addOrdersToList(orderResults);
    }

    private void addOrdersToList(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                Order dbOrder = Order.createOrderFromQuery(resultSet);
                if(dbOrder != null) {
                    orderList.add(dbOrder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override public int getRowCount() {
        return orderList.size();
    }

    @Override public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_TYPES[columnIndex];
    }

    @Override public Object getValueAt(final int rowIndex, final int columnIndex) {
            /*Adding components*/
        final Order rowOrder = orderList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOrder.getOrderid();
            case 1:
                return rowOrder.getOrderDate();
            case 2:
                return currencyFormat.format(rowOrder.getTotalPrice());
            case 3:
                return currencyFormat.format(rowOrder.getBalanceRemaining());
            case 4:
                final JButton detail_button = new JButton("Details");
                detail_button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        OrderDetail orderDetail = new OrderDetail(rowOrder);
                        orderDetail.pack();
                        orderDetail.setVisible(true);
                    }
                });
                return detail_button;
            case 5:
                final JButton make_payment = new JButton("Make payment");
                make_payment.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        MakePayment makePayment = new MakePayment(rowOrder);
                        makePayment.pack();
                        makePayment.setVisible(true);
                    }
                });
                // Disable the payment button on orders that have been paid off.
                if (rowOrder.getBalanceRemaining() == 0.0) {
                    make_payment.setEnabled(false);
                }
                return make_payment;
            default:
                return "Error";
        }
    }

    public int getCustomerID(){
        return this.customerID;
    }

    public void clearRows(){
        orderList.clear();
    }
}