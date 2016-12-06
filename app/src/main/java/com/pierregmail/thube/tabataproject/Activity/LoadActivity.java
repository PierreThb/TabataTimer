package com.pierregmail.thube.tabataproject.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;
import com.pierregmail.thube.tabataproject.data.TabataConfigurations;
import com.pierregmail.thube.tabataproject.data.TabataConfigurationsDAO;

import java.util.List;

public class LoadActivity extends AppCompatActivity {

    private ListView listView;
    TabataConfigurations configurationsCliked;
    TabataConfigurations previousConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        //Récupère la liste de toutes les configs
        final List<TabataConfigurations> configurationsList = TabataConfigurationsDAO.selectAll();

        listView = (ListView) findViewById(R.id.listView);

        final ArrayAdapter<TabataConfigurations> adapter = new ArrayAdapter<TabataConfigurations>(this, R.layout.activity_load_rows, configurationsList) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // Inflate LAYOUT
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = (ViewGroup) inflater.inflate(R.layout.activity_load_rows, null);
                }

                // VIEW
                TextView text_nom = (TextView) convertView.findViewById(R.id.text_nom);
                TextView text_préparation = (TextView) convertView.findViewById(R.id.text_prep);
                TextView text_repos = (TextView) convertView.findViewById(R.id.text_repos);
                TextView text_cycle = (TextView) convertView.findViewById(R.id.text_cycle);
                TextView text_tabs = (TextView) convertView.findViewById(R.id.text_tabs);
                TextView text_ex = (TextView) convertView.findViewById(R.id.text_ex);

                // Charger la vue avec les données
                TabataConfigurations configurations = configurationsList.get(position);
                text_nom.setText("" + configurations.getNom());
                text_préparation.setText("" + configurations.getPreparation());
                text_repos.setText("" + configurations.getRepos());
                text_ex.setText("" + configurations.getExercice());
                text_cycle.setText("" + configurations.getCycles());
                text_tabs.setText("" + configurations.getTabatas());

                return convertView;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Configurations ", "" + position);
                configurationsCliked = adapter.getItem(position);
                /*Log.e("Configuration clicked",""+configurationsCliked.toString());
                Log.e("Configuration ID",""+configurationsCliked.getId());*/
                getIntent().putExtra(MainActivity.ACTUAL_CONFIGURATION_ID,configurationsCliked.getId());
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
    }
}
