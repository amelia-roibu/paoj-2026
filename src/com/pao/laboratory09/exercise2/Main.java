package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public enum Status {
        PENDING, PROCESSED, REJECTED
    }

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) return;
        int n = Integer.parseInt(scanner.nextLine().trim());

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                String line = scanner.nextLine().trim();

                String[] tokens = line.split(" ");
                int id = Integer.parseInt(tokens[0]);
                double suma = Double.parseDouble(tokens[1]);
                String data = tokens[2];
                TipTranzactie tip = TipTranzactie.valueOf(tokens[3]);

                // Offset 0 (4 bytes): id -> little-endian
                byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                dos.write(idBytes);

                // Offset 4 (8 bytes): suma -> little-endian
                byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                dos.write(sumaBytes);

                // Offset 12 (10 bytes): data -> ASCII (paddat cu spații dacă e nevoie)
                StringBuilder sbData = new StringBuilder(data);
                while (sbData.length() < 10) sbData.append(" ");
                dos.write(sbData.toString().getBytes(StandardCharsets.US_ASCII));

                // Offset 22 (1 byte): tip (0=CREDIT, 1=DEBIT)
                dos.write(tip == TipTranzactie.CREDIT ? 0 : 1);

                // Offset 23 (1 byte): status inițial (0=PENDING)
                dos.write(Status.PENDING.ordinal());

                // Offset 24 (8 bytes): padding de zerouri
                dos.write(new byte[8]);
            }
        } catch (IOException e) {
            System.out.println("Eroare la scrierea initiala: " + e.getMessage());
            return;
        }

        // 2. CITIREA ȘI ACTUALIZAREA cu RandomAccessFile
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                String[] tokens = line.split(" ");
                String command = tokens[0];

                switch (command) {
                    case "READ": {
                        int idx = Integer.parseInt(tokens[1]);
                        readAndPrintRecord(raf, idx);
                        break;
                    }
                    case "UPDATE": {
                        int idx = Integer.parseInt(tokens[1]);
                        Status newStatus = Status.valueOf(tokens[2]);

                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.write(newStatus.ordinal());

                        System.out.println("Updated [" + idx + "]: " + newStatus.name());
                        break;
                    }
                    case "PRINT_ALL": {
                        for (int i = 0; i < n; i++) {
                            readAndPrintRecord(raf, i);
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Eroare la lucrul cu RandomAccessFile: " + e.getMessage());
        }
        scanner.close();
    }

    private static void readAndPrintRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] buffer = new byte[RECORD_SIZE];
        raf.readFully(buffer);

        // Folosim ByteBuffer cu LITTLE_ENDIAN ca sa despachetam corect numerele
        ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);

        int id = bb.getInt(); // extrage primii 4 octeți
        double suma = bb.getDouble(); // urmatorii 8 octeți

        byte[] dataBytes = new byte[10];
        bb.get(dataBytes);
        String data = new String(dataBytes, StandardCharsets.US_ASCII).trim();

        TipTranzactie tip = bb.get() == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        Status status = Status.values()[bb.get()];

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n", idx, id, data, tip.name(), suma, status.name());
    }
}
