package com.example.carteirasimples;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by gabriel.almeida on 19/12/2016.
 */

public class WalletValuesAdapter extends RecyclerView.Adapter<WalletValuesAdapter.ViewHolder> {
    private List<WalletValue> obj;
    private static final String currencyURL =
            "https://openexchangerates.org/api/latest.json?app_id=101bdb4a779b4ad7b0584069b8fd323b&currencies.json";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView iv;
        public TextView tvValue;
        public TextView tvCategory;
        public TextView tvDate;

        public ViewHolder (View v) {
            super(v);
            v.setOnClickListener(this);
            iv = (ImageView) v.findViewById(R.id.sign_image);
            tvValue = (TextView) v.findViewById(R.id.tv_value);
            tvCategory = (TextView) v.findViewById(R.id.tv_category);
            tvDate = (TextView) v.findViewById(R.id.tv_date);
        }
        public void onClick(View v){
            WalletValuesAdapter.CurrencyAsyncTask runner = new WalletValuesAdapter.CurrencyAsyncTask(v);
            runner.execute(currencyURL);
        }
    }

    public WalletValuesAdapter(List<WalletValue> objects) {
        obj = objects;
    }

    @Override
    public WalletValuesAdapter.ViewHolder onCreateViewHolder (ViewGroup parent,
                                                              int viewTipe) {
        //create new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_wallet_values, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get new item
        WalletValue walletValue = obj.get(position);

        // set value
        NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.getDefault());
        java.text.DecimalFormat df = (java.text.DecimalFormat) nf;
        df.applyPattern("##.00");
        String newValue = "R$"+df.format(walletValue.getValue());
        holder.tvValue.setText(newValue);

        //set image
        if(walletValue.getSign()) {
            holder.iv.setImageResource(R.drawable.green_plus);
        }
        else {
            holder.iv.setImageResource(R.drawable.red_minus);
        }

        //set category
        holder.tvCategory.setText(walletValue.getCategory());

        //set date
        holder.tvDate.setText(walletValue.getDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return obj.size();
    }


    /** AsyncTask class **/
    private static class CurrencyAsyncTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "AsyncTask";
        private Float brlValue = null;
        private View itemClicked;

        public CurrencyAsyncTask(View v) {
            super();
            itemClicked = v;
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            if (brlValue != null) {
                result = brlValue.toString();
            }
            else if(!isCancelled() && urls != null && urls.length > 0) {
                String strUrl = urls[0];
                try {
                    URL url = new URL(strUrl);
                    result = downloadUrl(url);
                } catch (Exception e) {
                    Log.v(TAG, e.getMessage());
                }
            }
            return result;
        }

        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpsURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                stream = connection.getInputStream();
                if (stream != null) {
                    result = parseCurrencyJSON(readStream(stream, 10000));
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        private String readStream(InputStream stream, int maxLength) throws IOException {
            String result = null;
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[maxLength];
            int numChars = 0;
            int readSize = 0;
            while (numChars < maxLength && readSize != -1) {
                numChars += readSize;
                readSize = reader.read(buffer, numChars, buffer.length - numChars);
            }
            if (numChars != -1) {
                numChars = Math.min(numChars, maxLength);
                result = new String(buffer, 0, numChars);
            }
            return result;
        }

        private String parseCurrencyJSON(String currencyJson) {
            String result = null;
            if (currencyJson != null) {
                try {
                    JSONObject jsonObject = new JSONObject(currencyJson);
                    jsonObject = jsonObject.getJSONObject("rates");
                    result = jsonObject.getString("BRL");
                } catch (JSONException e) { Log.v(TAG, e.getMessage()); }
            }
            return result;
        }

        private void changeItemValue() {
            if (brlValue == null) {
                Toast.makeText(itemClicked.getContext(), "No internet", Toast.LENGTH_SHORT).show();
                return;
            }
            TextView valueToDollar = (TextView) itemClicked.findViewById(R.id.tv_value);
            NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.getDefault());
            java.text.DecimalFormat df = (java.text.DecimalFormat) nf;
            df.applyPattern("##.00");
            float f;
            String valueToChange = valueToDollar.getText().toString();
            if (valueToChange.substring(0,1).contentEquals("R")) {
                try {
                    f = df.parse(valueToChange.substring(2)).floatValue();
                } catch (ParseException e) {
                    Log.v(TAG, "Cannot parse value");
                    return;
                }
                String newValue = "$"+df.format(f / brlValue);
                valueToDollar.setText(newValue);


            }
            else {
                try {
                    f = df.parse(valueToChange.substring(1)).floatValue();
                } catch (ParseException e) {
                    Log.v(TAG, "Cannot parse value");
                    return;
                }
                String newValue = "R$"+df.format(f * brlValue);
                valueToDollar.setText(newValue);
            }
        }

        @Override
        protected void onPostExecute(String currencyValue) {
            if (currencyValue != null) {
                brlValue = Float.parseFloat(currencyValue);
            }
            changeItemValue();
        }
    }
}
