package zenithbank.com.gh.mibank.Main.Classes;

/**
 * Created by Robby on 3/16/2016.
 */
public class DeviceRegisterMessage
{
    private String rim;

    public DeviceRegisterMessage(String msg)
    {
        this.rim = msg;
    }

    public String getRim()
    {
        return this.rim;
    }
}
