package com.example.carteirasimples;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class WalletListFragment extends Fragment{

    private RecyclerView walletRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


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
        ((Overview)getActivity()).initWalletListDB();

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    public WalletValuesAdapter getAdapter() {
        return (WalletValuesAdapter) walletRecyclerView.getAdapter();
    }
    public void setAdapter() {
        adapter = new WalletValuesAdapter();
        walletRecyclerView.setAdapter(adapter);
    }
}
