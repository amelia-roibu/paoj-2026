package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- 1. CONSTANTE FINANCIARE ---");
        System.out.println("TVA-ul actual este: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariul minim este: " + ConstanteFinanciare.SALARIU_MINIM.getValoare() + "\n");

        System.out.println("--- 2. SORTARE INGINERI ---");
        Inginer[] ingineri = {
                new Inginer("Zaharia", "Ion", "0722", 6000, 2000),
                new Inginer("Avram", "Vasile", "0733", 8000, 3000),
                new Inginer("Popescu", "Andrei", "0744", 4000, 1500)
        };

        // Natural order (Alfabetic)
        Arrays.sort(ingineri);
        System.out.println("Sortare naturală (Alfabetic):");
        for (Inginer i : ingineri) System.out.println(i);

        // Sortare cu Comparator (Salariu descrescător)
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\nSortare după salariu (Descrescător):");
        for (Inginer i : ingineri) System.out.println(i);
        System.out.println();

        System.out.println("--- 3. REFERINȚE DE TIP INTERFAȚĂ & EDGE CASES ---");

        // A. Acces prin referința PlataOnline
        PlataOnline plataInginer = ingineri[0]; // Inginerul cu salariul cel mai mare
        plataInginer.autentificare("user_valid", "parola123");
        System.out.println("Sold inginer: " + plataInginer.consultareSold());

        // B. Excepție: Autentificare cu null
        try {
            plataInginer.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Exception prinsă corect: " + e.getMessage());
        }

        // C. Excepție: Trimitere SMS pe entitate fără capabilitate (Inginer)
        try {
            plataInginer.trimiteSMS("Salut!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Exception prinsă corect: " + e.getMessage());
        }
        System.out.println();

        System.out.println("--- 4. PERSOANĂ JURIDICĂ (CU SMS) ---");

        PersoanaJuridica pjCuTelefon = new PersoanaJuridica("TechSRL", "SA", "0711223344", 50000);
        PersoanaJuridica pjFaraTelefon = new PersoanaJuridica("NoPhoneSRL", "SA", "", 10000);

        PlataOnlineSMS plataSms1 = pjCuTelefon;
        PlataOnlineSMS plataSms2 = pjFaraTelefon;

        // Trimitere cu succes
        boolean status1 = plataSms1.trimiteSMS("Plata de 500 lei confirmata.");
        System.out.println("SMS trimis la TechSRL? " + status1);

        // Trimitere eșuată (fără telefon)
        boolean status2 = plataSms2.trimiteSMS("Alertă securitate.");
        System.out.println("SMS trimis la NoPhoneSRL? " + status2);

        // Trimitere eșuată (mesaj gol)
        boolean status3 = plataSms1.trimiteSMS("");
        System.out.println("SMS gol trimis la TechSRL? " + status3);

        // Afișare istoric mesaje din clasa concretă
        System.out.println("\nIstoric SMS-uri TechSRL: " + pjCuTelefon.getSmsTrimise());
    }
}