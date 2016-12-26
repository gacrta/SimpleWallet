package com.example.carteirasimples;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class ViewValues extends AppCompatActivity {
    ListView walletListView;
    private static final String currencyURL =
            "https://openexchangerates.org/api/latest.json?app_id=101bdb4a779b4ad7b0584069b8fd323b&currencies.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_values);

        WalletValuesAdapter adapter = new WalletValuesAdapter(this, R.layout.list_wallet_values, Overview.valuesAdded);

        walletListView = (ListView) findViewById(R.id.wallet_list_view);
        walletListView.setAdapter(adapter);
        

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wallet_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_to_dollar:
                changeToDollar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeToDollar() {
        CurrencyAsyncTask runner = new CurrencyAsyncTask();
        runner.execute(currencyURL);
    }

    private class CurrencyAsyncTask extends AsyncTask<String, Void, String> {
        private static final String TAG = "AsyncTask";
        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            if(!isCancelled() && urls != null && urls.length > 0) {
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

        @Override
        protected void onPostExecute(String currencyValue) {
            //float brlCurrency = Float.parseFloat(currencyValue);
            Toast.makeText(getBaseContext(), "BRL = " + currencyValue,
            Toast.LENGTH_LONG).show();


        }
    }

}
