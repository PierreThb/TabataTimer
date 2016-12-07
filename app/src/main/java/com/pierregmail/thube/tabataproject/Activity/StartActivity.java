package com.pierregmail.thube.tabataproject.Activity;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;
import com.pierregmail.thube.tabataproject.data.TabataConfigurations;

public class StartActivity extends AppCompatActivity {
    /* View */
    private Button startButton;
    private Button pauseButton;
    private TextView timerValue;
    private TextView timerEtat;
    private TextView nombreCycle;
    private TextView nombreTabata;

    /* Data */
    private long updatedTime;
    private CountDownTimer timer;
    private String currentState; // état du timer, valeur "inStart" ou "inPause"
    private String currentTimer;//timer actuel, valeur "PreparationT","ExerciceT","ReposT" ou "PauseT"(qd currentState="Pause")
    private String savedTimer;//
    private int tpsrepos;
    private int tpsexercice;
    private int tpspreparation;
    private int nbCyclesRestant;
    private int nbTabatasRestant;
    private TabataConfigurations tabataConfigurations;
    private String etatAvantPause;
    private int NombreCyclesPourChaqueTab;

    /* Data for the SaveInstanceState */
    static final String TEMPS_PREPARTION = "tempsPreparation";
    static final String TEMPS_REPOS = "tempsRepos";
    static final String TEMPS_EXERCICE = "tempsExercice";
    static final String NOMBRE_CYCLE = "nombreCycle";
    static final String NOMBRE_TABATA = "nombreTabata";
    static final String NOMBRE_CYCLE_POUR_CHAQUE_TAB = "nombreCyclePourChaqueTab";

    static final String TIMER_ETAT = "timerEtat";
    static final String TIMER_VALUE = "timerValue";

    static final String CURRENT_STATE = "currentState";
    static final String CURRENT_TIMER = "currentTimer";

    static final String SAVED_TIMER = "savedTimer";
    static final String ETAT_AVANT_PAUSE = "etatAvantPause";
    static final String ISRUN = "isRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        tpsrepos = getIntent().getIntExtra(MainActivity.TPS_REPOS, 0);
        tpspreparation = getIntent().getIntExtra(MainActivity.TPS_PREPARATION, 0);
        tpsexercice = getIntent().getIntExtra(MainActivity.TPS_EXERCICE, 0);
        nbCyclesRestant = getIntent().getIntExtra(MainActivity.NB_CYCLES, 0);
        NombreCyclesPourChaqueTab = new Integer(nbCyclesRestant);
        nbTabatasRestant = getIntent().getIntExtra(MainActivity.NB_TABATA, 0);

        timerValue = (TextView) findViewById(R.id.timerValue);
        timerEtat = (TextView) findViewById(R.id.textView_Etat);
        nombreCycle = (TextView) findViewById(R.id.textView_nombreCycle);
        nombreTabata = (TextView) findViewById(R.id.textView_nombreTabata);
        startButton = (Button) findViewById(R.id.startButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);

        currentState = "inPause";//avant que le timer commence, l'état est "pause"
        currentTimer = "ReadyT";//au moment du lancement de l'activity, avant le premier click sur start

        nombreCycle.setText("Cycles: " + nbCyclesRestant);
        nombreTabata.setText("Tabs: " + nbTabatasRestant);

        parameters();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (currentTimer.equals("Ready to launch!") || currentTimer.equals("Paused!")){
            savedInstanceState.putBoolean(ISRUN,false);
        }
        else {
            savedInstanceState.putBoolean(ISRUN,true);
            onPauseClick(pauseButton);
        }
        // Save the user's current game state
        savedInstanceState.putInt(TEMPS_PREPARTION, tpspreparation);
        savedInstanceState.putInt(TEMPS_REPOS, tpsrepos);
        savedInstanceState.putInt(TEMPS_EXERCICE,tpsexercice);
        savedInstanceState.putInt(NOMBRE_CYCLE, nbCyclesRestant);
        savedInstanceState.putInt(NOMBRE_TABATA, nbTabatasRestant);
        savedInstanceState.putInt(NOMBRE_CYCLE_POUR_CHAQUE_TAB, NombreCyclesPourChaqueTab);

        savedInstanceState.putString(TIMER_ETAT, String.valueOf(timerEtat.getText()));
        savedInstanceState.putString(TIMER_VALUE, String.valueOf(timerValue.getText()));

        savedInstanceState.putString(CURRENT_STATE, currentState);
        savedInstanceState.putString(CURRENT_TIMER, currentTimer);

        savedInstanceState.putString(SAVED_TIMER,savedTimer);
        savedInstanceState.putString(ETAT_AVANT_PAUSE,etatAvantPause);

        savedInstanceState.putLong("updatedTime", updatedTime);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore value of members from saved state
        tpspreparation = savedInstanceState.getInt(TEMPS_PREPARTION);
        tpsrepos = savedInstanceState.getInt(TEMPS_REPOS);
        tpsexercice = savedInstanceState.getInt(TEMPS_EXERCICE);
        nbCyclesRestant = savedInstanceState.getInt(NOMBRE_CYCLE);
        nbTabatasRestant = savedInstanceState.getInt(NOMBRE_TABATA);
        NombreCyclesPourChaqueTab = savedInstanceState.getInt(NOMBRE_CYCLE_POUR_CHAQUE_TAB);
        tabataConfigurations = new TabataConfigurations(tpspreparation, tpsexercice, tpsrepos, nbCyclesRestant, nbTabatasRestant);

