package com.pierregmail.thube.tabataproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;

/**
 * Activité pour le nombre de cycle
 */
public class CycleActivity extends AppCompatActivity {

    private int nbCycle; //pour le nombre de cycle
    private TextView cycleView; //pour l'afffichage du nombre de cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);
        nbCycle = getIntent().getIntExtra(MainActivity.NB_CYCLES, 0);
        cycleView = (TextView) findViewById(R.id.tpsRepos);
        miseAJour();
    }

    private void miseAJour() {
        cycleView.setText(Integer.toString(nbCycle));
    }

    public void changeValueCycle(View view) {
        if (nbCycle >= 0) {
            if (view.getId() == R.id.buttonPlus) {
                nbCycle++;
            } else if (view.getId() == R.id.buttonMoins) {
                nbCycle--;
            }
            miseAJour();
        } else if (nbCycle == 1) {
            if (view.getId() == R.id.buttonPlus) {
                nbCycle++;
                miseAJour();
            }
        }
    }

    public void onClickOkCycle(View view) {
        getIntent().putExtra(MainActivity.NB_CYCLES, nbCycle);
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
