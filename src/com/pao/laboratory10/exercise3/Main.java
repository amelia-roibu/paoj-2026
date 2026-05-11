package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = Arrays.asList(
                new Tranzactie(1, 1500.00, "2024-01-10", TipTranzactie.CREDIT, "RO01INGB"),
                new Tranzactie(2,  250.00, "2024-01-15", TipTranzactie.DEBIT,  "RO01INGB"),
                new Tranzactie(3,  100.50, "2024-01-20", TipTranzactie.DEBIT,  "RO88BTRL"),
                new Tranzactie(4, 3200.00, "2024-02-05", TipTranzactie.CREDIT, "RO88BTRL"),
                new Tranzactie(5,  850.00, "2024-02-14", TipTranzactie.DEBIT,  "RO99BRDE"),
                new Tranzactie(6,  120.00, "2024-02-28", TipTranzactie.DEBIT,  "RO01INGB"),
                new Tranzactie(7,  900.00, "2024-03-01", TipTranzactie.CREDIT, "RO99BRDE"),
                new Tranzactie(8,  300.00, "2024-03-10", TipTranzactie.DEBIT,  "RO01INGB"),
                new Tranzactie(9,   55.00, "2024-03-15", TipTranzactie.DEBIT,  "RO88BTRL"),
                new Tranzactie(10, 450.00, "2024-03-25", TipTranzactie.CREDIT, "RO88BTRL")
        );

        System.out.println("=== 1. Tranzactiile de tip CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n=== 2. Suma totala procesata ===");
        double totalProcesat = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON\n", totalProcesat);

        System.out.println("\n=== 3. Suma procesata per luna ===");
        Map<String, Double> sumaPeLuni = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new, Collectors.summingDouble(Tranzactie::getSuma)
                ));
        sumaPeLuni.forEach((luna, suma) ->
                System.out.printf(Locale.US, "Per luna: %s: %.2f RON\n", luna, suma));

        System.out.println("\n=== 4. Top 3 tranzactii (ca valoare) ===");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        System.out.println("\n=== 6. Suma medie a tranzactiilor ===");
        double media = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON\n", media);

        System.out.println("\n=== 7. EXTRAS DE CONT LUNAR ===");
        Map<String, List<Tranzactie>> tranzactiiPeLuni = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new, Collectors.toList()
                ));

        tranzactiiPeLuni.forEach((luna, listaTranzactiiLuna) -> {
            double totalLuna = listaTranzactiiLuna.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();
            System.out.printf(Locale.US, "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON\n",
                    luna, listaTranzactiiLuna.size(), totalLuna);
        });
    }
}