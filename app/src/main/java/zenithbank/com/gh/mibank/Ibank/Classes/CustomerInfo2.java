package zenithbank.com.gh.mibank.Ibank.Classes;

import java.util.List;


public class CustomerInfo2
{

    public static class Emails
    {
        public String email;
    }

    public static class Mobilenos
    {
        public long mobileno;
    }

    public static class RimInfo
    {
        public int rimNo;
        public String rimName;
        public String rsmName;
    }

    public static class Account
    {
        public String acctType;
        public String acctNo;
        public String acctDesc;
        public String isoCurrency;
    }

    public static class CardAccounts
    {
        public String acctType;
        public String acctNo;
        public String acctDesc;
        public String isoCurrency;
    }

    public static class Profile
    {
        public String username;
        public long accesscode;
        public Emails emails;
        public Mobilenos mobilenos;
        public RimInfo rimInfo;
        public List<Account> accounts;
        public CardAccounts cardAccounts;
        public boolean changePassword;
    }

    public static class RootObject
    {
        public Profile profile;
    }

}
