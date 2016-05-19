package zenithbank.com.gh.mibank.Notification.Classes;

/**
 * Created by Robby on 3/29/2016.
 */
public class N_NotificationMessage
{
    public class MessageBody
    {
        public double recordId;
        public String date;
        public double amount;
        public String acctNo;
        public String balance;
        public String description;
        public String branchName;
        public String currency;
        public String tranxType;
    }

    public class RootObject
    {
        public String messageType;
        public MessageBody messageBody;
    }
}
