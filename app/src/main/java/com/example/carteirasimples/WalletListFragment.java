package com.example.carteirasimples;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;


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

        Overview overview = (Overview) getActivity();
        adapter = new WalletValuesAdapter(overview.valuesAdded);
        walletRecyclerView.setAdapter(adapter);

    }
}
