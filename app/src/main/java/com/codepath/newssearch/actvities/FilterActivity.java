package com.codepath.newssearch.actvities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.newssearch.R;
import com.codepath.newssearch.fragments.DatePickerFragment;
import com.codepath.newssearch.models.SearchModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.sortOrderSpinner)
    Spinner spinnerSortOrder;
    @BindView(R.id.etBeginDate)
    EditText etBeginDate;
    @BindView(R.id.cbArts)
    CheckBox cbArts;
    @BindView(R.id.cbFashion)
    CheckBox cbFashion;
    @BindView(R.id.cbSports)
    CheckBox cbSports;
    @BindView(R.id.btnSave)
    Button btnSave;

    String beginDateSelected = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Filters");

        etBeginDate.setActivated(false);
        etBeginDate.setOnClickListener(v->{
                showDatePickerDialog(v);
        });

        btnSave.setOnClickListener(v->{
                Intent intent = new Intent();
                intent.putExtra(SearchActivity.EXTRA_SEARCH_MODEL,getSearchModel());
                setResult(Activity.RESULT_OK,intent);
                finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private SearchModel getSearchModel(){
        SearchModel searchModel = new SearchModel();
        if(etBeginDate.getText().toString().length() > 0){
            searchModel.setBeginDate(beginDateSelected);
        }
        searchModel.setSortOrder(spinnerSortOrder.getSelectedItem().toString());
        List<String> categories = new ArrayList<>();
        if(cbArts.isChecked()){
            categories.add(cbArts.getText().toString());
        }
        if(cbFashion.isChecked()){
            //categories.add(cbFashion.getText().toString());
            categories.add("Fashion & Style");
        }
        if(cbSports.isChecked()){
            categories.add(cbSports.getText().toString());
        }
        searchModel.setCategories(categories);
        return searchModel;
    }
    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        beginDateSelected = sdf.format(c.getTime());//String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
        sdf = new SimpleDateFormat("MM-dd-yyyy");
        etBeginDate.setText(sdf.format(c.getTime()));
    }
}
