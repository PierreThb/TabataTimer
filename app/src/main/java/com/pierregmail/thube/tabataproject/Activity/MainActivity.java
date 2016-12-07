package com.pierregmail.thube.tabataproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pierregmail.thube.tabataproject.R;
import com.pierregmail.thube.tabataproject.data.TabataConfigurations;

public class MainActivity extends AppCompatActivity {
    public final static String NB_CYCLES = "nbcyles";
    public final static String TPS_PREPARATION = "tpspreparation";
    public final static String TPS_EXERCICE = "tpsexercice";
    public final static String TPS_REPOS = "tpsrepos";
    public final static String NB_TABATA = "nbtabata";
    public final static String ACTUAL_CONFIGURATION_ID = "actualconfigurationid";

    public final static int CYCLE_ACTIVITY_REQUEST = 1;
    public final static int EXERCICE_ACTIVITY_REQUEST = 2;
    public final static int PREPARATION_ACTIVITY_REQUEST = 3;
    public final static int REPOS_ACTIVITY_REQUEST = 4;
    public final static int TABATA_ACTIVITY_REQUEST = 5;
    public final static int LOAD_ACTIVITY_REQUEST = 6;

    /* Création d'un objet de type TabataConfigurations avec des valeurs par défault */
    TabataConfigurations configuration = new TabataConfigurations(10, 30, 10, 3, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MiseAJour();
    }

    private void MiseAJour() {
        /* Récupère les bouttons pour les mettre à jour */
        Button button_prep = (Button) findViewById(R.id.boutton_préparation);
        Button button_ex = (Button) findViewById(R.id.boutton_exercice);
        Button button_repos = (Button) findViewById(R.id.bouton_repos);
        Button button_cycle = (Button) findViewById(R.id.bouton_cycle);
        Button button_tab = (Button) findViewById(R.id.bouton_tabata);

        /* update les bouttons */
        button_prep.setText("Préparation : "+configuration.getPreparation()+" secondes");
        button_ex.setText("Exercice : "+configuration.getExercice() +" secondes");
        button_repos.setText("Repos : "+configuration.getRepos()+" secondes");
        button_cycle.setText("Nombre de cycle : "+configuration.getCycles());
        button_tab.setText("Nombre de tabatas : "+configuration.getTabatas());
    }

    /* Fonction qui lance l'activité CycleActivity */
    public void onCycleClick(View view) {
        Intent CycleActivityViewIntent = new Intent(MainActivity.this, CycleActivity.class);
        CycleActivityViewIntent.putExtra(NB_CYCLES, configuration.getCycles());
        startActivityForResult(CycleActivityViewIntent, CYCLE_ACTIVITY_REQUEST);
    }

    /* Fonction qui lance l'activité PreparationActivity */
    public void onPreparationClick(View view) {
        Intent PreparationActivityViewIntent = new Intent(MainActivity.this, PreparationActivity.class);
        PreparationActivityViewIntent.putExtra(TPS_PREPARATION, configuration.getPreparation());
        startActivityForResult(PreparationActivityViewIntent, PREPARATION_ACTIVITY_REQUEST);
    }

    /* Fonction qui lance l'activité ExerciceActivity */
    public void onExerciceClick(View view) {
        Intent ExerciceActivityViewIntent = new Intent(MainActivity.this, ExerciceActivity.class);
        ExerciceActivityViewIntent.putExtra(TPS_EXERCICE, configuration.getExercice());
        startActivityForResult(ExerciceActivityViewIntent, EXERCICE_ACTIVITY_REQUEST);
    }

    /* Fonction qui lance l'activité ReposActivity */
    public void onReposClick(View view) {
        Intent ReposActivityViewIntent = new Intent(MainActivity.this, ReposActivity.class);
        ReposActivityViewIntent.putExtra(TPS_REPOS, configuration.getRepos());
        startActivityForResult(ReposActivityViewIntent, REPOS_ACTIVITY_REQUEST);
    }

    /* Fonction qui lance l'activité TabataActivity */
    public void onTabataClick(View view) {
        Intent TabataActivityViewIntent = new Intent(MainActivity.this, TabataActivity.class);
        TabataActivityViewIntent.putExtra(NB_TABATA, configuration.getTabatas());
        startActivityForResult(TabataActivityViewIntent, TABATA_ACTIVITY_REQUEST);
    }

    /* Fonction pour enregistrer les configurationsCliked dans la base de données */
    public void onEnregistrer(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_save,null))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextView dialog_input = (TextView) ((AlertDialog) dialog).findViewById(R.id.dialog_input);
                        configuration.setNom(dialog_input.getText().toString());
                        Log.e("Nom",configuration.toString());
                        configuration.save();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /* Fonction pour charger une configuration enregistrée dans la base */
    public void onCharger(View view) {
        Intent LoadActivityViewIntent = new Intent(MainActivity.this, LoadActivity.class);
        LoadActivityViewIntent.putExtra(ACTUAL_CONFIGURATION_ID, configuration.getId());
        startActivityForResult(LoadActivityViewIntent, LOAD_ACTIVITY_REQUEST);
    }

    /* Fonction qui lance l'activité StartActivity */
    public void onStartClick(View view) {
        Intent StartActivityViewIntent = new Intent(MainActivity.this, StartActivity.class);
        StartActivityViewIntent.putExtra(NB_CYCLES, configuration.getCycles());
        StartActivityViewIntent.putExtra(TPS_PREPARATION, configuration.getPreparation());
        StartActivityViewIntent.putExtra(TPS_EXERCICE, configuration.getExercice());
        StartActivityViewIntent.putExtra(TPS_REPOS, configuration.getRepos());
        StartActivityViewIntent.putExtra(NB_TABATA, configuration.getTabatas());
        startActivity(StartActivityViewIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Vérification du retour à l'aide du code requête et du code de résultat
        if (requestCode == CYCLE_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            // Afficher une notification
            String notification = "Retour CycleActivity";
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
            configuration.setCycles(data.getIntExtra(NB_CYCLES, 0));
            MiseAJour();

        } else if (requestCode == PREPARATION_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            String notification = "Retour PreparationActivity";
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
            configuration.setPreparation(data.getIntExtra(TPS_PREPARATION, 0));
            MiseAJour();
        } else if (requestCode == EXERCICE_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            String notification = "Retour ExerciceActivity";
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
            configuration.setExercice(data.getIntExtra(TPS_EXERCICE, 0));
            MiseAJour();
        } else if (requestCode == REPOS_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            String notification = "Retour ReposActivity";
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
            configuration.setRepos(data.getIntExtra(TPS_REPOS, 0));
            MiseAJour();
        } else if (requestCode == TABATA_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            String notification = "Retour TabataActivity";
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
            configuration.setTabatas(data.getIntExtra(NB_TABATA, 0));
            MiseAJour();
        } else if (requestCode == LOAD_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            String notification = "Retour LoadActivity";
            Toast.makeText(this, notification, Toast.LENGTH_SHORT).show();
            TabataConfigurations newConfiguration = TabataConfigurations.findById(TabataConfigurations.class, data.getLongExtra(ACTUAL_CONFIGURATION_ID, 0));
            configuration = new TabataConfigurations(newConfiguration.getPreparation(),newConfiguration.getExercice(),newConfiguration.getRepos(),newConfiguration.getCycles(),newConfiguration.getTabatas());
            configuration.setNom(newConfiguration.getNom());
            MiseAJour();
        }
    }
}
