package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 4/27/2015.
 */

import java.util.List;

/**
 * Created by Robby on 2/19/2015.
 */
public class Customer
{
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

    public static class Profile
    {
        public String username;
        public long accesscode;
        public RimInfo rimInfo;
        public List<Account> accounts;
        public boolean changePassword;
    }

    public static class RootObject
    {
        public Profile profile;
        public String acctType;
        public String acctNo;
        public String acctDesc;
        public String isoCurrency;
        public TransferProduct transferProduct;
        public TransferBeneficiary transferBeneficiary;
        public ScheduleFrequency scheduleFrequency;
        public AccountActivity accountActivity;
        public double availableBal;
        public double curBal;
        public int memoCR;
        public int memoDR;
        public int memoFloat;
        public AccountBalance accountBalance;
        // public BillPayment billPayment;
    }

    public static class Validation
    {
        public int id;
        public int fieldNum;
        public boolean required;
        public String validatorMessage;
        public String extAutoComplete;
        public String regEx;
    }

    public static class TransferProduct
    {
        public int id;
        public String productCode;
        public String productName;
        public String field1;
        public String field2;
        public String field3;
        public String field4;
        public String field5;
        public boolean valid;
        public List<Validation> validations;
    }

    public static class TransferBeneficiary
    {
        public int id;
        public TransferProduct transferProduct;
        public String field1;
        public String field2;
        public int field3;
        public int field4;
        public String field5;
        public boolean valid;
    }

    public static class ScheduleFrequency
    {
        public int frid;
        public String name;
    }

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

    public static class Validations
    {
        public int id;
        public int fieldNum;
        public boolean required;
        public String regEx;
        public String validatorMessage;
    }

    public static class AccountBalance
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

    public static class TransferRequest
    {
        public String fromAcct;
        public String toAcct;
        public double amt;
        public String description;
    }


}

