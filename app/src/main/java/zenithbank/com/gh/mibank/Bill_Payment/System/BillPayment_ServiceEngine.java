package zenithbank.com.gh.mibank.Bill_Payment.System;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import zenithbank.com.gh.mibank.Bill_Payment.Classes.BillPaymentRequestClass;

/**
 * Created by Robby on 5/4/2016.
 */
public class BillPayment_ServiceEngine
{
    String BaseUrl = "http://www.zenithbank.com.gh/MobileService/api/MiBank";

    public String MakePayment(BillPaymentRequestClass.RootObject Brequest)
    {
        String data = null;
        try
        {
            String jData = new Gson().toJson(Brequest);
            HttpRequest request = new HttpRequest(BaseUrl + "/ExpressBillPayment", HttpRequest.METHOD_POST);
            request.contentType(HttpRequest.CONTENT_TYPE_JSON);
            request.send(jData);
            String responseBody = request.body();
            int code = request.code();
            if (code == 200)
            {
                JSONObject json = new JSONObject(responseBody);
                data = json.getString("Data");
            }

        } catch (Exception ex)
        {
            data = ex.getMessage();
        }

        return data;
    }
}
