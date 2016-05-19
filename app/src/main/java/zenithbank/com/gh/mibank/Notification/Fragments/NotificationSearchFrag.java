package zenithbank.com.gh.mibank.Notification.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.StringTokenizer;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import zenithbank.com.gh.mibank.Main.System.BusStation;
import zenithbank.com.gh.mibank.Main.System.MibankServiceEngine;
import zenithbank.com.gh.mibank.Main.System.System_DBEngine;
import zenithbank.com.gh.mibank.Notification.Adapters.N_HistoryAdapter;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryClass;
import zenithbank.com.gh.mibank.Notification.Classes.N_HistoryMessage;
import zenithbank.com.gh.mibank.Notification.Classes.N_SearchErrorMessage;
import zenithbank.com.gh.mibank.Notification.System.N_SearchUpdater;
import zenithbank.com.gh.mibank.Notification.System.N_SystemService;
import zenithbank.com.gh.mibank.R;


public class NotificationSearchFrag extends Fragment
{

    TextView txtFromdate, txtEndDatae;
    Button btnSearch;
    Spinner SpnAccts;
    ProgressDialog progressDialog;
    String fromddate, todate, acctNo;
    MibankServiceEngine serviceEngine;
    ListView listView;
    ArrayList<String> _accts;
    N_SystemService n_systemService;
    System_DBEngine system_dbEngine;
    AlertDialog alertDialog;
    // FloatingActionButton fabExcel,fabPDF;
    FloatingActionMenu fabMenu;
    N_HistoryMessage Smessage;
    int resCount = 0;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    public NotificationSearchFrag()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = container;
        try
        {
            view = inflater.inflate(R.layout.fragment_notification_search, container, false);
            txtFromdate = (TextView) view.findViewById(R.id.txtFromdate);
            txtEndDatae = (TextView) view.findViewById(R.id.txtEndDate);
            btnSearch = (Button) view.findViewById(R.id.btnSearch);
            SpnAccts = (Spinner) view.findViewById(R.id.SpnAccts);
            listView = (ListView) view.findViewById(R.id.listTrsx1);

            fabMenu = (FloatingActionMenu) view.findViewById(R.id.menufab);

            final FloatingActionButton pdfFab = new FloatingActionButton(getActivity());
            pdfFab.setButtonSize(FloatingActionButton.SIZE_MINI);
            pdfFab.setLabelText("Save As PDF");
            pdfFab.setImageResource(R.drawable.ic_file_pdf_box);
            pdfFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (resCount < 1)
                    {
                        Toast.makeText(getActivity(), "No Data Loaded", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        saveAsPDF(Smessage, SpnAccts.getSelectedItem().toString().trim());
                    }
                }
            });
            fabMenu.addMenuButton(pdfFab);

            final FloatingActionButton ExcelFab = new FloatingActionButton(getActivity());
            ExcelFab.setButtonSize(FloatingActionButton.SIZE_MINI);
            ExcelFab.setLabelText("Save As excel");
            ExcelFab.setImageResource(R.drawable.ic_file_excel_box);
            ExcelFab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (resCount < 1)
                    {
                        Toast.makeText(getActivity(), "No Data Loaded", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        saveAsExcel(Smessage, SpnAccts.getSelectedItem().toString().trim());
                    }
                }
            });
            fabMenu.addMenuButton(ExcelFab);

            n_systemService = new N_SystemService();
            system_dbEngine = new System_DBEngine();
            system_dbEngine.Init(getActivity());

            BusStation.getBus().register(this);
            dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Calendar newCalendar = Calendar.getInstance();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {

                    TextView desc = (TextView) view.findViewById(R.id.txtListDesc);
                    TextView amt = (TextView) view.findViewById(R.id.txtListAmount);
                    TextView acct = (TextView) view.findViewById(R.id.txtListAccount);
                    //TextView crncy = (TextView) view.findViewById(R.id.txtCrncy);
                    TextView date = (TextView) view.findViewById(R.id.txtListDate);
                    TextView type = (TextView) view.findViewById(R.id.txtListType);

                    makePopUp2(date.getText().toString().trim(),
                            acct.getText().toString().trim(),
                            amt.getText().toString().trim(),
                            desc.getText().toString().trim(),
                            type.getText().toString().trim()
                    );
                }
            });

            txtEndDatae.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    toDatePickerDialog.show();
                }
            });
            txtFromdate.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    fromDatePickerDialog.show();
                }
            });
            fromDatePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener()
                    {

                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth)
                        {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            txtFromdate.setText(dateFormatter.format(newDate.getTime()));

                        }
                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH));

            toDatePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener()
                    {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                        {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            txtEndDatae.setText(dateFormatter.format(newDate.getTime()));
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                    newCalendar.get(Calendar.DAY_OF_MONTH));


            btnSearch.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    fromddate = txtFromdate.getText().toString().trim();
                    todate = txtEndDatae.getText().toString().trim();
                    acctNo = SpnAccts.getSelectedItem().toString().trim();
                    progressDialog = ProgressDialog.show(getActivity(),
                            "Fetching Transactions...", "Please Wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    Intent fetchersService = new Intent(getActivity(), N_SearchUpdater.class);
                    fetchersService.putExtra("fromDate", fromddate);
                    fetchersService.putExtra("toDate", todate);
                    fetchersService.putExtra("acct", acctNo);
                    getActivity().startService(fetchersService);

                }
            });

        } catch (Exception ex)
        {
            ex.getMessage();
        }

        progressDialog = ProgressDialog.show(getActivity(),
                "Fetching Accounts", "Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        new fetchAccounts().execute();
        return view;
    }

    private void saveAsExcel(N_HistoryMessage smessage, String acct)
    {

        try
        {
            //Saving file in external storage
            String dir = Environment.getExternalStorageDirectory() +
                    File.separator + "MiBank_Downloads";
            File folder = new File(dir);
            folder.mkdirs();

            File file = new File(dir, "TransactionHistory.xls");

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Transactions for " + acct, 0);
            sheet.addCell(new Label(0, 0, "Date")); // column and row
            sheet.addCell(new Label(1, 0, "Account"));
            sheet.addCell(new Label(2, 0, "Amount")); // column and row
            sheet.addCell(new Label(3, 0, "Type"));
            sheet.addCell(new Label(4, 0, "Description")); // column and row
            sheet.addCell(new Label(5, 0, "Currency"));
            sheet.addCell(new Label(6, 0, "Branch")); // column and row
            sheet.addCell(new Label(7, 0, "Balance"));

            int i = 0;
            for (N_HistoryClass.RootObject dd : smessage.getTransactions())
            {
                sheet.addCell(new Label(0, i, dd.trsxDate)); // column and row
                sheet.addCell(new Label(1, i, dd.trsxAcctno));
                sheet.addCell(new Label(2, i, dd.trsxAmount));
                sheet.addCell(new Label(3, i, dd.trsxType));
                sheet.addCell(new Label(4, i, dd.trsxDecsription));
                sheet.addCell(new Label(5, i, dd.trsxCurrency));
                sheet.addCell(new Label(6, i, dd.trsxBranch));
                sheet.addCell(new Label(7, i, dd.trsxBalance));
                i++;
            }
            workbook.write();
            workbook.close();
            fabMenu.close(true);

            Toast.makeText(getActivity(), "Excel Saved", Toast.LENGTH_SHORT).show();
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void saveAsPDF(N_HistoryMessage smessage, String acct)
    {
        try
        {
            String dir = Environment.getExternalStorageDirectory() +
                    File.separator + "MiBank_Downloads";
            File folder = new File(dir);
            folder.mkdirs();

            File file = new File(dir, "TransactionHistory.pdf");
            Document document = new Document();  // create the document
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Paragraph p3 = new Paragraph();
            p3.add("Your Transaction History for account no: " + acct);
            document.add(p3);

            PdfPTable table = new PdfPTable(8);
            table.addCell("Date");
            table.addCell("Account");
            table.addCell("Amount");
            table.addCell("Type");
            table.addCell("Description");
            table.addCell("Currency");
            table.addCell("Branch");
            table.addCell("Balance");

            for (N_HistoryClass.RootObject dd : smessage.getTransactions())
            {
                table.addCell(dd.trsxDate);
                table.addCell(dd.trsxAcctno);
                table.addCell(dd.trsxAmount);
                table.addCell(dd.trsxType);
                table.addCell(dd.trsxDecsription);
                table.addCell(dd.trsxCurrency);
                table.addCell(dd.trsxBranch);
                table.addCell(dd.trsxBalance);
            }
            document.add(table);
            document.addCreationDate();
            document.close();
            fabMenu.close(true);

            Toast.makeText(getActivity(), "PDF Saved", Toast.LENGTH_SHORT).show();

        } catch (Exception ex)
        {
            ex.getMessage();
        }


    }

    @Subscribe
    public void onSearchDataRecived(final N_HistoryMessage message)
    {
        Smessage = message;
        try
        {
            resCount = message.getTransactions().size();
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    progressDialog.dismiss();
                    N_HistoryAdapter adapter = new N_HistoryAdapter(getActivity(),
                            message.getTransactions());
                    listView.setAdapter(adapter);
                }
            });
        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    public void onSearchErrorRecieved(final N_SearchErrorMessage messge)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                AlertError();
            }
        });
    }

    private void AlertError()
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("Error!!!")
                .setMessage("No data returned.")
                .setNegativeButton("ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).show();
    }

    void makePopUp2(String date, String account, String amount, String desc, String type)
    {
        try
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("TRANSACTION")
                    .setMessage(
                            date + "\n" +
                                    "Account Number: " + account + "\n" +
                                    "Amount " + amount + "\n" +
                                    "Desc: " + desc + "\n " +
                                    "Type: " + type + "\n"

                    )
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    })
                    .setIcon(R.drawable.zenith)
                    .show();
        } catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    class fetchAccounts extends AsyncTask<String, String, String>
    {
        ArrayList<String> acctList = new ArrayList<>();
        boolean done = false;

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String data = n_systemService.getAccounts(system_dbEngine.getRim()).trim();

                if (data.contains("error"))
                {
                    throw new Exception("error");
                }
                data = data.replace("[", "");
                data = data.replace("]", "");
                data = data.replace("\"", "");
                data = data.replace(",", ":");
                String delimit = ":";

                StringTokenizer ress = new StringTokenizer(data, delimit);
                String[] splitStr = new String[ress.countTokens()];
                int count = ress.countTokens();
                int index = 0;
                while (ress.hasMoreElements())
                {
                    splitStr[index++] = ress.nextToken();
                    //acctList.add(ress.nextToken());
                }

                Collections.addAll(acctList, splitStr);
                done = true;
            } catch (Exception ex)
            {
                ex.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressDialog.dismiss();
            if (done)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (getActivity(), android.R.layout.simple_spinner_item, acctList);

                        dataAdapter.setDropDownViewResource
                                (android.R.layout.simple_spinner_dropdown_item);
                        SpnAccts.setAdapter(dataAdapter);
                    }
                });
            }
            else
            {
                AlertError2();
            }

        }

        private void AlertError2()
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error!!!")
                    .setMessage("Account Fetch Error...Check network and retry")
                    .setNegativeButton("Back", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            getActivity().onBackPressed();
                        }
                    })
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            progressDialog = ProgressDialog.show(getActivity(),
                                    "Fetching Accounts", "Please Wait...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            new fetchAccounts().execute();
                        }
                    })
                    .show();
        }

    }


}
