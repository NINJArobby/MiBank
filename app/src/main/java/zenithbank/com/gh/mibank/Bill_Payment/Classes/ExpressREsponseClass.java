package zenithbank.com.gh.mibank.Bill_Payment.Classes;

/**
 * Created by Robby on 5/4/2016.
 */
public class ExpressREsponseClass
{
    public class RootObject
    {
        public int status;
        public String message;
        public String referenceNumber;
        public String receiptNumber;
        public String transactionId;
        public String timestamp;
        public int balance;
        public String currency;
    }
}
