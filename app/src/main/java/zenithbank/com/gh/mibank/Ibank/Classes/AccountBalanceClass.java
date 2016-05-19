package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 6/10/2015.
 */
public class AccountBalanceClass
{
    public class AccountBalance
    {
        public String acctType;
        public String acctNo;
        public String acctDesc;
        public String isoCurrency;
        public double curBal;
        public double acctAvail;
        public int holdBal;
        public String title1;
        public int memoFloat;
    }

    public class RootObject
    {
        public AccountBalance accountBalance;
    }
}
