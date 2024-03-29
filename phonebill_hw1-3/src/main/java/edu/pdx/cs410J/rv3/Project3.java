package edu.pdx.cs410J.rv3;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J Phone Bill Project that parses command line arguments to make sure they are in the correct
 * format before creating a <code>PhoneBill</code> and <code>PhoneCall</code> objects of the passed in arguments.
 *
 * @author Ricky Valencia
 * @version 1.0
 */


public class Project3 {
    /**
     * Holds the phone bill that will be handled throughout the entire program
     */
    private PhoneBill bill;

    public static void main(String[] args) {

        //If no command line arguments are passed in, gracefully exit
        if (args.length == 0) {
            System.err.println("Missing command line arguments");
            System.exit(1);
        }

        new Project3().start(args);
    }

    private void start(String[] args) {
        Class c = AbstractPhoneBill.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath
        PhoneCall call = null; //Holds the phone call for the user
        ArrayList<String> comLineInput = new ArrayList<>(); //Holds the array of command line arguments
        boolean verbose; //Holds whether the '-print' tag was used
        String textFile; //Holds whether the user used the -textFile tag with an external file
        String prettyFile; //Holds the name of the pretty file or if it's going to print to the output stream
        String customer; //Holds the name of the customer
        Collections.addAll(comLineInput, args); //Adds the command line arguments into the ArrayList comLineInput
        PrettyPrinter printer; //Stores a PrettyPrinter object in case the user wants to print their calls

        parseReadMe(comLineInput);
        textFile = parseFile(comLineInput);
        prettyFile = parsePretty(comLineInput);
        verbose = parsePrint(comLineInput);
        parseCLSize(comLineInput, 9);
        customer = comLineInput.get(0);
        comLineInput.remove(0); //Removes the customer name from the list
        try {
            call = (PhoneCall) parsePhoneCall(comLineInput);
        } catch (ParserException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        manageBill(call, customer);

        //If the user inputs the -textFile tag, then write the bill to the file
        if (textFile != null) {
            writeToFile(textFile);
        }

        //Check if the user used the -pretty tag. Display the bill if they did
        if (prettyFile != null) {
            if (prettyFile.equals("-")) {
                printer = new PrettyPrinter();
                printer.dumpStandardOut(bill);
            } else {
                try {
                    printer = new PrettyPrinter(prettyFile);
                    printer.dump(bill);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        //If the user inputs the tag -print, then it will print here
        if (verbose) {
            System.out.println(call.toString());
        }
    }

    /**
     * This method checks if the bill manages a phone call that is being sent to the bill. If there are no phone calls
     * in the bill initially, then it adds it. If the customers from the new call and the current bill don't match, then
     * it throws an error.
     *
     * @param call     The call that is being inserted into the bill.
     * @param customer The name of the new customer if there was no bill to begin with.
     */
    private void manageBill(AbstractPhoneCall call, String customer) {
        if (bill == null) {
            bill = new PhoneBill(customer);
            bill.addPhoneCall(call);
        } else {
            if (!bill.getCustomer().equalsIgnoreCase(customer)) {
                System.err.println("The customers do not match!");
                System.exit(1);
            }
            bill.addPhoneCall(call);
        }
    }

    /**
     * This method parses the command line arguments in list and checks to see if the -README tag was in it. If the tag is
     * present, then it will print a description of the project then exists the program.
     *
     * @param list Takes in the command line arguments as an ArrayList to parse it for the tag.
     */
    private void parseReadMe(ArrayList list) {
        //If the -README tag exists, run the description method and exit the program
        if (list.contains("-README")) {
            printDescription();
            System.exit(1);
        }
    }

    /**
     * Prints a description of the project by explaining how to use it, what it is, and any new features.
     */
    private void printDescription() {
        System.out.println("Author: Ricky Valencia\n Assignment: Project 1\n" +
                "This program takes in command line arguments which include customer's name, caller phone number, callee\n" +
                "phone number, the start date and time of the call, and when the call ended. The Order of the arguments is\n" +
                "important and they can be followed by the optional tags -print and -README, which can be in any order. This\n" +
                "program will parse the CL arguments and check to make sure they are in the correct format. If the user \n" +
                "inputs the -print tag then it will print a description of the call. If the user adds the -README tag, then\n" +
                "it will print a description of the program without executing the rest of the program. It can read and write\n " +
                "the phone call to the file if the -textFile tag exists with a filename. Now, it can use a -pretty tag that" +
                "will display the contents of the phone bill in a text file or the output stream in a neat, pretty format.");
    }

    /**
     * This method takes in the command line arguments as the parameter, then it checks it to see if it has the -textFile
     * tag. If it has the tag, then it locates the name of the file, parses the files' content, and returns the new
     * <code>PhoneBill</code> object created from the text file. It then removes the tag and filename from the list so it
     * doesn't interfere with the parsing of the new phone call. If the tag doesn't exist, then it returns null.
     *
     * @param list Stores the command line arguments being passed in.
     * @return Returns null if the tag does not exist, returns the name of the file to be written to otherwise
     * from the text file if the tag exists. It can still return null if the file does not exist but the tag was still present.
     */
    private String parseFile(ArrayList<String> list) {
        int fileNameIndex;

        if (list.contains("-textFile")) {
            fileNameIndex = list.indexOf("-textFile") + 1;
            String filename = list.get(fileNameIndex);
            //Removes the tag and filename from the list of arguments
            list.remove("-textFile");
            list.remove(filename);
            TextParser contents = new TextParser(filename);
            try {
                bill = (PhoneBill) contents.parse();
                return filename;
            } catch (ParserException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        return null;
    }

    /**
     * Parses the command line arguments and checks for the tags '-print'. If the '-print' tag is found,
     * then it returns true, otherwise false.
     *
     * @param list The list of command line arguments passed in as an <code>ArrayList</code> object.
     * @return If the '-print' tag is found, it returns true, otherwise false.
     */
    private boolean parsePrint(ArrayList<String> list) {
        //If the -print tag exists, set verbose to true and remove it from the array so only the arguments are left
        if (list.contains("-print")) {
            list.remove("-print"); //Removes the tag from the list of arguments
            return true;
        }
        return false;
    }

    /**
     * This method takes in the command line arguments as an argument and checks to see if it has the "-pretty" tag. If
     * it does, then it'll extract the filename, then remove the tag and the filename from the list of command line arguments.
     *
     * @param list Stores the command line arguments passed into the program.
     * @return Returns the name of the file that <code>PrettyPrinter</code> will print to.
     */
    private String parsePretty(ArrayList<String> list) {
        int fileNameIndex;
        String prettyFile;

        if (list.contains("-pretty")) {
            fileNameIndex = list.indexOf("-pretty") + 1;
            prettyFile = list.get(fileNameIndex);
            list.remove("-pretty");
            list.remove(prettyFile);
            return prettyFile;
        }
        return null;
    }

    /**
     * This method takes in a list of strings that hold all the information for a phone call. It then parses each string
     * to see if it is in the proper format.
     *
     * @param call Stores the list of arguments used for a phone call
     * @return Returns a new <code>PhoneCall</code> object if all the arguments are valid.
     * @throws ParserException Throws this exception if one of the arguments was not in the correct format.
     */
    public AbstractPhoneCall parsePhoneCall(ArrayList<String> call) throws ParserException {
        String caller; //Holds the caller's phone number
        String callee; //Holds the callee's phone number
        String start; //Holds the concatenation of the start date and start time
        String end; //Holds the concatenation of the end date and end time

        caller = call.get(0);
        callee = call.get(1);
        if (call.size() == 4) {
            start = call.get(2);
            end = call.get(3);
        } else {
            start = call.get(2) + " " + call.get(3) + " " + call.get(4);
            end = call.get(5) + " " + call.get(6) + " " + call.get(7);
        }
        try {
            parseTelephone(caller);
            parseTelephone(callee);
            Date startDate = parseDateAndTime(start);
            Date endDate = parseDateAndTime(end);
            return new PhoneCall(caller, callee, startDate, endDate);
        } catch (ParserException e) {
            throw e;
        }
    }

    /**
     * This method parses the command line arguments and checks to see if it has the correct number of arguments for a
     * phone call. It must have 7 arguments in total and it will exit with an error message if it is under or over the
     * size of the argument passed in.
     *
     * @param comLineInput The command line arguments passed in by the main method
     * @param size         Holds the specific size the command line arguments should be.
     */
    public void parseCLSize(ArrayList<String> comLineInput, int size) {
        if (comLineInput.size() < size) {
            System.err.println("Missing command line arguments.");
            System.exit(1);
        } else if (comLineInput.size() > size) {
            System.err.println("Too many command line arguments.");
            System.exit(1);
        }
    }

    /**
     * Parses the number string against a regex pattern (xxx-xxx-xxxx where x's are numbers) to verify that the phone
     * number was passed in under the correct format. If it's not in the proper format, <code>ParseTelephone</code>
     * will print an error message and exit the program.
     *
     * @param number The phone number from the command line that will be parsed by the method.
     * @throws ParserException Throws this exception if the phone number is not in the correct format.
     */
    public void parseTelephone(String number) throws ParserException {

        Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
        Matcher matcher = pattern.matcher(number); //Check if the phone number is in the format xxx-xxx-xxxx

        if (!matcher.matches()) {
            throw new ParserException("Phone number must be in the format xxx-xxx-xxxx");
        }
    }

    /**
     * Parses the dateString string against two different string formats, MM/dd/yyyy HH:mm and "M/dd/yyyy HH:mm, to make
     * sure that they were entered in under the correct format. If it's not in the proper format, <code>ParseDateAndTime</code>
     * will throw a <code>ParseException</code>, give an error message, and exit the program.
     *
     * @param dateString The date and time string from the command line that will be parsed.
     * @throws ParserException Throws this exception if the date is in the wrong format.
     * @return Returns the new <code>Date</code> object that was parsed if it was in the correct format.
     */
    public Date parseDateAndTime(String dateString) throws ParserException {
        dateString = dateString.replace("pm", "PM").replace("am", "AM");

        try {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            format.setLenient(false);
            return format.parse(dateString);
        } catch (ParseException e) {
            throw new ParserException("Date must be in the format MM/dd/yyyy hh:mm (am/pm).");
        }
    }

    /**
     * This method writes the contents of bill into filename in the correct format.
     *
     * @param filename Holds the name of the text file that the <code>PhoneBill</code> will be written to.
     */
    private void writeToFile(String filename) {

        try {
            new TextDumper(filename).dump(bill);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}