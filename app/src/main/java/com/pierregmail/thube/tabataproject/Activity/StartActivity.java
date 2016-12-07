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

        nombreCycle.setText("Cycles: "+nbCyclesRestant);
        nombreTabata.setText("Tabs: "+nbTabatasRestant);

        parameters();
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
                    timerEtat.setTextColor(Color.GREEN);
                    launch();
                }else if (currentTimer.equals("ExerciceT")){
                    currentTimer = "ReposT";
                    timerEtat.setText("Repos");
                    timerEtat.setTextColor(Color.RED);
                    launch();
                }else if((currentTimer.equals("ReposT") && tabataConfigurations.getCycles() > 1 ) || tabataConfigurations.getTabatas() > 0){ // si repos et nbcycle > 0  OU   nbTabatasRestant > 0
                    if(currentTimer.equals("ReposT") && tabataConfigurations.getCycles() > 1){ //premier cas, il reste au moins un cycle
                        nbCyclesRestant = tabataConfigurations.getCycles()-1;
                        tabataConfigurations.setCycles(nbCyclesRestant);
                        currentTimer = "ExerciceT";
                        timerEtat.setText("Exercice");
                        timerEtat.setTextColor(Color.GREEN);
                        nombreCycle.setText("Cycles: "+nbCyclesRestant);
                        launch();
                    }else if(tabataConfigurations.getTabatas() > 1){ //premier second cas, plus de cycle mais encore au moins DEUX tabatas restant
                        nbTabatasRestant = tabataConfigurations.getTabatas()-1;
                        tabataConfigurations.setTabatas(nbTabatasRestant);
                        tabataConfigurations.setCycles(NombreCyclesPourChaqueTab);
                        nbCyclesRestant = tabataConfigurations.getCycles()-1;
                        currentTimer = "PreparationT";
                        timerEtat.setText("Preparation");
                        timerEtat.setTextColor(Color.YELLOW);
                        nombreTabata.setText("Tabs: "+nbTabatasRestant);
                        nombreCycle.setText("Cycles: "+NombreCyclesPourChaqueTab);
                        launch();
                    }else if(tabataConfigurations.getTabatas() == 1){ //deuxième second cas, plus de cycle et fin du dernier tabata, donc fin du timer
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
            etatAvantPause = new String(""+timerEtat.getText());
            timerEtat.setText("Pause");
            miseAJour();
        }
    }

    public void onStartClick(View view) {
        if (currentTimer.equals("ReadyT") || currentState.equals("inPause")) { //si début, au moment au l'activity est lancé
            tabataConfigurations = new TabataConfigurations(tpspreparation, tpsexercice, tpsrepos, nbCyclesRestant, nbTabatasRestant);
            currentTimer = "PreparationT"; //premier timer, celui pour le tps de preparation
            currentState = "inStart";
            timerEtat.setText("Preparation");
            timerEtat.setTextColor(Color.YELLOW);
            Log.e("onStartClick","lance premier cycle");
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

    private void parameters(){
        startButton.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    startButton.setTextColor(Color.RED);
                    pauseButton.setTextColor(Color.GREEN);
                }
                return false;
            }
        });

        pauseButton.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    startButton.setTextColor(Color.GREEN);
                    pauseButton.setTextColor(Color.RED);
                    if(pauseButton.getText().equals("Pause")){
                        pauseButton.setText("Reprendre");
                    }else if (pauseButton.getText().equals("Reprendre")){
                        pauseButton.setText("Pause");
                    }
                }
                return false;
            }
        });
    }
}
