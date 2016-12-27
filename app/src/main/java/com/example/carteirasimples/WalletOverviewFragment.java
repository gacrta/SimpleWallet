package com.example.carteirasimples;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WalletOverviewFragment extends Fragment {

    private ViewWalletListListener mListener;

    public interface ViewWalletListListener {
        void onButtonClick();
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
        View view = inflater.inflate(R.layout.fragment_wallet_overview, container, false);
        Button button = (Button) view.findViewById(R.id.hit_me_button);
        if (getActivity().findViewById(R.id.fragment_container) == null) {
            button.setVisibility(View.INVISIBLE);
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onButtonClick();
                }
            });
        }

        return view;
    }

}
