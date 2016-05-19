package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 5/8/2015.
 */
public class InterbankTransferClass
{
    public static class TransferBeneficiary
    {
        public String id;
        public boolean valid;
    }

    public static class Transfer
    {
        public int id;
        public TransferBeneficiary transferBeneficiary;
        public long tfrAcctNo;
        public int amount;
        public String token;
    }

    public static class RootObject
    {
        public Transfer transfer;
    }


}
