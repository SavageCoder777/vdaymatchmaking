package MatchMaker;

import java.util.*;
import java.io.*;
import java.util.PriorityQueue;

public class Correlation {
    private static ArrayList<Person> people = new ArrayList<Person>();

    public static void main(String[] args) {
        String inputFile = "PeopleInfo.txt";
        try {
            Scanner scanner = new Scanner(new File(inputFile));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] c = line.split("\t");
                int[] a = new int[10];
                for (int i = 0; i < 10; i++) {
                    a[i] = Integer.parseInt(c[i+5]);
                }
                people.add(new Person(c[0], c[1], c[2], c[15], c[3], c[4], a, c[17]));
            }
            printMatches();
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }

    public static void printMatches() {
        String outputFile = "Matches.tex";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("\\documentclass[12pt,landscape]{article}");
            writer.println("\\usepackage[a4paper,margin=1.5cm]{geometry}");
            writer.println("\\usepackage{multicol}");
            writer.println("\\usepackage{pifont}");
            writer.println("\\usepackage{xcolor}");
            writer.println("\\setlength{\\columnsep}{1.5cm}");
            writer.println("\\renewcommand{\\baselinestretch}{1.4}");
            writer.println("\\usepackage{fancyhdr}");
            writer.println("\\pagestyle{empty}");
            writer.println("\\begin{document}");
            int num = 1;
            int count = 0;
            for (Person person : people) {
                if (count % 4 == 0) {
                    if (count > 0) {
                        writer.println("\\newpage");
                        num = 1;
                    }
                }
                writer.println("\\noindent\\begin{minipage}[t][0.24\\textheight][t]{0.48\\textwidth}"); 
                writer.println("\\large");
                if (num == 3 || num == 4) writer.println("\\vspace{4.2cm}");
                writer.println("\\textbf{\\underline{" + escapeLatex(person.toTitleString()) + "}}\\\\");
                writer.println("\\textit{Name, Grade, Contact - Percentage Match}\\\\");
                for (Person[] match : calculateCorr(people, person)) {
                    if (match[0] != null) {
                        writer.printf("%s - %s\\%%\\\\\n", escapeLatex(match[0].toString()), ((Percent) match[1]).getPercent());
                    }
                }
                writer.println("\\textcolor{red}{\\ding{170}}\\textit{\\textbf{Happy Valentine's Day!} from L2's A-Team }\\textcolor{red}{\\ding{170}}\\\\");
                writer.println("\\end{minipage}\\hfill");
                count++;
                if (count % 2 == 0) {
                    writer.println("\\vspace{1cm}");
                }
                num++;
            }
            writer.println("\\end{document}");
        } catch (IOException e) {
            System.err.println("Error writing LaTeX file: " + e.getMessage());
        }
    }

    public static String escapeLatex(String text) {
        return text.replace("_", "\\_").replace("%", "\\%");
    }

    public static Person[][] calculateCorr(ArrayList<Person> a, Person n) {
        PriorityQueue<PersonMatch> topMatches = new PriorityQueue<>();
        for (Person candidate : a) {
            if (!candidate.equals(n) && isCompatibleGrade(n.getGrade(), candidate.getGrade()) && isCompatibleGender(n.getGender(), n.getOrientation(), candidate.getGender(), candidate.getOrientation())) {
                double correlation = calculateCorrelation(n.getAnswers(), candidate.getAnswers());
                topMatches.offer(new PersonMatch(candidate, correlation));
                if (topMatches.size() > 5) topMatches.poll();
            }
        }
        Person[][] matches = new Person[5][2];
        int index = 4;
        while (!topMatches.isEmpty()) {
            PersonMatch match = topMatches.poll();
            matches[index][0] = match.person;
            matches[index][1] = new Percent((int) (50 + (50 * match.correlation)));
            index--;
        }
        return matches;
    }

    public static boolean isCompatibleGrade(int personGrade, int checkingGrade) {
        if (personGrade == 9) return (checkingGrade == 9 || checkingGrade == 10);
        if (personGrade == 10) return (checkingGrade == 9 || checkingGrade == 10 || checkingGrade == 11);
        if (personGrade == 11) return (checkingGrade == 10 || checkingGrade == 11 || checkingGrade == 12);
        if (personGrade == 12) return (checkingGrade == 11 || checkingGrade == 12);
        return false;
    }

    public static boolean isCompatibleGender(String genderOne, String orientationOne, String genderTwo, String orientationTwo) {
        if (genderOne.equals("Male") && orientationOne.equals("Straight")) {
            return genderTwo.equals("Female");
        } if (genderOne.equals("Male") && orientationOne.equals("Gay/Lesbian")) {
            return genderTwo.equals("Male");
        } if (genderOne.equals("Female") && orientationOne.equals("Straight")) {
            return genderTwo.equals("Male");
        } if (genderOne.equals("Female") && orientationOne.equals("Gay/Lesbian")) {
            return genderTwo.equals("Female");
        }
        return true;
    }

    public static double calculateCorrelation(int[] a, int[] b) {
        int n = a.length;
        double sumX = 0, sumY = 0, sumXY = 0;
        double sumX2 = 0, sumY2 = 0;
        for (int i = 0; i < n; i++) {
            sumX += a[i];
            sumY += b[i];
            sumXY += a[i] * b[i];
            sumX2 += a[i] * a[i];
            sumY2 += b[i] * b[i];
        }
        double num = (n * sumXY) - (sumX * sumY);
        double denom = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        if (denom == 0) return 0;
        return (num / denom);
    }
}