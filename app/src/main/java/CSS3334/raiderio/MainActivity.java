package CSS3334.raiderio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // declare elements from layout
    private EditText editTextToon, editTextToonServer;
    private Button buttonSearch, buttonClear;
    private TextView textViewName, textViewServer, textViewGuild, textViewSpec, textViewRating;

    // mythic_plus_ranks Overall: World Rank (textViewRaceWorld), Region Rank (textViewRaceRegion), Realm Rank (textViewRaceRealm)
    private TextView textViewRaceWorld, textViewRaceRegion, textViewRaceRealm;

    // mythic_plus_ranks Class DPS: World Rank (textViewDpsWorld), Region Rank (textViewDpsRegion), Realm Rank (textViewDpsRealm)
    private TextView textViewDpsWorld, textViewDpsRegion, textViewDpsRealm;

    // mythic_plus_ranks Class Healer: World Rank (textViewHealWorld), Region Rank (textViewHealRegion), Realm Rank (textViewHealRealm)
    private TextView textViewHealWorld, textViewHealRegion, textViewHealRealm;


    // initialize viewModel
    private CharacterViewModel characterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link variables to elements of layout.
        editTextToon = findViewById(R.id.editTextToon);
        editTextToonServer = findViewById(R.id.editTextToonServer);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonClear = findViewById(R.id.buttonClear);
        textViewName = findViewById(R.id.textViewName);
        textViewServer = findViewById(R.id.textViewServer);
        textViewGuild = findViewById(R.id.textViewGuild);
        textViewSpec = findViewById(R.id.textViewSpec);
        textViewRating = findViewById(R.id.textViewRating);


        // mythic_plus_ranks by race
        textViewRaceWorld = findViewById(R.id.textViewRaceWorld);
        textViewRaceRegion = findViewById(R.id.textViewRaceRegion);
        textViewRaceRealm = findViewById(R.id.textViewRaceRealm);

        // mythic_plus_ranks by dps
        textViewDpsWorld = findViewById(R.id.textViewDpsWorld);
        textViewDpsRegion = findViewById(R.id.textViewDpsRegion);
        textViewDpsRealm = findViewById(R.id.textViewDpsRealm);

        // mythic_plus_ranks by healer
        textViewHealWorld = findViewById(R.id.textViewHealWorld);
        textViewHealRegion = findViewById(R.id.textViewHealRegion);
        textViewHealRealm = findViewById(R.id.textViewHealRealm);

        // initialize viewModel
        characterViewModel = new ViewModelProvider(this).get(CharacterViewModel.class);


        // onclicklistener for the search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String characterName = editTextToon.getText().toString();
                String serverName = editTextToonServer.getText().toString();

                //hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);

            if (!characterName.isEmpty() && !serverName.isEmpty()) {
                performNetworkRequest(characterName, serverName);
                } else {
                Toast.makeText(MainActivity.this, "Both character name and server are required.", Toast.LENGTH_SHORT).show();
                }
                Log.d("button click", serverName);
            }

        });

        //on click listener for clear button
        buttonClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                clearEditTextFields();
            }
        });
    }

    private void clearEditTextFields() {
        editTextToon.setText("");
        editTextToonServer.setText("");
    }

    // parse the data from RaiderIO
    private void performNetworkRequest(String characterName, String serverName) {
        new Thread(() -> {
            try {
                URL url = new URL("https://raider.io/api/v1/characters/profile?region=us&realm=" + serverName + "&name=" + characterName + "&fields=mythic_plus_scores_by_season%3Acurrent%2Cguild%2Cmythic_plus_ranks");
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
            String spec = jsonObject.getString("active_spec_name");

            if (jsonObject.has("guild") && !jsonObject.isNull("guild")) {
                JSONObject guildObject = jsonObject.getJSONObject("guild");
                String guildName = guildObject.getString("name");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewName.setText(name);
                        textViewServer.setText(server);
                        textViewGuild.setText(guildName);
                        textViewSpec.setText(spec);
                    }

                });
            } else
            {
                // in the event there isn't any guild information
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewName.setText(name);
                        textViewServer.setText(server);
                        textViewGuild.setText("No Guild Listed");
                        textViewSpec.setText(spec);
                    }
                });
            };

            // extract mythic_plus_ranks data
            JSONObject mythicPlusRanks = jsonObject.getJSONObject("mythic_plus_ranks");

            // overall ranks
            JSONObject overall = mythicPlusRanks.getJSONObject("overall");
            int worldRank = overall.getInt("world");
            int regionRank = overall.getInt("region");
            int realmRank = overall.getInt("realm");

            // Class dps ranks
            JSONObject classDPS = mythicPlusRanks.getJSONObject("class_dps");
            int dpsWorldRank = classDPS.getInt("world");
            int dpsRegionRank = classDPS.getInt("region");
            int dpsRealmRank = classDPS.getInt("realm");

            // Class healer ranks
            JSONObject classHealer = mythicPlusRanks.getJSONObject("class_healer");
            int healWorldRank = classHealer.getInt("world");
            int healRegionRank = classHealer.getInt("region");
            int healRealmRank = classHealer.getInt("realm");

            // extract the mythic plus score
            double mythicPlusScore = jsonObject.getJSONArray("mythic_plus_scores_by_season")
                            .getJSONObject(0)
                                    .getJSONObject("scores")
                                            .getDouble("all");
            // set extracted score to textViewRating
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    textViewRating.setText(String.valueOf(mythicPlusScore));
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // update overall ranks
                    textViewRaceWorld.setText(String.valueOf(worldRank));
                    textViewRaceRegion.setText(String.valueOf(regionRank));
                    textViewRaceRealm.setText(String.valueOf(realmRank));

                    // update class DPS ranks
                    textViewDpsWorld.setText(String.valueOf(dpsWorldRank));
                    textViewDpsRegion.setText(String.valueOf(dpsRegionRank));
                    textViewDpsRealm.setText(String.valueOf(dpsRealmRank));

                    // update class healer ranks
                    textViewHealWorld.setText(String.valueOf(healWorldRank));
                    textViewHealRegion.setText(String.valueOf(healRegionRank));
                    textViewHealRealm.setText(String.valueOf(healRealmRank));
                }
            });




            } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
