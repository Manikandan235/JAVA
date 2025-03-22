import java.util.*;
import java.io.*;

class Student {
    int rollNumber;
    String name;
    double marks;
    String grade;
    boolean isPresent;

    public Student(int rollNumber, String name, double marks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.marks = marks;
        this.grade = calculateGrade(marks);
        this.isPresent = false;
    }

    String calculateGrade(double marks) {
        if (marks >= 90) return "A";
        else if (marks >= 80) return "B";
        else if (marks >= 70) return "C";
        else if (marks >= 60) return "D";
        else return "F";
    }
}

public class SMS {
    private static List<Student> students = new ArrayList<>();
    private static final String adminuser = "a";
    private static final String adminpwd= "a1";
    private static final String filename = "C:\\Users\\ramya\\eclipse-workspace\\FristProject\\src\\students";
    private static final String filenames= "C:\\Users\\ramya\\eclipse-workspace\\FristProject\\src\\students1";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n Welcome to Student Management System ");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Student");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1 -> adminLogin(scanner);
                    case 2 -> studentLogin(scanner);
                    case 3 -> {
                        System.out.println("Exiting... Goodbye!");
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }  
    static void adminLogin(Scanner scanner) {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        if (username.equals(adminuser) && password.equals(adminpwd)) {
            adminMenu(scanner);
        } else {
            System.out.println("Invalid username or password.");
        }
    } 
    static void studentLogin(Scanner scanner) {
        if (students.isEmpty()) {
            System.out.println("No students found");
            return;
        }
        viewStudents();
    }
    static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Mark Attendance");
            System.out.println("6. Import Students from File");
            System.out.println("7. Export Students to File");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                switch (choice) {
                    case 1 -> addStudent(scanner);
                    case 2 -> viewStudents();
                   case 3 -> searchStudent(scanner);
                    case 4 -> deleteStudent(scanner);
                   case 5 -> markAttendance(scanner);
                    case 6 -> importStudents();
                    case 7 -> exportStudents();
                    case 8 -> {
                       System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
            }
        }
    }
    static void addStudent(Scanner scanner) {
        System.out.print("Enter Roll Number: ");
        int roll = scanner.nextInt();
       scanner.nextLine();
       System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Marks: ");
       double marks = scanner.nextDouble();
        students.add(new Student(roll, name, marks));
        System.out.println("Student Added Successfully");
    }   
    static void viewStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found");
            return;
        }
        for (Student s : students) {
            System.out.println("Roll Number1: " + s.rollNumber + ", Name: " + s.name + ", Marks: " + s.marks + ", Grade: " + s.grade + ", Present: " + s.isPresent);
        }
    }
    static void searchStudent(Scanner scanner) {
        System.out.print("Enter Roll Number or Name to search: ");
        String input = scanner.nextLine();
        boolean found = false;
        for (Student s : students) {
            if (String.valueOf(s.rollNumber).equals(input) || s.name.equalsIgnoreCase(input)) {
                System.out.println("Roll: " + s.rollNumber + ", Name: " + s.name + ", Marks: " + s.marks + ", Grade: " + s.grade + ", Present: " + s.isPresent);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Student not found.");
        }
    }   
    static void deleteStudent(Scanner scanner) {
        System.out.print("Enter Roll Number of student to delete: ");
        int roll = scanner.nextInt();
        scanner.nextLine();
        students.removeIf(s -> s.rollNumber == roll);
        System.out.println("Student deleted successfully!");
    }
    static void markAttendance(Scanner scanner) {
        System.out.print("Enter Roll Number to mark attendance: ");
        int roll = scanner.nextInt();
        scanner.nextLine();
        for (Student s : students) {
            if (s.rollNumber == roll) {
                s.isPresent = true;
                System.out.println("attendance marked for " + s.name);
                return;
            }}
        System.out.println("student not found.");
    }  
    static void importStudents() {
     try(BufferedReader reader = new BufferedReader(new FileReader(filenames))){
            String line;
            students.clear();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                students.add(new Student(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2])));
            }
            System.out.println("Students imported successfully!");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }  
    static void exportStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (Student s : students) {
                writer.write("Roll Number: "+s.rollNumber + "\nName: "+ s.name + "\nMark: " + s.marks + "\n");
            }
            System.out.println("Students exported successfully!");
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
