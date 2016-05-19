package zenithbank.com.gh.mibank.Ibank.Classes;

import java.util.List;

/**
 * Created by Robby on 10/15/2015.
 */
public class CustomerInfo
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
        public List<CardAccounts> cardAccounts;
        public Boolean changePassword;
    }

    public static class RootObject
    {
        public Profile profile;
    }


}
