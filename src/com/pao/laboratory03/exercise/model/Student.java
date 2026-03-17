package com.pao.laboratory03.exercise.model;
import com.pao.laboratory03.exercise.exception.*;

import java.util.*;

/* *
 * 2. model/Student.java — CLASĂ
 *    - Câmpuri private: String name, int age, Map<Subject, Double> grades
 *    - Constructor: Student(String name, int age)
 *      → inițializează grades ca HashMap gol
 *      → validare: dacă age < 18 sau age > 60, aruncă InvalidStudentException
 *    - Metode: getName(), getAge(), getGrades()
 *    - addGrade(Subject subject, double grade)
 *      → dacă grade < 1 sau grade > 10, aruncă InvalidGradeException
 *      → pune nota în map (suprascrie dacă materia există deja)
 *    - double getAverage()
 *      → calculează media aritmetică a notelor (returnează 0 dacă nu are note)
 *    - toString() → "Student{name='Ana', age=20, avg=8.50}"
 */

public class Student implements Comparable<Student> {
    private final String name;
    private final int age;
    private final Map<Subject, Double> grades;

    public Student(String name, int age) {
        if (age < 18 || age > 60){
            throw new InvalidStudentException("Varsta " + age + " nu este valida (18-60)");
        }

        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<Subject, Double> getGrades() {
        return grades;
    }

    public void addGrade(Subject subject, double grade){
        if (grade < 1 || grade > 10){
            throw new InvalidGradeException("Nota " + grade + " nu este valida (1-10)");
        }
        grades.put(subject, grade);
    }

    public double getAverage(){
        if (!grades.isEmpty()){
            double sum = 0;
            for (double grade : grades.values()){
                sum += grade;
            }
            return sum / grades.size();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Student{" + "name='" + name + "', age=" + age + ", avg=" + getAverage() + '}';
    }

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.getAverage(), this.getAverage());
    }
}
