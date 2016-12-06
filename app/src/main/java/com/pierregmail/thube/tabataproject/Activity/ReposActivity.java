package com.pierregmail.thube.tabataproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pierregmail.thube.tabataproject.R;

public class ReposActivity extends AppCompatActivity {

    private int tpsRepos;
    private TextView reposView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        tpsRepos = getIntent().getIntExtra(MainActivity.TPS_REPOS,0);
        reposView = (TextView) findViewById(R.id.tpsRepos);
        miseAJour();
    }

    private void miseAJour(){
        reposView.setText(Integer.toString(tpsRepos));
    }

    public void changeValueRepos(View view){
        if (tpsRepos >= 0) {
            if (view.getId() == R.id.buttonPlus) {
                tpsRepos++;
            } else if (view.getId() == R.id.buttonMoins) {
                tpsRepos--;
            }
            miseAJour();
        } else if (tpsRepos == 1) {
            if (view.getId() == R.id.buttonPlus) {
                tpsRepos++;
                miseAJour();
            }
        }
    }

    public void onClickOkRepos(View view){
        getIntent().putExtra(MainActivity.TPS_REPOS,tpsRepos);
        setResult(RESULT_OK,getIntent());
        finish();
    }
}
