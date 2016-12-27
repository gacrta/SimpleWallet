package com.example.carteirasimples;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class ViewValues extends AppCompatActivity {
    ListView walletListView;
    private static final String currencyURL =
            "https://openexchangerates.org/api/latest.json?app_id=101bdb4a779b4ad7b0584069b8fd323b&currencies.json";
    private Float brlValue = null;
    private boolean[] isDollarValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_values);

        WalletValuesAdapter adapter = new WalletValuesAdapter(this, R.layout.list_wallet_values, Overview.valuesAdded);

        walletListView = (ListView) findViewById(R.id.wallet_list_view);
        walletListView.setAdapter(adapter);
        walletListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CurrencyAsyncTask runner = new CurrencyAsyncTask(view, i);
                runner.execute(currencyURL, Integer.toString(i));
            }
        });
        int N = Overview.valuesAdded.size();
        isDollarValue = new boolean[N];
        for (int i = 0; i < N; i++) {
            isDollarValue[i] = false;
        }


    }

    private class CurrencyAsyncTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "AsyncTask";
        private View itemClicked;
        private int positionOfItemClicked;

        public CurrencyAsyncTask(View v, int position) {
            super();
            itemClicked = v;
            positionOfItemClicked = position;
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
            TextView valueToDollar = (TextView) itemClicked.findViewById(R.id.tv_value);
            NumberFormat nf = NumberFormat.getNumberInstance(java.util.Locale.getDefault());
            java.text.DecimalFormat df = (java.text.DecimalFormat) nf;
            df.applyPattern("##.00");
            float f;
            if (!isDollarValue[positionOfItemClicked]) {
                try {
                    f = df.parse(valueToDollar.getText().toString().substring(2)).floatValue();
                } catch (ParseException e) {
                    Log.v(TAG, "Cannot parse value");
                    return;
                }
                String newValue = "$"+df.format(f / brlValue);
                valueToDollar.setText(newValue);
                isDollarValue[positionOfItemClicked] = true;
            }
            else {
                try {
                    f = df.parse(valueToDollar.getText().toString().substring(1)).floatValue();
                } catch (ParseException e) {
                    Log.v(TAG, "Cannot parse value");
                    return;
                }
                String newValue = "R$"+df.format(f * brlValue);
                valueToDollar.setText(newValue);
                isDollarValue[positionOfItemClicked] = false;
            }
        }

        @Override
        protected void onPostExecute(String currencyValue) {
            brlValue = Float.parseFloat(currencyValue);
            changeItemValue();
        }
    }

}
