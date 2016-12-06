package com.pierregmail.thube.tabataproject.data;

import com.orm.SugarRecord;

/**
 * Created by Pierre on 31/10/2016.
 */

/**
 * Class qui permet d'enregister des données utiles pour l'application
 */
public class TabataConfigurations extends SugarRecord {
    private int preparation;
    private int exercice;
    private int repos;
    private int cycles;
    private int tabatas;
    private String nom;

    /* SUGARORM : OBLIGATOIRE */
    public TabataConfigurations() {
    }

    public TabataConfigurations(int preparation, int travail, int repos, int cycles, int tabatas) {
        this.preparation = preparation; //Pour le temps de préparation
        this.exercice = travail; //pour le temps de exercice
        this.repos = repos; //pour le temps de repos
        this.cycles = cycles; //pour le nombre de cycle
        this.tabatas = tabatas; //pour le nombre de tabatas
    }

    public int getPreparation() {
        return preparation;
    }

    public void setPreparation(int preparation) {
        this.preparation = preparation;
    }

    public int getExercice() {
        return exercice;
    }

    public void setExercice(int exercice) {
        this.exercice = exercice;
    }

    public int getRepos() {
        return repos;
    }

    public void setRepos(int repos) {
        this.repos = repos;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public int getTabatas() {
        return tabatas;
    }

    public void setTabatas(int tabatas) {
        this.tabatas = tabatas;
    }

    public String getNom(){ return nom; }

    public void setNom(String nom){ this.nom = nom; }

    public String toString() {
        return  "Nom: " + getNom() +
                ", Préparation: " + getPreparation() +
                ", Exercice: " + getExercice() +
                ", Repos: " + getRepos() +
                ", Cycles: " + getCycles() +
                ", Tatatas: " + getTabatas();
    }
}
