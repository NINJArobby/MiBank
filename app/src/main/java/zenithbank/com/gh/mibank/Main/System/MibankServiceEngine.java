package zenithbank.com.gh.mibank.Main.System;

import android.app.Activity;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import zenithbank.com.gh.mibank.Main.Classes.UserAccessClass;

public class MibankServiceEngine extends Activity
{
    System_DBEngine systemDbEngine;
    String BaseUrl = "http://www.zenithbank.com.gh/MobileService/api/MiBank";
    int timeout = 50000;

    public void start()
    {
        systemDbEngine = new System_DBEngine();
        systemDbEngine.Init(this);
    }

    public String getCurrncyRates()
    {
        String data = null;
        try
        {
            data = HttpRequest.get(BaseUrl + "/getExchangeRates").body();
            JSONObject json = new JSONObject(data);
            data = json.getString("Data");


        } catch (Exception ex)
        {
            data = ex.getMessage();

        }

        return data;
    }

    public String getBalances(String rim)
    {
        String data = null;
        String balURL = BaseUrl + "/getBalances";
        //rim = "389618";
        try
        {
            data = HttpRequest.get(balURL, true, "RIM", rim).body();
            JSONObject json = new JSONObject(data);
            data = json.getString("Data");
        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return data;
    }

    public String getRecentHistory(String rim)
    {
        String data = null;
        try
        {
            data = HttpRequest.get(BaseUrl + "/getRecentHistory", true, "RIM", rim).body();
            JSONObject json = new JSONObject(data);
            data = json.getString("Data");

        } catch (Exception ex)
        {
            data = ex.getMessage();

        }

        return data;

    }

    public String getInvestmentRates()
    {
        String data = null;
        try
        {
            data = HttpRequest.get(BaseUrl + "/getInvestmentRates").body();
            JSONObject json = new JSONObject(data);
            data = json.getString("Data");

        } catch (Exception ex)
        {
            data = ex.getMessage();

        }

        return data;
    }

    public String SaveDevID(String rim, String devID)
    {
        String data = null;
        String res = null;

        try
        {
            data = HttpRequest.get(BaseUrl + "/RegisterDeviceOLD",
                    true, "rim", rim, "devID", devID).body();
            if (data.contains("device Already registered"))
            {
                throw new Exception("DeviceExistsOnDB");
            }
            if (data.contains("registration Successful"))
            {
                res = "success";
            }

        } catch (Exception ex)
        {
            data = ex.getMessage();
            res = ex.getMessage();

        }

        return res;
    }

    public String SearchTransactions(String acct)
    {
        String data = null;

        try
        {
            data = HttpRequest.get(BaseUrl + "/SearchTransactions", true, "acct", acct).body();
            JSONObject json = new JSONObject(data);
            data = json.getString("Data");

        } catch (Exception ex)
        {
            data = ex.getMessage();

        }

        return data;

    }

    public String Authenticate(UserAccessClass userAccessClass)
    {
        String data = null;
        try
        {
            String jData = new Gson().toJson(userAccessClass);
            HttpRequest request = new HttpRequest(BaseUrl + "/Authenticate", HttpRequest.METHOD_POST);
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
