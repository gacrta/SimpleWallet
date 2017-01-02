package com.example.carteirasimples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WalletListFragment extends Fragment{
    private static final String currencyURL =
            "https://openexchangerates.org/api/latest.json?app_id=101bdb4a779b4ad7b0584069b8fd323b&currencies.json";

    private RecyclerView walletRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_wallet_list, container, false);

        walletRecyclerView = (RecyclerView) mView.findViewById(R.id.wallet_recycler_view);

        //layout size is not affected by content
        walletRecyclerView.setHasFixedSize(true);

        //using linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        walletRecyclerView.setLayoutManager(layoutManager);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        updateList();
    }

    public void updateList() {
        Overview overview = (Overview) getActivity();
        adapter = new WalletValuesAdapter(overview.valuesAdded);
        walletRecyclerView.setAdapter(adapter);
    }
}
