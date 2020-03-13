package com.example.myapplication_finalproject;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class ExpensesActivity extends AppCompatActivity {
    private FrameLayout expenses_LAY_frag;
    private CallBack_AddExpenses callBack_addExpenses;
    private CallBack_SaveExpenses callBack_saveExpenses;
    private ExpensesListFragment expensesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        findViews();
        callBack_saveExpenses = new CallBack_SaveExpenses() {
            @Override
            public void save() {
                showList();
            }

            @Override
            public void back() {
                showList();
            }
        };
        callBack_addExpenses = new CallBack_AddExpenses() {
            @Override
            public void add() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.expenses_LAY_frag, new NewExpenses(callBack_saveExpenses));
                transaction.commit();

            }
        };

        expensesListFragment = new ExpensesListFragment(callBack_addExpenses);


        showList();
    }

    private void showList() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.expenses_LAY_frag, expensesListFragment);
        transaction.commit();
    }

    private void findViews() {
        expenses_LAY_frag = findViewById(R.id.expenses_LAY_frag);

    }


}
