package zenithbank.com.gh.mibank.Ibank.System;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Robby on 4/4/2016.
 */
public class I_BusStation
{
    private static Bus _bus = new Bus(ThreadEnforcer.ANY);

    //create getter for the buss to allow others to access it

    public static Bus getBus()
    {
        return _bus;
    }
}
