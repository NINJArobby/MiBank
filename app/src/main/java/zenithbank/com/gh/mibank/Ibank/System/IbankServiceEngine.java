package zenithbank.com.gh.mibank.Ibank.System;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import zenithbank.com.gh.mibank.Ibank.Classes.InterbankTransferClass;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferBeneficiaryClass;
import zenithbank.com.gh.mibank.Ibank.Classes.TransferRequestClass;
import zenithbank.com.gh.mibank.Main.System.ExSSLSocketFactory;


public class IbankServiceEngine extends Activity
{
    IbankDatabaseEngine dbEngine;
    CookieStore _cookieStore;
    String cookie1;
    String cookie2;
    String SOAP_ACTION_VERIFY_ACCT = "http://tempuri.org/GetAccountName";
    String SOAP_METHOD_NAME_AUTHENTICATE = "GetAccountTitle";
    String BaseUrl = "http://www.zenithbank.com.gh/MobileService/api/MiBank";
    String SOAP_NAMESPACE = "http://tempuri.org/";
    int timeout = 50000;


    public static HttpClient getHttpsClient(HttpClient httpClient)
    {
        try
        {
            X509TrustManager x509TrustManager = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType)
                {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType)
                {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = httpClient.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, httpClient.getParams());
        } catch (Exception ex)
        {
            return null;
        }
    }

    //TODO CHANGE TEST URL TO LIVE
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbEngine = new IbankDatabaseEngine();
        dbEngine.Init(this);
    }

    public String getCookieFromServer()
    {

        ArrayList<String> newCookieStore = new ArrayList<>();
        HttpClient client = getHttpsClient(new DefaultHttpClient());
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
        try
        {
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/profile";

            //TODO remove below test url
            //String url = "http://196.216.180.26/restful-service/internet-banking/profile";
            HttpGet httpPost = new HttpGet(url);
            httpPost.setParams(params);
            HttpResponse response = client.execute(httpPost, localContext);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                Header[] headers = response.getAllHeaders();
                for (Header h : headers)
                {
                    newCookieStore.add(h.getName() + ";" + h.getValue());
                }

                cookie1 = newCookieStore.get(3).trim();
                cookie2 = newCookieStore.get(7).trim();
                cookie1 = cookie1.substring(11);
                cookie2 = cookie2.substring(11);

                cookie1 = getString(cookie1);
                cookie2 = getString(cookie2);

                Log.d("cookieStatus", "Cookie loaded");
            }
            else
            {
                throw new Exception("FetchCookieError");
            }

        } catch (Exception ex)
        {
            cookie1 = ex.getLocalizedMessage();
            cookie2 = " error";
            ex.getMessage();
        }
        return cookie1 + ";" + cookie2;
    }

    public ArrayList<String> SignUp(String AccessCode, String Username, String Password)
    {
        ArrayList<String> data = new ArrayList<>();
        try
        {
            String cookie = getCookieFromServer();
            if (cookie.contains("error"))
            {
                throw new Exception("fetchCookieError");
            }

            data.add(cookie);
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "j_security_check?j_username=" + AccessCode + ":" + Username + "&j_password="
                    + Password;

            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                data.add(feedback);
            }
            else
            {
                throw new Exception("LoginErrorException");
            }
        } catch (Exception ex)
        {
            data.add("error:" + ex.getMessage());
        }
        return data;
    }

    String getResponse(HttpResponse response)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return sb.toString().trim();
    }

    public String getString(String data)
    {
        String message = data;
        String delimit = ";";
        StringTokenizer ress = new StringTokenizer(message, delimit);
        String[] splitStr = new String[ress.countTokens()];
        int index = 0;
        while (ress.hasMoreElements())
        {
            splitStr[index++] = ress.nextToken();
        }

        return splitStr[0].trim();
    }

    public String DoIntraAccountTransfer(
            TransferRequestClass.RootObject transferRequest,
            String cookie)
    {

        String feedback = null;
        try
        {
            String URL = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/transfer";
            String jsonString = new Gson().toJson(transferRequest);
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(new URI(URL));
            httpPost.setEntity(new StringEntity(jsonString));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Cookie", cookie);
            httpPost.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                if (feedback.contains("To login send a get"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                feedback = getResponse(response);
                throw new Exception("TransferErrorException");
            }
        } catch (Exception e)
        {
            feedback = e.getMessage();
            e.printStackTrace();
        }

        return feedback;
    }

    public String DoInterAccountTransfer(
            TransferRequestClass.RootObject transferRequest,
            String cookie)
    {
        String feedback = null;
        try
        {
            String URL = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/transfer";
            String jsonString = new Gson().toJson(transferRequest);
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(new URI(URL));
            httpPost.setEntity(new StringEntity(jsonString));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Cookie", cookie);
            httpPost.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpPost, ctx);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                if (feedback.contains("To login send a get"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                throw new Exception("TransferErrorException");
            }
        } catch (Exception e)
        {
            feedback = e.getMessage();
            e.printStackTrace();
        }

        return feedback;
    }

    public String DoInterBankTransfer(
            InterbankTransferClass.RootObject transferRequest,
            String cookie)
    {
        String feedback = null;
        try
        {
            String URL = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "external-transfer";
            String jsonString = new Gson().toJson(transferRequest);
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpPost httpPost = new HttpPost(new URI(URL));
            httpPost.setEntity(new StringEntity(jsonString));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Cookie", cookie);
            httpPost.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpPost, ctx);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                if (feedback.contains("To login send a get"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                throw new Exception("TransferErrorException");
            }
        } catch (Exception e)
        {
            feedback = e.getMessage();
            e.printStackTrace();
        }

        return feedback;
    }

    public String GetAccountBalances(String cookie)
    {
        String AccountBallances = "";
        try
        {
            if (cookie.contains("error"))
            {
                throw new Exception("fetchCookieError");
            }
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "account-balances";
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                AccountBallances = feedback;
                if (AccountBallances.contains("To login"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                throw new Exception("LoginErrorException");
            }
        } catch (Exception ex)
        {
            AccountBallances = ex.getMessage();
        }
        return AccountBallances;
    }

    public String GetBillInformation(String cookie)
    {
        String AccountBallances = "";
        try
        {
            if (cookie.contains("error"))
            {
                throw new Exception("fetchCookieError");
            }
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "bill-information";
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                AccountBallances = feedback;
                if (AccountBallances.contains("To login"))
                {
                    throw new Exception("DeadCookie");
                }
                if (AccountBallances.isEmpty())
                {
                    throw new Exception("NoDataError");
                }
            }
            else
            {
                throw new Exception("LoginErrorException");
            }
        } catch (Exception ex)
        {
            AccountBallances = ex.getMessage();
        }
        return AccountBallances;
    }

    public boolean CanTransfer(String acct, String cookie)
    {
        boolean done = false;
        try
        {
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "transfer-authorization?fromacct=" + acct;
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                if (feedback.equalsIgnoreCase("true"))
                {
                    done = true;
                }
            }
            else
            {
                throw new Exception("LoginErrorException");
            }

        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return done;

    }

    public boolean RequestToken(String cookie)
    {
        boolean done = false;
        try
        {
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "token/smsemail";
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);
                if (feedback.equalsIgnoreCase("true"))
                {
                    done = true;
                }
            }
            else
            {
                throw new Exception("RequestTokenError");
            }

        } catch (Exception ex)
        {
            ex.getMessage();
        }

        return done;
    }

    public String ValidateAccount(String account)
    {
        String data = null;
        try
        {
            data = HttpRequest.get(BaseUrl + "/GetAccountTitle", true, "acctno", account).body();
        } catch (Exception ex)
        {
            data = ex.getMessage();
        }


        return data;
    }

    public String GetTransferBeneficiares(String cookie)
    {
        String AccountBalances = "";
        try
        {
            if (cookie.contains("error"))
            {
                throw new Exception("fetchCookieError");
            }
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "external-transfer-beneficiaries";
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)

            {
                feedback = getResponse(response);
                AccountBalances = feedback;
                if (AccountBalances.contains("To login"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                throw new Exception("LoginErrorException");
            }
        } catch (Exception ex)
        {
            AccountBalances = ex.getMessage();
        }
        return AccountBalances;
    }

    public String GetTransferProducts(String cookie)
    {
        String AccountBalances = "";
        try
        {
            if (cookie.contains("error"))
            {
                throw new Exception("fetchCookieError");
            }
            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "external-transfer-products";
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpGet httpGet = new HttpGet(new URI(url));
            httpGet.setHeader("Cookie", cookie);
            httpGet.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpGet, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)

            {
                feedback = getResponse(response);
                AccountBalances = feedback;
                if (AccountBalances.contains("To login"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                throw new Exception("LoginErrorException");
            }
        } catch (Exception ex)
        {
            AccountBalances = ex.getMessage();
        }
        return AccountBalances;
    }

    public boolean AddNewBenefeciary(TransferBeneficiaryClass.RootObject data, String cookie)
    {
        boolean AccountBalances = false;
        try
        {
            String jsonString = new Gson().toJson(data);
            if (cookie.contains("error"))
            {
                throw new Exception("fetchCookieError");
            }

            String feedback;
            String url = "https://ibank.zenithbank.com.gh/restful-service/internet-banking/" +
                    "external-transfer-beneficiary";
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpPut httpPut = new HttpPut(new URI(url));
            httpPut.setEntity(new StringEntity(jsonString));
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setHeader("Cookie", cookie);
            httpPut.setParams(params);
            HttpContext ctx = new BasicHttpContext();
            ctx.setAttribute(ClientContext.COOKIE_STORE, _cookieStore);
            HttpResponse response = client.execute(httpPut, ctx);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200)
            {
                feedback = getResponse(response);

                if (feedback.contains("To login"))
                {
                    throw new Exception("DeadCookie");
                }
            }
            else
            {
                throw new Exception("LoginErrorException");
            }
            AccountBalances = true;
        } catch (Exception ex)
        {
            ex.getMessage();
        }
        return AccountBalances;
    }


}
