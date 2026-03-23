package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private AngajatService() {}

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    // metodele noi
    private void logAction(String action, String target) {
        AuditEntry newEntry = new AuditEntry(action, target, LocalDateTime.now().toString());

        AuditEntry[] tmp = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, tmp, 0, auditLog.length);
        tmp[tmp.length - 1] = newEntry;
        auditLog = tmp;
    }

    public void printAuditLog() {
        System.out.println("\n--- AUDIT LOG ---");
        if (auditLog.length == 0) {
            System.out.println("Nicio actiune inregistrata.");
        }
        for (AuditEntry entry : auditLog) {
            System.out.println(entry);
        }
    }

    // metodele vechi
    public void addAngajat(Angajat a) {
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati = tmp;
        System.out.println("Angajat adaugat: " + a.getNume());

        logAction("ADD", a.getNume());
    }

    public void printAll() {
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        System.out.println("--- Angajați după salariu (descrescător) ---");
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);
        for (Angajat a : copy) {
            System.out.println(a);
        }
    }

    public void findByDepartament(String numeDepartament) {
        logAction("FIND_BY_DEPT", numeDepartament);

        System.out.println("\n--- Angajati din " + numeDepartament + " ---");
        boolean gasit = false;

        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDepartament)) {
                System.out.println(a);
                gasit = true;
            }
        }

        if (!gasit) {
            System.out.println("Niciun angajat in departamentul " + numeDepartament);
        }
    }
}