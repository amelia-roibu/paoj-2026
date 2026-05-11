package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<Tranzactie> coada = new LinkedList<>();
    private final int capacitate = 5;
    private boolean deschisa = true;

    public synchronized void adauga(Tranzactie t, int atmId) {
        while (coada.size() == capacitate) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() {
        while (coada.isEmpty()) {
            if (!deschisa) return null;

            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean isEmpty() {
        return coada.isEmpty();
    }

    public synchronized void opreste() {
        deschisa = false;
        notifyAll();
    }
}