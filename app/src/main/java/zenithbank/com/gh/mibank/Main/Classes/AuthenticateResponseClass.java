package zenithbank.com.gh.mibank.Main.Classes;

/**
 * Created by Robby on 3/22/2016.
 */
public class AuthenticateResponseClass
{
    public static class Data
    {
        public String name;
        public String rim;
        public String error;
    }

    public static class RootObject
    {
        public Data Data;
    }
}
