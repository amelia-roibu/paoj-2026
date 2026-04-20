package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;
        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++){
            String line = scanner.nextLine().trim();
            String[] tokens = line.split(" ");
            try{
                if (tokens[0].equals("STANDARD")) {
                    comenzi.add(new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]), tokens[3]));
                } else if (tokens[0].equals("DISCOUNTED")) {
                    comenzi.add(new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]));
                } else if (tokens[0].equals("GIFT")) {
                    comenzi.add(new ComandaGratuita(tokens[1], tokens[2]));
                }
            } catch (Exception e) {
                System.out.println("Input invalid la linia: " + line);
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        while (scanner.hasNextLine()){
            String commandLine = scanner.nextLine().trim();
            if (commandLine.isEmpty()) continue;

            String[] tokens = commandLine.split(" ");
            String action = tokens[0];

            if (action.equals("QUIT")){
                break;
            }

            switch (action) {
                case "STATS":
                    System.out.println("--- STATS ---");
                    Map<Class<? extends Comanda>, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    c -> c.getClass(),
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));

                    if (medii.containsKey(ComandaStandard.class))
                        System.out.printf(Locale.US, "STANDARD: medie = %.2f lei\n", medii.get(ComandaStandard.class));
                    if (medii.containsKey(ComandaRedusa.class))
                        System.out.printf(Locale.US, "DISCOUNTED: medie = %.2f lei\n", medii.get(ComandaRedusa.class));
                    if (medii.containsKey(ComandaGratuita.class))
                        System.out.printf(Locale.US, "GIFT: medie = %.2f lei\n", medii.get(ComandaGratuita.class));
                    break;

                case "FILTER":
                    if (tokens.length > 1) {
                        double threshold = Double.parseDouble(tokens[1]);
                        System.out.printf(Locale.US, "--- FILTER (>= %.2f) ---\n", threshold);

                        comenzi.stream()
                                .filter(c -> c.pretFinal() >= threshold)
                                .forEach(c -> System.out.println(formatPentruPrint(c)));
                    }
                    break;

                case "SORT":
                    System.out.println("--- SORT (by client, then by pret) ---");
                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient)
                                    .thenComparing(Comanda::pretFinal))
                            .forEach(c -> System.out.println(formatPentruPrint(c)));
                    break;

                case "SPECIAL":
                    System.out.println("--- SPECIAL (discount > 15%) ---");
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa)
                            .map(c -> (ComandaRedusa) c)
                            .filter(cr -> cr.getDiscountProcent() > 15)
                            .forEach(cr -> System.out.println(cr.descriere().replace(" [PLACED]", "")));
                    break;

                default:
                    System.out.println("Comanda necunoscuta: " + action);
                    break;
            }
        }
        scanner.close();
    }

    private static String formatPentruPrint(Comanda c) {
        String original = c.descriere();
        return original.replace(" [PLACED]", "");
    }
}
