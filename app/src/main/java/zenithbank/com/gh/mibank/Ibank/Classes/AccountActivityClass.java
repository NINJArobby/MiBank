package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 6/4/2015.
 */
public class AccountActivityClass
{
    public static class AccountActivity
    {
        public int amt;
        public int checkNo;
        public String createDt;
        public String drCr;
        public String effectiveDt;
        public String historyDesc;
        public String isoCurrency;
        public int itemType;
        public String originTracerNo;
        public int ptid;
        public boolean reversal;
        public int runningBal;
        public String tranCodeDesc;
        public String transactionDt;
    }

    public static class RootObject
    {
        public AccountActivity accountActivity;
    }
}
