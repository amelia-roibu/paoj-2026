package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 4; i++) {
            int txId = atmId * 1000 + i;
            double suma = 100.0 + (Math.random() * 900);
            Tranzactie t = new Tranzactie(txId, suma, "2024-05-14");

            System.out.printf(Locale.US, "[ATM-%d] trimite: Tranzactie #%d %.2f RON\n", atmId, t.id, t.suma);

            coada.adauga(t, atmId);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}