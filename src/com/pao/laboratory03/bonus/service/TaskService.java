package com.pao.laboratory03.bonus.service;
import com.pao.laboratory03.bonus.model.*;
import com.pao.laboratory03.bonus.exception.*;
import java.util.*;

/**
 * Structuri de date interne:
 *   - Map<String, Task> tasksById — toate task-urile, indexate după id
 *   - Map<Priority, List<Task>> tasksByPriority — grupate pe prioritate
 *   - List<String> auditLog — jurnal cu TOATE operațiile efectuate
 *
 * Metode:
 *   a) Task addTask(String title, Priority priority)
 *      → generează id automat ("T001", "T002", ...)
 *      → adaugă în ambele map-uri
 *      → logează: "[ADD] T001: 'Fix bug' (HIGH)"
 *
 *   b) void assignTask(String taskId, String assignee)
 *      → aruncă TaskNotFoundException dacă id-ul nu există
 *      → logează: "[ASSIGN] T001 → Ana"
 *
 *   c) void changeStatus(String taskId, Status newStatus)
 *      → aruncă TaskNotFoundException dacă id-ul nu există
 *      → aruncă InvalidTransitionException dacă tranziția nu e permisă
 *        (folosește Status.canTransitionTo())
 *      → logează: "[STATUS] T001: TODO → IN_PROGRESS"
 *
 *   d) List<Task> getTasksByPriority(Priority priority)
 *      → returnează lista din map (sau listă goală)
 *
 *   e) Map<Status, Long> getStatusSummary()
 *      → returnează câte task-uri sunt pe fiecare status
 *      → exemplu: {TODO=3, IN_PROGRESS=2, DONE=5, CANCELLED=1}
 *
 *   f) List<Task> getUnassignedTasks()
 *      → task-uri cu assignee == null
 *
 *   g) void printAuditLog()
 *      → afișează toate intrările din jurnal
 *
 *   h) double getTotalUrgencyScore(int baseDays)
 *      → suma scorurilor de urgență ale task-urilor care NU sunt DONE/CANCELLED
 *      → folosește Priority.calculateScore(baseDays)
 */

public class TaskService {
    private static TaskService instance;
    private final Map<String, Task> tasksById;
    private final Map<Priority, List<Task>> tasksByPriority;
    private final List<String> auditLog;
    private int counter;

    private TaskService() {
        tasksById = new HashMap<>();
        tasksByPriority = new HashMap<>();
        auditLog = new ArrayList<>();
        counter = 1;

    }

    public static TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", counter++);
        Task task = new Task(id, title, priority);

        tasksById.put(id, task);
        tasksByPriority.computeIfAbsent(priority, k -> new ArrayList<>()).add(task);

        auditLog.add(String.format("[ADD] %s: '%s' (%s)", id, title, priority));
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task task = tasksById.get(taskId);
        if (task == null) {
            throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost gasit");
        }
        task.setAssignee(assignee);
        auditLog.add(String.format("[ASSIGN] %s → %s", taskId, assignee));
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task task = tasksById.get(taskId);
        if (task == null) throw new TaskNotFoundException("Task-ul '" + taskId + "' nu a fost gasit");

        if (!task.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(task.getStatus(), newStatus);
        }

        auditLog.add(String.format("[STATUS] %s: %s → %s", taskId, task.getStatus(), newStatus));
        task.setStatus(newStatus);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();

        for (Status s : Status.values()) {
            summary.put(s, 0L);
        }

        for (Task task : tasksById.values()) {
            Status s = task.getStatus();
            summary.put(s, summary.get(s) + 1);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task task : tasksById.values()) {
            if (task.getAssignee() == null) {
                unassigned.add(task);
            }
        }
        return unassigned;
    }

    public void printAuditLog() {
        System.out.println("Audit Log:");
        for (String log : auditLog) {
            System.out.println(log);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;
        for (Task task : tasksById.values()) {
            if (task.getStatus() != Status.DONE && task.getStatus() != Status.CANCELLED) {
                total += task.getPriority().calculateScore(baseDays);
            }
        }
        return total;
    }
}