package zenithbank.com.gh.mibank.TouchTransfer.Activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import zenithbank.com.gh.mibank.R;


public class TouchTransReceive extends Activity
{

    private static final String TAG = "stickynotes";
    NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;
    private Intent _intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_receive_layout);

        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try
        {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException ex)
        {
            ex.getMessage();
        }
        mNdefExchangeFilters = new IntentFilter[]{ndefDetected};

        Toast.makeText(this, "Complete", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        _intent = intent;

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {

        }

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction()))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Action")
                    .setPositiveButton("ok",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                }
                            })
                    .setNegativeButton("cancel",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                }
                            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
