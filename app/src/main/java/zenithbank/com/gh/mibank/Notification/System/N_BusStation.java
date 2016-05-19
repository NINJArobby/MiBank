package zenithbank.com.gh.mibank.Notification.System;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Robby on 3/11/2016.
 */
public class N_BusStation
{
    private static Bus _bus = new Bus(ThreadEnforcer.ANY);

    //create getter for the buss to allow others to access it

    public static Bus getBus()
    {
        return _bus;
    }
}
