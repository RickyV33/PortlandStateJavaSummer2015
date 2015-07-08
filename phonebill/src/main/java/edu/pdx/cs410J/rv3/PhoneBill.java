package edu.pdx.cs410J.rv3;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The <code>PhoneBill</code> class extends the <code>AbstractPhoneBill</code> class which
 * allows the user to create a bill consisting of a series of phone calls addressed to one person.
 */
public class PhoneBill extends AbstractPhoneBill{
    /**
     * Stores the customer's name that the phone calls belong to
     */
    public String customer;
    /**
     * Stores the list of all the phone calls made by the customer
     */
    public List<PhoneCall> phoneCalls = null;

    /**
     * Creates a new <code>PhoneBill</code> object using a customers name and a <code>List</code> of phone calls already existing.
     * @param customer The name of the customer that the list of phone calls belongs to.
     * @param phoneCalls A list of phone calls made for the customers bill using the <code>PhoneCall</code> class.
     */
    public PhoneBill(String customer, List phoneCalls){
        this.customer = new String(customer);
        this.phoneCalls = new ArrayList<PhoneCall>(phoneCalls);
    }

    /**
     * Creates a new <code>PhoneBill</code> object using only the customers name.
     * @param customer The name of the customer that the list of phone calls belongs to.
     */
    public PhoneBill(String customer) {
        this.customer = new String(customer);
    }

    /**
     * Overrides <code>AbstractPhoneBill</code>'s abstract method <code>getCustomer</code> to return the customer's name.
     * @return The customer's name
     */
    @Override
    public String getCustomer() {
        return customer;
    }

    /**
     * Overrides <code>AbstractPhoneBill</code>'s abstract method <code>addPhoneCall</code> to add a phone call into
     * the colletion of phone calls held by the customer.
     * @param call A <code>PhoneCall </code> object to be stored into the list
     */
    @Override
    public void addPhoneCall(AbstractPhoneCall call) {
        if (phoneCalls == null)
            phoneCalls = new ArrayList<PhoneCall>();
        phoneCalls.add((PhoneCall) call);
    }

    /**
     * Overrides <code>AbstractPhoneBill</code>'s abstract method <code>getPhoneCalls</code> to return the list of
     * phone calls.
     * @return The list of phone calls as a collection.
     */
    @Override
    public Collection getPhoneCalls() {
        return phoneCalls;
    }

    /**
     * Returns <code>AbstractPhoneBill</code>'s method <code>toString</code> that prints a description of the customer's
     * phone bill.
     * @return The customer's name and the number of phone calls in his bill.
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
