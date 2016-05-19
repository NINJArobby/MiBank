package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 5/8/2015.
 */
public class TransferRequestClass
{
    public static class TransferRequest
    {
        public String fromAcct;
        public String toAcct;
        public double amt;
        public String description;
        public String token;
    }

    public static class RootObject
    {
        public TransferRequest transferRequest;
    }
}
