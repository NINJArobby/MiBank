package zenithbank.com.gh.mibank.Ibank.Classes;

/**
 * Created by Robby on 5/3/2015.
 */
public class AddBillClass
{
    public static class Validations
    {
        public int id;
        public int fieldNum;
        public boolean required;
        public String regEx;
        public String validatorMessage;
    }

    public static class BillProduct
    {
        public int id;
        public String productName;
        public String field1;
        public String field2;
        public String field3;
        public String field4;
        public String field5;
        public String field6;
        public String field7;
        public String field8;
        public String field9;
        public Validations validations;
    }

    public static class RootObject
    {
        public BillProduct billProduct;
    }
}
