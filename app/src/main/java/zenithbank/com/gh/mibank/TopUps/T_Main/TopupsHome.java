package zenithbank.com.gh.mibank.TopUps.T_Main;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import zenithbank.com.gh.mibank.R;

/**
 * Created by Robby on 4/7/2016.
 */
public class TopupsHome extends Activity
{
    String[] networks = new String[]{"Select Network", "mtn", "vodafone", "tigo", "glo"};
    Spinner netSelector;
    EditText txtNumber, txtAmount;
    ImageButton btnContact, btnPay;
    ImageView ImgNetwork;
    ListView logs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_layout);
        netSelector = (Spinner) findViewById(R.id.spnSelecor);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtNumber = (EditText) findViewById(R.id.txtNumber);
        btnContact = (ImageButton) findViewById(R.id.btnContact);
        btnPay = (ImageButton) findViewById(R.id.btnPay);
        ImgNetwork = (ImageView) findViewById(R.id.ImgNetwork);

        netSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 1:
                        ImgNetwork.setImageDrawable(getResources().getDrawable(R.drawable.mtn_logo));
                        break;
                    case 2:
                        ImgNetwork.setImageDrawable(getResources().getDrawable(R.drawable.vodafone_logo));
                        break;
                    case 3:
                        ImgNetwork.setImageDrawable(getResources().getDrawable(R.drawable.tigo));
                        break;
                    case 4:
                        ImgNetwork.setImageDrawable(getResources().getDrawable(R.drawable.glo));
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, networks);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        netSelector.setAdapter(dataAdapter);
        btnContact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
                startActivityForResult(intent, 1);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RESULT_OK)
        {
            Uri contact = data.getData();
            ContentResolver cr = getContentResolver();

            Cursor c = managedQuery(contact, null, null, null, null);
            while (c.moveToNext())
            {
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    while (pCur.moveToNext())
                    {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        txtNumber.setText(phone);
                    }
                }

            }
        }
    }
}