        currentState = savedInstanceState.getString(CURRENT_STATE);
        currentTimer = savedInstanceState.getString(CURRENT_TIMER);

        timerEtat.setText(savedInstanceState.getString(TIMER_ETAT));
        timerValue.setText(savedInstanceState.getString(TIMER_VALUE));

        savedTimer = savedInstanceState.getString(SAVED_TIMER);
        etatAvantPause = savedInstanceState.getString(ETAT_AVANT_PAUSE);

        boolean isRun = savedInstanceState.getBoolean(ISRUN);

        updatedTime = savedInstanceState.getLong("updatedTime");

        nombreCycle.setText("Cycles: " + nbCyclesRestant);
        nombreTabata.setText("Tabs: " + nbTabatasRestant);

        parameters();
        if(isRun){
            onStartClick(startButton);
        }
        miseAJour();
    }

    public void launch() {
        /* Set la variable updatedTime en fonction du timer */
        if (currentTimer.equals("PreparationT")) {
            updatedTime = tpspreparation * 1000;
        } else if (currentTimer.equals("ExerciceT")) {
            updatedTime = tpsexercice * 1000;
        } else if (currentTimer.equals("ReposT")) {
            updatedTime = tpsrepos * 1000;
        } else if (currentTimer.equals("PauseT")) {
            currentTimer = savedTimer;
        }

        timer = new CountDownTimer(updatedTime, 10) {

            public void onTick(long millisUntilFinished) {
                updatedTime = millisUntilFinished;
                miseAJour();
            }

            public void onFinish() {
                updatedTime = 0;
                if (currentTimer.equals("PreparationT")) {
                    currentTimer = "ExerciceT";
                    timerEtat.setText("Exercice");
                    launch();
                } else if (currentTimer.equals("ExerciceT")) {
                    currentTimer = "ReposT";
                    timerEtat.setText("Repos");
                    launch();
                } else if ((currentTimer.equals("ReposT") && tabataConfigurations.getCycles() > 1) || tabataConfigurations.getTabatas() > 0) { // si repos et nbcycle > 0  OU   nbTabatasRestant > 0
                    if (currentTimer.equals("ReposT") && tabataConfigurations.getCycles() > 1) { //premier cas, il reste au moins un cycle
                        nbCyclesRestant = tabataConfigurations.getCycles() - 1;
                        tabataConfigurations.setCycles(nbCyclesRestant);
                        currentTimer = "ExerciceT";
                        timerEtat.setText("Exercice");
                        nombreCycle.setText("Cycles: " + nbCyclesRestant);
                        launch();
                    } else if (tabataConfigurations.getTabatas() > 1) { //premier second cas, plus de cycle mais encore au moins DEUX tabatas restant
                        nbTabatasRestant = tabataConfigurations.getTabatas() - 1;
                        tabataConfigurations.setTabatas(nbTabatasRestant);
                        tabataConfigurations.setCycles(NombreCyclesPourChaqueTab);
                        nbCyclesRestant = tabataConfigurations.getCycles() - 1;
                        currentTimer = "PreparationT";
                        timerEtat.setText("Preparation");
                        nombreTabata.setText("Tabs: " + nbTabatasRestant);
                        nombreCycle.setText("Cycles: " + NombreCyclesPourChaqueTab);
                        launch();
                    } else if (tabataConfigurations.getTabatas() == 1) { //deuxième second cas, plus de cycle et fin du dernier tabata, donc fin du timer
                        currentTimer = "ReadyT";
                        timerEtat.setText("Fin");
                    }
                    miseAJour();
                }
            }
        }.start();
    }

    public void onPauseClick(View view) {
        if (timer != null && currentState.equals("inStart")) { //si le timer n'est pas finis et pas déjà en pause
            timer.cancel();
            savedTimer = new String(currentTimer);
            currentTimer = "PauseT";
            etatAvantPause = new String("" + timerEtat.getText());
            timerEtat.setText("Pause");
            miseAJour();
        }
    }

    public void onStartClick(View view) {
        if (currentTimer.equals("ReadyT")) { //si début, au moment au l'activity est lancé
            tabataConfigurations = new TabataConfigurations(tpspreparation, tpsexercice, tpsrepos, nbCyclesRestant, nbTabatasRestant);
            currentTimer = "PreparationT"; //premier timer, celui pour le tps de preparation
            currentState = "inStart";
            timerEtat.setText("Preparation");
            timerEtat.setTextColor(Color.YELLOW);
            launch(); // lance le timer
        } else if (currentTimer.equals("PauseT")) { //si le bouton pause avait été cliqué auparavant
            currentState = "inStart";
            timerEtat.setText(etatAvantPause);
            launch();
        }
    }

    // Mise à jour graphique
    private void miseAJour() {
        // Décompositions en secondes et minutes
        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (updatedTime % 1000);

        // Affichage du "timer"
        timerValue.setText("" + mins + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds));
    }

    private void parameters() {
        pauseButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (pauseButton.getText().equals("Pause")) {
                        pauseButton.setText("Reprendre");
                    } else if (pauseButton.getText().equals("Reprendre")) {
                        pauseButton.setText("Pause");
                    }
                }
                return false;
            }
        });
    }
}
