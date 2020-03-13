package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExpensesListFragment extends Fragment {
    private View view;
    private Toolbar expensesfarg_TOOL_toolbar;
    private Spinner expensesfarg_SPN_filter;
    private RecyclerView expensesfarg_LST_list;
    private FloatingActionButton expensesfarg_FAB_add;
    private CallBack_AddExpenses callBack_addExpenses;
    private Adapter_ExpendedModel adapter_expendedModel;
    private String[] years;


    public ExpensesListFragment(CallBack_AddExpenses callBack_addExpenses) {
        this.callBack_addExpenses = callBack_addExpenses;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_expenses_list, container, false);
        }

        findViews(view);
        expensesfarg_TOOL_toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        expensesfarg_TOOL_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        expensesfarg_FAB_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack_addExpenses.add();
            }
        });
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
        years = new String[currentYear - 2020 + 1];
        for (int i = currentYear; i >= 2020; i--)
            years[i - 2020] = String.valueOf(i);


        ArrayAdapter<String> aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, years);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        expensesfarg_SPN_filter.setAdapter(aa);
        expensesfarg_SPN_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initList(SortAndFilter.expensesFilterByYear(years[position], FilesManager.getInstance().getExpenses()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }


    private void initList(ArrayList<Expenses> e) {
        adapter_expendedModel = new Adapter_ExpendedModel(getContext(), e);
        expensesfarg_LST_list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        expensesfarg_LST_list.setLayoutManager(layoutManager);
        expensesfarg_LST_list.setAdapter(adapter_expendedModel);


    }


    private void findViews(View view) {
        expensesfarg_TOOL_toolbar = view.findViewById(R.id.expensesfarg_TOOL_toolbar);
        expensesfarg_SPN_filter = view.findViewById(R.id.expensesfarg_SPN_filter);
        expensesfarg_LST_list = view.findViewById(R.id.expensesfarg_LST_list);
        expensesfarg_FAB_add = view.findViewById(R.id.expensesfarg_FAB_add);

    }
}
