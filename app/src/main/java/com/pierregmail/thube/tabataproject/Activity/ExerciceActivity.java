package com.pierregmail.thube.tabataproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;

public class ExerciceActivity extends AppCompatActivity {

    private int tpsExercice;
    private TextView exerciceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);
        tpsExercice = getIntent().getIntExtra(MainActivity.TPS_EXERCICE,0);
        exerciceView = (TextView) findViewById(R.id.tpsExercice);
        miseAJour();
    }

    private void miseAJour(){
        exerciceView.setText(Integer.toString(tpsExercice));
    }

    public void changeValueExercice(View view){
        if (tpsExercice >= 0) {
            if (view.getId() == R.id.buttonPlus) {
                tpsExercice++;
            } else if (view.getId() == R.id.buttonMoins) {
                tpsExercice--;
            }
            miseAJour();
        } else if (tpsExercice == 1) {
            if (view.getId() == R.id.buttonPlus) {
                tpsExercice++;
                miseAJour();
            }
        }
    }

    public void onClickOkExercice(View view){
        getIntent().putExtra(MainActivity.TPS_EXERCICE, tpsExercice);
        setResult(RESULT_OK,getIntent());
        finish();
    }
}
