package com.pao.laboratory03.exercise.service;
import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.*;
import java.util.*;

/**
 * 6. service/StudentService.java — SERVICIU (Singleton)
 *    - Câmp: List<Student> students (ArrayList)
 *    - Singleton pattern (constructor privat, getInstance())
 *    - Metode:
 *      a) void addStudent(String name, int age)
 *         → creează Student și adaugă în listă
 *         → dacă există deja un student cu același nume, aruncă RuntimeException
 *      b) Student findByName(String name)
 *         → caută în listă, aruncă StudentNotFoundException dacă nu găsește
 *      c) void addGrade(String studentName, Subject subject, double grade)
 *         → găsește studentul (findByName) și adaugă nota
 *      d) void printAllStudents()
 *         → afișează toți studenții cu notele lor
 *      e) void printTopStudents()
 *         → sortează studenții descrescător după medie și afișează
 *      f) Map<Subject, Double> getAveragePerSubject()
 *         → calculează media pe fiecare materie (din toți studenții care au notă)
 */

public class StudentService {
    private static StudentService instance;
    private List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age) {
        try {
            findByName(name);
            throw new RuntimeException("Studentul " + name + " exista deja");
        } catch (StudentNotFoundException e) {
            students.add(new Student(name, age));
        }
    }

    public Student findByName(String name){
        for (Student student : students){
            if (student.getName().equals(name)){
                return student;
            }
        }
        throw new StudentNotFoundException("Studentul " + name + " nu exista in baza de date");
    }

    public void addGrade(String studentName, Subject subject, double grade){
        Student student = findByName(studentName);
        student.addGrade(subject, grade);
    }

    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu exista studenti in baza de date.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
            System.out.println("Note: " + student.getGrades());
        }
    }

    public void printTopStudents() {
        List<Student> sortedStudents = new ArrayList<>(students);
        Collections.sort(sortedStudents);
        for (Student student : sortedStudents) {
            System.out.println(student);
        }
    }

    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> sums = new HashMap<>();
        Map<Subject, Integer> counts = new HashMap<>();

        for (Student student : students) {
            for (Map.Entry<Subject, Double> entry : student.getGrades().entrySet()) {
                Subject subject = entry.getKey();
                Double grade = entry.getValue();

                sums.put(subject, sums.getOrDefault(subject, 0.0) + grade);
                counts.put(subject, counts.getOrDefault(subject, 0) + 1);
            }
        }

        Map<Subject, Double> averages = new HashMap<>();
        for (Subject subject : sums.keySet()) {
            averages.put(subject, sums.get(subject) / counts.get(subject));
        }

        return averages;
    }
}
