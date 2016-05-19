package zenithbank.com.gh.mibank.Bill_Payment.Classes;

/**
 * Created by Robby on 5/4/2016.
 */
public class BillPaymentRequestClass
{
    public static class RootObject
    {
        public String serviceType;
        public String bankAccountNumber;
        public String serviceAccountNumber;
        public int amount;
        public String referenceNumber;
        public String transId;
    }
}
