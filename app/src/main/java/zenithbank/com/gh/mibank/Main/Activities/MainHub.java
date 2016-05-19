package zenithbank.com.gh.mibank.Main.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 2/27/2016.
 */
public class MainHub extends FragmentActivity


{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.main_home);

        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }


}
