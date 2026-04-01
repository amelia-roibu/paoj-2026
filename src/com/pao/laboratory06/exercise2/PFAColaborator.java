package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica{
    private double cheltuieliLunare;
    private static final double SALARIU_MINIM_ANUAL = 48600.0;

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public String tipContract() {
        return "PFA";
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.PFA;
    }

    @Override
    public double calculeazaVenitNetAnual(){
        double venitNetBrutAnual = (venitLunar - cheltuieliLunare) * 12;

        double impozit = 0.10 * venitNetBrutAnual;

        double cass = 0;
        if (venitNetBrutAnual < 6 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * (6 * SALARIU_MINIM_ANUAL);
        } else if (venitNetBrutAnual <= 72 * SALARIU_MINIM_ANUAL) {
            cass = 0.10 * venitNetBrutAnual;
        } else {
            cass = 0.10 * (72 * SALARIU_MINIM_ANUAL);
        }

        double cas = 0;
        if (venitNetBrutAnual >= 12 * SALARIU_MINIM_ANUAL && venitNetBrutAnual <= 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (12 * SALARIU_MINIM_ANUAL);
        } else if (venitNetBrutAnual > 24 * SALARIU_MINIM_ANUAL) {
            cas = 0.25 * (24 * SALARIU_MINIM_ANUAL);
        }

        return venitNetBrutAnual - impozit - cass - cas;
    }
}
