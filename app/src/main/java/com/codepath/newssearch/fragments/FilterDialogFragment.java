package com.codepath.newssearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.newssearch.R;
import com.codepath.newssearch.models.SearchModel;
import com.codepath.newssearch.util.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sharath on 7/31/16.
 */
public class FilterDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
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

    public interface FilterFragmentListener{
        void onFiltersSelected(SearchModel searchModel);
    }
    public FilterDialogFragment(){

    }

    public static FilterDialogFragment newInstance(){
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        return filterDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_filter, container);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etBeginDate.setOnClickListener(v -> {
            UiUtils.hideSoftKeyboard(getActivity(),v);
            showDatePickerDialog(v);
        });

        btnSave.setOnClickListener(v->{
                FilterFragmentListener listener = (FilterFragmentListener) getActivity();
                listener.onFiltersSelected(getSearchModel());
                dismiss();
        });
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

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getChildFragmentManager(), "datePicker");
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
