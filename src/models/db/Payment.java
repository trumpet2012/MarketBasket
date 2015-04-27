package models.db;

import utils.Connection;

import javax.swing.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * William Trent Holliday
 * 4/26/15
 */
public class Payment {

    private long paymentID;
    private Date paymentDate;
    private double paymentAmount;
    private double balance;
    private String paymentMethod;
    private long customerID;
    private long orderID;

    public Payment(long orderID, long paymentID, Date paymentDate, double paymentAmount, double balance, String paymentMethod, long customerID) {
        this.orderID = orderID;
        this.paymentID = paymentID;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.balance = balance;
        this.paymentMethod = paymentMethod;
        this.customerID = customerID;
    }

    public static ArrayList<Payment> getPaymentsForOrder(Order order) {
        String sqlQuery = "select * from payment where orderid = ?";
        ResultSet paymentResults = Connection.getResultsFromQuery(sqlQuery, String.valueOf(order.getOrderid()));

        ArrayList<Payment> paymentsForOrder = new ArrayList<Payment>();
        try {
            while (paymentResults.next()) {
                long paymentID = paymentResults.getLong("paymentid");
                Date paymentDate = paymentResults.getDate("paymentdate");
                double paymentAmount = paymentResults.getDouble("paymentAmount");
                double balance = paymentResults.getDouble("balance");
                String paymentMethod = paymentResults.getString("paymentmethod");
                long customerID = paymentResults.getLong("customerid");
                long orderID = paymentResults.getLong("orderID");

                Payment payment = new Payment(orderID, paymentID, paymentDate,
                        paymentAmount, balance, paymentMethod, customerID);
                paymentsForOrder.add(payment);
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error loading payments.", JOptionPane.ERROR_MESSAGE);
        }

        return paymentsForOrder;
    }

    public long getPaymentID() {
        return paymentID;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public double getBalance() {
        return balance;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public long getCustomerID() {
        return customerID;
    }

    public long getOrderID() {
        return orderID;
    }
}