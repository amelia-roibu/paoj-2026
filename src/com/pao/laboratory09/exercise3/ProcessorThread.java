package com.pao.laboratory09.exercise3;

import java.util.Locale;

public class ProcessorThread implements Runnable {
    private final CoadaTranzactii coada;
    public volatile boolean activ = true;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (activ || !coada.isEmpty()) {
            Tranzactie t = coada.extrage();
            if (t != null) {
                System.out.printf(Locale.US, "[Processor] Factura #%d - %.2f RON | %s\n", t.id, t.suma, t.data);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}