package zenithbank.com.gh.mibank.Notification.System;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import zenithbank.com.gh.mibank.Notification.Classes.TrsxSearchClass;


/**
 * Created by Robby on 3/30/2016.
 */
public class N_SystemService
{
    String BaseUrl = "http://www.zenithbank.com.gh/MobileService/api/MiBank";

    public String getAccounts(String RIM)
    {
        String data = null;
        String balURL = BaseUrl + "/getAccounts";
        //rim = "389618";
        try
        {
            data = HttpRequest.get(balURL, true, "RIM", RIM).body();
            if (data.contains("error"))
            {
                throw new Exception("error");
            }
            JSONObject json = new JSONObject(data);
            data = json.getString("Data");
        } catch (Exception ex)
        {
            data = ex.getMessage();
        }
        return data;
    }

    public String doSearch(TrsxSearchClass _data)
    {
        String data = null;
        try
        {
            String jData = new Gson().toJson(_data);
            HttpRequest request = new HttpRequest(BaseUrl + "/SearchHistory", HttpRequest.METHOD_POST);
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
