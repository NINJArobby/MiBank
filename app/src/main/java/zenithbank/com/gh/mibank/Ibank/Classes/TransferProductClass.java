package zenithbank.com.gh.mibank.Ibank.Classes;

import java.util.List;

/**
 * Created by Robby on 4/6/2016.
 */
public class TransferProductClass
{
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

    public static class Validation
    {
        public int id;
        public int fieldNum;
        public boolean required;
        public String regEx;
        public String validatorMessage;
        public String extAutoComplete;
    }

    public static class RootObject
    {
        public TransferProduct transferProduct;
    }
}
