package MatchMaker;

import java.util.Arrays;

public class Person {
    private String name;
    private String email;
    private String thirdPeriod;
    private String contact;
    private int grade;
    private String gender;
    private int[] answers;
    private String orientation;
    private Person[][] people;

    public Person() {

    }

    public Person(String n, String e, String tP, String c, String gr, String g, int[] a, String o) {
        this.name = n;
        this.email = e;
        this.thirdPeriod = tP;
        this.contact = c;
        this.grade = Integer.parseInt(gr);
        this.gender = g;
        this.answers = a;
        this.orientation = o;
        this.people = new Person[2][2];
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getThirdPeriod() {
        return this.thirdPeriod;
    }

    public String getContact() {
        return this.contact;
    }

    public int getGrade() {
        return this.grade;
    }

    public String getGender() {
        return this.gender;
    }

    public int[] getAnswers() {
        return this.answers;
    }

    public String getOrientation() {
        return this.orientation;
    }

    public void setAnswers(int[] a) {
        this.answers = a;
    }

    public void setAnswers(int i, int j) {
        this.answers[i] = j;
    }

    public void setPeople(Person[][] p) {
        this.people = p;
    }

    public String toString() {
        return this.name;
        // String fin = this.name + ", " + this.grade + ", ";
        // if (this.contact == null || this.contact == "") fin += "No Contact Given";
        // else fin += this.contact;
        // return fin;
    }

    public String toTitleString() {
        return this.name + ", " + this.grade + ", " + this.thirdPeriod;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return grade == person.grade &&
                name.equals(person.name) &&
                email.equals(person.email) &&
                thirdPeriod.equals(person.thirdPeriod) &&
                contact.equals(person.contact) &&
                gender.equals(person.gender) &&
                orientation.equals(person.orientation) &&
                Arrays.equals(answers, person.answers);
    }
}
