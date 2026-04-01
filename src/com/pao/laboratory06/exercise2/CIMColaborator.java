package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica{
    private boolean areBonusCIM = false;

    @Override
    public void citeste(Scanner in){
        this.nume = in.next();
        this.prenume = in.next();
        this.venitLunar = in.nextDouble();

        String bonus = in.nextLine().trim();
        if(bonus.equals("DA")){
            this.areBonusCIM = true;
        }
    }

    @Override
    public String tipContract(){
        return "CIM";
    }

    @Override
    public TipColaborator getTip(){
        return TipColaborator.CIM;
    }

    @Override
    public boolean areBonus(){
        return areBonusCIM;
    }

    @Override
    public double calculeazaVenitNetAnual(){
        double venitNetAnual = venitLunar * 12 * 0.55;
        if (areBonus()) {
            venitNetAnual += venitNetAnual * 0.10;
        }
        return venitNetAnual;
    }
}
