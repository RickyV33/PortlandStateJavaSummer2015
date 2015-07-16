package edu.pdx.cs410J.rv3;
import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The <code>TextParser</code> class extends the <code>PhoneBillParser</code> class which reads information for a phone
 * bill into a PhoneBill object and returns the object.
 *
 * @author Ricky Valencia
 * @version 1.0
 */
public class TextParser implements PhoneBillParser {
   /**Stores the name of the file that the class will parse the information from.*/
    String filename;

    /**
     * Creates a new <code>TextParser</code> object using the filename argument.
     * @param filename Stores the name of the file that the <code>TextParser</code> object reads from.
     */
    public TextParser(String filename) {
        this.filename = filename;
    }

    /**
     * This method checks to see if a file exists with the name of filename. If it doesn't exist, then it creates
     * the file and returns a null <code>PhoneBill</code> object, if it does exist, then it loads in the contents of the
     * file and parses the data. If the data is not in the correct format, then it'll throw a <code>ParserException</code>.
     * In the end, it returns the <code>PhoneBill</code> object created from the file.
     * @return Returns the <code>PhoneBill</code> object instantiated with all information from filename
     * @throws ParserException Thrown if the data stored in filename is in the wrong format.
     */
    @Override
    public AbstractPhoneBill parse() throws ParserException {
        ArrayList<String> callInfo;
        Scanner inputFile = null;
        PhoneBill bill = null;
        Project2 parseArgs = new Project2();
        File file = new File(filename);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                inputFile = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (!inputFile.hasNext())
                return null;

            bill = new PhoneBill(inputFile.nextLine()); //Reads the phone bill customer
            while (inputFile.hasNextLine()) {
                callInfo = new ArrayList<String>(); //Clear out callInfo by instantiating a new object
                for (String string: inputFile.nextLine().split(";")) {
                    callInfo.add(string); //Add the phone call into the list
                }
                try {
                    if (callInfo.size() != 4) {
                        throw new ParserException("Not enough/too much information in the file for the phone calls!");
                    }
                    parseArgs.parseTelephone(callInfo.get(0));
                    parseArgs.parseTelephone(callInfo.get(1));
                    parseArgs.parseDateAndTime(callInfo.get(2));
                    parseArgs.parseDateAndTime(callInfo.get(3));
                }catch (ParserException e) {
                    throw e;
                }
                //Adds a Phone Call object to the bill from the callInfo List
                bill.addPhoneCall(new PhoneCall(callInfo.get(0), callInfo.get(1), callInfo.get(2), callInfo.get(3)));
            }
        }
        return bill;
    }
}
