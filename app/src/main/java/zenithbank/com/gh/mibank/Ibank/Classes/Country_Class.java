package zenithbank.com.gh.mibank.Ibank.Classes;

import java.util.List;

/**
 * Created by Robby on 7/13/2015.
 */
public class Country_Class
{
    public static class Result
    {
        public String name;
        public String alpha2_code;
        public String alpha3_code;
    }

    public static class RootObject
    {
        public List<String> messages;
        public List<Result> result;
    }
}
