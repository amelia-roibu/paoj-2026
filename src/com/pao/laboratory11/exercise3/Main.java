package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Main {

    public static final class Transaction {
        private final int id;
        private final BigDecimal amount;
        private final LocalDate date;
        private final String country;
        private final String channel;

        public Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
        }

        public BigDecimal getAmount() { return amount; }
        public String getCountry() { return country; }
        public String getChannel() { return channel; }
        public int getId() { return id; }

        @Override
        public String toString() {
            return String.format(Locale.US, "[Tx #%d] %s | %s | %s | %.2f RON", id, date, country, channel, amount);
        }
    }

    public static final class Snapshot {
        private final Map<String, Long> countByCountry;
        private final Map<String, Long> countByChannel;
        private final BigDecimal totalAmount;
        private final List<Transaction> topTransactions;

        public Snapshot(Map<String, Long> byCountry, Map<String, Long> byChannel, BigDecimal total, List<Transaction> top) {
            this.countByCountry = Collections.unmodifiableMap(new HashMap<>(byCountry));
            this.countByChannel = Collections.unmodifiableMap(new HashMap<>(byChannel));
            this.totalAmount = total;
            this.topTransactions = Collections.unmodifiableList(new ArrayList<>(top));
        }

        public Map<String, Long> getCountByCountry() { return countByCountry; }
        public Map<String, Long> getCountByChannel() { return countByChannel; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public List<Transaction> getTopTransactions() { return topTransactions; }
    }

    public static class CustomCollectors {
        private static class Aggregator {
            Map<String, Long> byCountry = new HashMap<>();
            Map<String, Long> byChannel = new HashMap<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<Transaction> allTransactions = new ArrayList<>();

            public void accumulate(Transaction tx) {
                byCountry.merge(tx.getCountry(), 1L, Long::sum);
                byChannel.merge(tx.getChannel(), 1L, Long::sum);
                totalAmount = totalAmount.add(tx.getAmount());
                allTransactions.add(tx);
            }

            public Aggregator combine(Aggregator other) {
                other.byCountry.forEach((k, v) -> this.byCountry.merge(k, v, Long::sum));
                other.byChannel.forEach((k, v) -> this.byChannel.merge(k, v, Long::sum));
                this.totalAmount = this.totalAmount.add(other.totalAmount);
                this.allTransactions.addAll(other.allTransactions);
                return this;
            }
        }

        public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
            return Collector.of(
                    Aggregator::new,
                    Aggregator::accumulate,
                    Aggregator::combine,
                    agg -> {

                        List<Transaction> sorted = new ArrayList<>(agg.allTransactions);
                        sorted.sort(Comparator.comparing(Transaction::getAmount).reversed()
                                .thenComparingInt(Transaction::getId));

                        List<Transaction> top = sorted.stream().limit(topN).collect(Collectors.toList());

                        return new Snapshot(agg.byCountry, agg.byChannel, agg.totalAmount, top);
                    }
            );
        }
    }

    public static void main(String[] args) {
        List<Transaction> data = Arrays.asList(
                new Transaction(1, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 10), "RO", "WEB"),
                new Transaction(2, new BigDecimal("450.50"), LocalDate.of(2026, 5, 12), "RU", "ATM"),
                new Transaction(3, new BigDecimal("6000.00"), LocalDate.of(2026, 5, 15), "NG", "APP"),
                new Transaction(4, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 16), "RO", "APP"), // Suma identica pentru testat tie-breaker
                new Transaction(5, new BigDecimal("20.00"), LocalDate.of(2026, 5, 20), "NL", "POS"),
                new Transaction(6, new BigDecimal("850.00"), LocalDate.of(2026, 5, 22), "RO", "WEB")
        );

        System.out.println("--- Procesare Stream (o singura trecere) ---");
        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println("\nSnapshot generat cu succes! Total procesat: " + snap.getTotalAmount() + " RON");

        System.out.println("\n--- Interogarea 1: Top 3 Tranzactii ---");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println("\n--- Interogarea 2: Numar Tranzactii pe Tari (Sortat descrescator) ---");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue() + " tranzactii"));

        System.out.println("\n--- Interogarea 3: Canale utilizate (Ordine alfabetica) ---");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue() + " tranzactii"));

        // UnsupportedOperationException
        // snap.getTopTransactions().clear();
        // snap.getCountByCountry().put("US", 99L);
    }
}