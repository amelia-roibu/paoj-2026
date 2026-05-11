package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        LinkedList<Tranzactie> coada = new LinkedList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            String[] tokens = line.split(" ");
            String command = tokens[0];

            switch (command) {
                case "ENQUEUE": {
                    int id = Integer.parseInt(tokens[1]);
                    double suma = Double.parseDouble(tokens[2]);
                    String data = tokens[3];
                    TipTranzactie tip = TipTranzactie.valueOf(tokens[4]);
                    coada.addLast(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie extrasa = coada.removeFirst();
                        System.out.println("Procesat: " + extrasa);
                    }
                    break;
                }
                case "PUSH": {
                    int id = Integer.parseInt(tokens[1]);
                    double suma = Double.parseDouble(tokens[2]);
                    String data = tokens[3];
                    TipTranzactie tip = TipTranzactie.valueOf(tokens[4]);
                    coada.addFirst(new Tranzactie(id, suma, data, tip));
                    break;
                }
                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie extrasa = coada.removeFirst();
                        System.out.println("Extras: " + extrasa);
                    }
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }
                case "REMOVE_DEBIT": {
                    int contor = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            contor++;
                        }
                    }
                    System.out.println("Eliminat " + contor + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double threshold = Double.parseDouble(tokens[1]);
                    int contor = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getSuma() < threshold) {
                            itr.remove();
                            contor++;
                        }
                    }
                    System.out.printf(Locale.US, "Eliminat %d tranzactii sub %.2f RON.\n", contor, threshold);
                    break;
                }
                default:
                    break;
            }
        }
        scanner.close();
    }
}
