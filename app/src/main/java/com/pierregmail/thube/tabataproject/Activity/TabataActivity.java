package com.pierregmail.thube.tabataproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;

public class TabataActivity extends AppCompatActivity {

    private int nbTabata;
    private TextView tabataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabata);
        nbTabata = getIntent().getIntExtra(MainActivity.NB_TABATA,0);
        tabataView = (TextView) findViewById(R.id.nbTabata);
        miseAJour();
    }

    private void miseAJour(){
        tabataView.setText(Integer.toString(nbTabata));
    }

    public void changeValueTabata(View view){
        if (nbTabata >= 0) {
            if (view.getId() == R.id.buttonPlus) {
                nbTabata++;
            } else if (view.getId() == R.id.buttonMoins) {
                nbTabata--;
            }
            miseAJour();
        } else if (nbTabata == 1) {
            if (view.getId() == R.id.buttonPlus) {
                nbTabata++;
                miseAJour();
            }
        }
    }

    public void onClickOkTabata(View view){
        getIntent().putExtra(MainActivity.NB_TABATA, nbTabata);
        setResult(RESULT_OK,getIntent());
        finish();
    }
}
