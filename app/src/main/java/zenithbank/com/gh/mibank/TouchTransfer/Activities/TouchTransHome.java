package zenithbank.com.gh.mibank.TouchTransfer.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import zenithbank.com.gh.mibank.R;
import zenithbank.com.gh.mibank.TouchTransfer.System.NFCUtils;

/**
 * Created by Robby on 4/15/2016.
 */
public class TouchTransHome extends ActionBarActivity
{
    //private final String _MIME_TYPE = "text/plain";
    private final String _MIME_TYPE = "application/vnd.zenithbank.com.gh.mibank";
    Button write_tag;
    NdefMessage message;
    private NfcAdapter _nfcAdapter;
    private PendingIntent _pendingIntent;
    private IntentFilter[] _readIntentFilters;
    private IntentFilter[] _writeIntentFilters;
    private Intent _intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_home_layout);
        write_tag = (Button) findViewById(R.id.write_tag);

        write_tag.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _enableNdefExchangeMode();
            }
        });
        _init();
    }

    //initialize adapter and check for availability
    private void _init()
    {
        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (_nfcAdapter == null)
        {
            Toast.makeText(this, "This device does not support NFC.", Toast.LENGTH_LONG).show();
            TouchTransHome.this.finish();
        }

        if (_nfcAdapter.isEnabled())
        {
            _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try
            {
                ndefDetected.addDataType(_MIME_TYPE);
            } catch (IntentFilter.MalformedMimeTypeException e)
            {
                Log.e(this.toString(), e.getMessage());
            }

            _readIntentFilters = new IntentFilter[]{ndefDetected};
        }
    }

    private void _enableNdefExchangeMode()
    {
        _nfcAdapter.setNdefPushMessage(_getNdefMessage(), this);
        //_nfcAdapter.enableForegroundDispatch(this, _pendingIntent, _readIntentFilters, null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        //_enableNdefExchangeMode();
        //_enableTagWriteMode();
    }

    private void _enableTagWriteMode()
    {
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        _writeIntentFilters = new IntentFilter[]{tagDetected};
        _nfcAdapter.enableForegroundDispatch(this, _pendingIntent, _writeIntentFilters, null);
    }

    private NdefMessage _getNdefMessage()
    {
        String stringMessage;

        try
        {
            EditText messageTextField = (EditText) findViewById(R.id.message_text_field);
            stringMessage = " " + messageTextField.getText().toString();

            message = NFCUtils.getNewMessage(_MIME_TYPE, stringMessage.getBytes());
        } catch (Exception ex)
        {
            ex.getMessage();
        }


        return message;
    }

    private void _writeMessage()
    {
        Tag detectedTag = _intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (NFCUtils.writeMessageToTag(_getNdefMessage(), detectedTag))
        {
            Toast.makeText(this, "Successfully wrote message to NFC tag", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Write failed", Toast.LENGTH_LONG).show();
        }
    }

    private void _readMessage()
    {
        List<String> msgs = NFCUtils.getStringsFromNfcIntent(_intent);

        Toast.makeText(this, "Message : " + msgs.get(0), Toast.LENGTH_LONG).show();
    }
}
