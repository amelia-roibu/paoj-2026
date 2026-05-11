package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;
        int n = Integer.parseInt(scanner.nextLine().trim());

        List<Tranzactie> tranzactii = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            String[] tokens = line.split(" ");
            int id = Integer.parseInt(tokens[0]);
            double suma = Double.parseDouble(tokens[1]);
            String data = tokens[2];
            String contSursa = tokens[3];
            String contDestinatie = tokens[4];
            TipTranzactie tip = TipTranzactie.valueOf(tokens[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.setNote("procesat");
            tranzactii.add(t);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))){
            oos.writeObject(tranzactii);
        } catch(IOException e){
            System.out.println("Eroare la serializare: " + e.getMessage());
            return;
        }

        List<Tranzactie> tranzactiiDeserializate;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))){
            tranzactiiDeserializate = (List<Tranzactie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Eroare la deserializare: " + e.getMessage());
            return;
        }

        while(scanner.hasNextLine()){
            String line = scanner.nextLine().trim();
            String[] tokens = line.split(" ");
            String command = tokens[0];

            switch(command){
                case "LIST":
                    for (Tranzactie t: tranzactiiDeserializate){
                        System.out.println(t.formatToOutput());
                    }
                    break;

                case "FILTER":
                    if (tokens.length > 1) {
                        String prefix = tokens[1];
                        boolean found = false;
                        for (Tranzactie t : tranzactiiDeserializate) {
                            if (t.getData().startsWith(prefix)) {
                                System.out.println(t.formatToOutput());
                                found = true;
                            }
                        }
                        if (!found) {
                            System.out.println("Niciun rezultat.");
                        }
                    }
                    break;

                case "NOTE":
                    if (tokens.length > 1) {
                        int id = Integer.parseInt(tokens[1]);
                        boolean found = false;
                        for (Tranzactie t : tranzactiiDeserializate) {
                            if (t.getId() == id) {
                                System.out.println("NOTE[" + id + "]: " + t.getNote());
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println("NOTE[" + id + "]: not found");
                        }
                    }
                    break;
            }
        }
        scanner.close();
    }
}
