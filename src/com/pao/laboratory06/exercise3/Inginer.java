package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer>{
    private double soldCont;

    public Inginer(String nume, String prenume, String telefon, double salariu, double soldCont) {
        super(nume, prenume, telefon, salariu);
        this.soldCont = soldCont;
    }

    @Override
    public int compareTo(Inginer other){
        return this.nume.compareTo(other.nume);
    }

    @Override
    public void autentificare(String user, String parola){
        if (user == null || user.isBlank() || parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("Userul sau parola nu pot fi goale/null.");
        }
        System.out.println("Inginerul " + nume + " a fost autentificat cu succes.");
    }

    @Override
    public double consultareSold(){
        return soldCont;
    }

    @Override
    public boolean efectuarePlata(double suma){
        if(suma <= 0 || suma > soldCont) return false;
        soldCont -= suma;
        return true;
    }

    @Override
    public String toString(){
        return "Inginer: " + nume + " " + prenume + ", salariu: " + salariu;
    }
}
