package models.db;

import utils.Connection;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dr.H on 4/17/2015.
 */
public class Address {

    private long addressid;
    private String addressLine;
    private String city;
    private int zip;
    private String country;
    private String state;

    public Address(long addressid, String addressLine, String city, int zip, String country, String state) {
        this.addressid = addressid;
        this.addressLine = addressLine;
        this.city = city;
        this.zip = zip;
        this.country = country;
        this.state = state;
    }

    public long getAddressid() {
        return addressid;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public int getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    private static Address createAddressFromResult(ResultSet result){
        try{
                long addressid = result.getLong("ADDRESSID");
                String addressLine = result.getString("ADDRESSLINE");
                String city = result.getString("CITY");
                int zip = result.getInt("ZIP");
                String country = result.getString("COUNTRY");
                String state = result.getString("STATE");

                return new Address(addressid, addressLine, city, zip, country, state);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error loading Address from database.", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static Address getAddressByID(int addressID) {
        String addressQuery = "Select * from address where addressid = ?";
        ResultSet addressResults = Connection.getResultsFromQuery(addressQuery, String.valueOf(addressID));
        try {
            while (addressResults.next()) {
                return createAddressFromResult(addressResults);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error loading Address from database.", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public static List<Address> getAddressesForCustomer(int customerID, boolean getBilling) {
        List<Address> addressList = new ArrayList<Address>();
        String addressType;
        if(getBilling){
            addressType = "isbilling = 'true'";
        }else {
            addressType = "isshipping = 'true'";
        }
        String addressQuery = "select * from address where customerid = ? and " + addressType;
        ResultSet addressResults = Connection.getResultsFromQuery(addressQuery, String.valueOf(customerID));

        try {
            while(addressResults.next()){
                Address address = createAddressFromResult(addressResults);
                addressList.add(address);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }

        return addressList;
    }
}