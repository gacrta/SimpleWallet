package com.example.carteirasimples;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WalletOverviewFragment extends Fragment {

    private ViewWalletListListener mListener;
    private View mView;

    public interface ViewWalletListListener {
        void viewWalletDetails();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ViewWalletListListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement ViewWalletListListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wallet_overview, container, false);
        Button button = (Button) mView.findViewById(R.id.button_to_wallet_list);
        if (getActivity().findViewById(R.id.fragment_container) == null) {
            button.setVisibility(View.INVISIBLE);
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.viewWalletDetails();
                }
            });
        }
        updateSummary();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSummary();
    }

    // function to evaluate total income
    public float getIncomeSum() {
        Overview overview = (Overview) getActivity();
        int valuesNumber = overview.valuesAdded.size();
        float sum = 0;
        WalletValue walletValue;
        for(int i = 0; i < valuesNumber; i++) {
            walletValue = overview.valuesAdded.get(i);
            if (walletValue.getSign()) {
                sum += walletValue.getValue();
            }
        }
        return sum;
    }

    // function to evaluate total outcome
    public float getOutcomeSum() {
        Overview overview = (Overview) getActivity();
        int valuesNumber = overview.valuesAdded.size();
        float sum = 0;
        WalletValue walletValue;
        for(int i = 0; i < valuesNumber; i++) {
            walletValue = overview.valuesAdded.get(i);
            if (!walletValue.getSign()) {
                sum += walletValue.getValue();
            }
        }
        return sum;
    }

    protected void updateSummary() {
        float income = getIncomeSum();
        float outcome = getOutcomeSum();
        float balance = income - outcome;

        TextView tv_income = (TextView) mView.findViewById(R.id.tv_income_value);
        tv_income.setText(String.format(java.util.Locale.getDefault(),"%.2f", income));
        TextView tv_outcome = (TextView) mView.findViewById(R.id.tv_outcome_value);
        tv_outcome.setText(String.format(java.util.Locale.getDefault(),"%.2f", outcome));
        TextView tv_balance = (TextView) mView.findViewById(R.id.tv_balance_value);
        tv_balance.setText(String.format(java.util.Locale.getDefault(),"%.2f", balance));
        if (balance < 0.0) {
            tv_balance.setTextColor(Color.parseColor("#a00103"));
        }
        else {
            tv_balance.setTextColor(Color.parseColor("#74ba48"));
        }
    }

}
