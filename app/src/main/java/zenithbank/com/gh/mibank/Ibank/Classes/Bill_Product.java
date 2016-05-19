package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 5/3/2015.
 */
public class Bill_Product
{
    public static class BillProduct
    {
        public int id;
    }

    public static class BillInformation
    {
        public int id;
        public BillProduct billProduct;
        public int field1;
    }

    public static class RootObject
    {
        public BillInformation billInformation;
    }
}
