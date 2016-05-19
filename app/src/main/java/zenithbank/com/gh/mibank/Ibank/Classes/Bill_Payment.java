package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 5/1/2015.
 */
public class Bill_Payment
{
    public static class RootObject
    {
        public BillPayment billPayment;
    }

    public static class BillPayment
    {
        public int id;
        public BillInformation billInformation;
        public long pmtAcctNo;
        public int amount;
    }

    public static class BillInformation
    {
        public String id;
    }


}