package com.underarmour.nytimes.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.underarmour.nytimes.R;
import com.underarmour.nytimes.dialogs.DatePickerDialogFragment;
import com.underarmour.nytimes.storage.DataStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SearchFilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SearchFilterActivity.class.getSimpleName() ;
    private EditText etDate;
    private Spinner spinnerSort;
    private String sortOrderSelected;
    private ArrayList<String> listNewsdesk;
    private String strBeginDate;
    private Button btnSave;
    private DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);

        setView();
    }

    /**
     * initialize all view
     */
    private void setView() {

        dataStorage = DataStorage.getInstance();
        etDate = (EditText)findViewById(R.id.etBeginDate);
        etDate.setOnClickListener(this);
        spinnerSort = (Spinner) findViewById(R.id.spSortOrder);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sort_order_array, R.layout.spinner_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(this);
        listNewsdesk = new ArrayList<String>();

        setCheckBoxes();
    }

    private void setCheckBoxes() {

        // This actually applies the listener to the checkboxes
        // by calling `setOnCheckedChangeListener` on each one
        CheckBox cbArts = (CheckBox)findViewById(R.id.cbArts);
        CheckBox cbFashion = (CheckBox)findViewById(R.id.cbFashion);
        CheckBox cbSports = (CheckBox)findViewById(R.id.cbSports);
        CheckBox cbLifeStyle = (CheckBox)findViewById(R.id.cbLifeStyle);

        cbArts.setOnCheckedChangeListener(this);
        cbFashion.setOnCheckedChangeListener(this);
        cbSports.setOnCheckedChangeListener(this);
        cbLifeStyle.setOnCheckedChangeListener(this);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

        // store the values selected into a Calendar instance
        final Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String beginDate = simpleDateFormat.format(cal.getTime());
        etDate.setText(beginDate);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DATE);
        if(month<10) {
            strBeginDate = year + "0" + month + "" + day;
        }else{
            strBeginDate = year + "" + month + "" + day;
        }
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.etBeginDate:
                showDatePickerDialog(view);
                break;
            case R.id.btnSave:
                dataStorage.saveData("Date",strBeginDate);
                dataStorage.saveData("Sort",sortOrderSelected);
                dataStorage.saveObject("Newsdesk",listNewsdesk);
                finish();
                break;
            default:break;
        }
    }

    private void showDatePickerDialog(View view) {

        DatePickerDialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        sortOrderSelected = (String)adapterView.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        sortOrderSelected = spinnerSort.getPrompt().toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean checked) {

        switch (view.getId()){

            case R.id.cbArts:

                String strArts = getResources().getString(R.string.arts);
                if(checked){
                    listNewsdesk.add(strArts);
                }else{
                    if(listNewsdesk.contains(strArts))
                        listNewsdesk.remove(strArts);
                }
                break;
            case R.id.cbFashion:

                String strFashion = getResources().getString(R.string.fashion);
                if(checked){
                    listNewsdesk.add(strFashion);
                }else{
                    if(listNewsdesk.contains(strFashion)){
                        listNewsdesk.remove(strFashion);
                    }
                }
                break;
            case R.id.cbSports:
                String strSports = getResources().getString(R.string.sports);
                if(checked){
                    listNewsdesk.add(strSports);
                }else{
                    if(listNewsdesk.contains(strSports))
                        listNewsdesk.remove(strSports);
                }
                break;
            case R.id.cbLifeStyle:
                String strLifestyle = getResources().getString(R.string.lifestyle);
                if(checked){
                    listNewsdesk.add(strLifestyle);
                }else{
                    if(listNewsdesk.contains(strLifestyle))
                        listNewsdesk.remove(strLifestyle);
                }
                break;
        }
    }
}