package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            studenti.add(new Student(nume, varsta, adresa));
        }
        br.close();

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) return;
        String comandaLinie = scanner.nextLine().trim();
        String[] tokens = comandaLinie.split(" ", 2);
        String actiune = tokens[0];

        switch (actiune) {
            case "PRINT":
                for (Student s : studenti) {
                    System.out.println(s);
                }
                break;

            case "SHALLOW":
                if (tokens.length > 1) {
                    Student original = gasesteStudent(studenti, tokens[1]);
                    if (original != null) {
                        Student clona = (Student) original.shallowClone();
                        clona.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + original);
                        System.out.println("Clona: " + clona);
                    }
                }
                break;

            case "DEEP":
                if (tokens.length > 1) {
                    Student original = gasesteStudent(studenti, tokens[1]);
                    if (original != null) {
                        Student clona = (Student) original.deepClone();
                        clona.getAdresa().setOras("MODIFICAT");
                        System.out.println("Original: " + original);
                        System.out.println("Clona: " + clona);
                    }
                }
                break;

            default:
                System.out.println("Comanda necunoscuta.");
        }
        scanner.close();
    }

    private static Student gasesteStudent(List<Student> studenti, String nume) {
        for (Student s : studenti) {
            if (s.getNume().equals(nume)) {
                return s;
            }
        }
        return null;
    }
}
