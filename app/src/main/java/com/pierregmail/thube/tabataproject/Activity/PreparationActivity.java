package com.pierregmail.thube.tabataproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;

public class PreparationActivity extends AppCompatActivity {

    private int tempsPreparation; //pour le temps de préparation
    private TextView preparationView; // pour l'affichage du temps de préparation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation);
        tempsPreparation = getIntent().getIntExtra(MainActivity.TPS_PREPARATION, 0);
        preparationView = (TextView) findViewById(R.id.tpsPreparation);
        miseAJour();
    }

    private void miseAJour() {
        preparationView.setText(Integer.toString(tempsPreparation));
    }

    public void changeValuePreparation(View view) {
        if (tempsPreparation >= 0) {
            if (view.getId() == R.id.buttonPlus) {
                tempsPreparation++;
            } else if (view.getId() == R.id.buttonMoins) {
                tempsPreparation--;
            }
            miseAJour();
        } else if (tempsPreparation == 1) {
            if (view.getId() == R.id.buttonPlus) {
                tempsPreparation++;
                miseAJour();
            }
        }
    }

    public void onClickOkPreparation(View view) {
        getIntent().putExtra(MainActivity.TPS_PREPARATION, tempsPreparation);
        setResult(RESULT_OK, getIntent());
        finish();
    }

}
