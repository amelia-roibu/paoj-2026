package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS{
    private List<String> smsTrimise;
    private double soldCont;

    public PersoanaJuridica(String nume, String prenume, String telefon, double soldCont) {
        super(nume, prenume, telefon);
        this.soldCont = soldCont;
        this.smsTrimise = new ArrayList<>();
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("Userul sau parola invalide.");
        }
        System.out.println("Persoana Juridica " + nume + " autentificata.");
    }

    @Override
    public double consultareSold() {
        return soldCont;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0 || suma > soldCont) return false;
        soldCont -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (this.telefon == null || this.telefon.isBlank()) return false;
        if (mesaj == null || mesaj.isBlank()) return false;
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }
}
