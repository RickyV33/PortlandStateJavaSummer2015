package edu.pdx.cs410J.rv3.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.Collection;

/**
 * The client-side interface to the ping service
 */
public interface PhoneBillServiceAsync {

  /**
   * Return the current date/time on the server
   */
  void addPhoneCall(Collection<String> args, AsyncCallback<AbstractPhoneBill> async) throws RuntimeException;
}
