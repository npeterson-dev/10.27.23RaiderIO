package CSS3334.raiderio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // declare elements from layout
    private EditText editTextToon, editTextToonServer;
    private Button buttonSearch;
    private TextView textViewName, textViewServer, textViewGuild, textViewRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link variables to elements of layout.
        editTextToon = findViewById(R.id.editTextToon);
        editTextToonServer = findViewById(R.id.editTextToonServer);
        buttonSearch = findViewById(R.id.buttonSearch);
        textViewName = findViewById(R.id.textViewName);
        textViewServer = findViewById(R.id.textViewServer);
        textViewGuild = findViewById(R.id.textViewGuild);
        textViewRating = findViewById(R.id.textViewRating);


        // onclicklistener for the search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String characterName = editTextToon.getText().toString();
                String serverName = editTextToonServer.getText().toString();
            if (!characterName.isEmpty() && !serverName.isEmpty()) {
                performNetworkRequest(characterName, serverName);
                } else {
                Toast.makeText(MainActivity.this, "Both character name and server are required.", Toast.LENGTH_SHORT).show();
            }
            }

        });
    }

    // parse the data from RaiderIO
    private void performNetworkRequest(String characterName, String serverName) {
        new Thread(() -> {
            try {
                URL url = new URL("https://raider.io/characters/us/area-52/Kv%C3%BD");
                //URL url = new URL("https://raider.io/api/v1/characters/profile?region=us&realm=" + serverName + "&name=" + characterName + "&fields=mythic_plus_ranks");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    Log.d("character request", line);
                }
                reader.close();

                processResponse(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // set parsed data to display in app.
    private void processResponse(String response) {
        try {
            //objects to pull out of JSON
            JSONObject jsonObject = new JSONObject(response);
            String name = jsonObject.getString("name");
            String server = jsonObject.getString("realm");
            JSONObject guildObject = jsonObject.getJSONObject("guild");
            String guildName = guildObject.getString("name");

            //connect to UI elements
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textViewName.setText(name);
                    textViewServer.setText(server);
                    textViewGuild.setText(guildName);
                }
            });

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
