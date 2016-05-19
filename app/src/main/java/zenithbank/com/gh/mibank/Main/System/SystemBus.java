package zenithbank.com.gh.mibank.Main.System;

import com.squareup.otto.Bus;


class SystemBus
{

    private static Bus _bus = new Bus();

    //create getter for the buss to allow others to access it

    public static Bus getBus()
    {
        return _bus;
    }
}
