import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Custom Exception for invalid marks
class InvalidMarksException extends Exception {
    public InvalidMarksException(String message) {
        super(message);
    }
}

// Custom Exception for invalid name
class InvalidNameException extends Exception {
    public InvalidNameException(String message) {
        super(message);
    }
}

// Main class extending JFrame and implementing ActionListener
public class StudentPerformanceGUI extends JFrame implements ActionListener {
    // GUI Components
    JTextField nameField, rollField, mathField, physicsField, chemField, engField, javaField, pythonField;
    JButton submit;

    // Constructor to build UI
    public StudentPerformanceGUI() {
        setTitle("Student Performance Evaluator");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 2, 10, 10)); // Changed from 10 to 9 rows

        Font font = new Font("Arial", Font.PLAIN, 16);
        // Set font for all labels and text fields directly

        JLabel nameLabel = new JLabel("Student Name:");
        nameLabel.setFont(font);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setFont(font);
        add(nameField);

        JLabel rollLabel = new JLabel("Roll Number:");
        rollLabel.setFont(font);
        add(rollLabel);
        rollField = new JTextField();
        rollField.setFont(font);
        add(rollField);

        JLabel mathLabel = new JLabel("Maths Marks:");
        mathLabel.setFont(font);
        add(mathLabel);
        mathField = new JTextField();
        mathField.setFont(font);
        add(mathField);

        JLabel physicsLabel = new JLabel("Physics Marks:");
        physicsLabel.setFont(font);
        add(physicsLabel);
        physicsField = new JTextField();
        physicsField.setFont(font);
        add(physicsField);

        JLabel chemLabel = new JLabel("Chemistry Marks:");
        chemLabel.setFont(font);
        add(chemLabel);
        chemField = new JTextField();
        chemField.setFont(font);
        add(chemField);

        JLabel engLabel = new JLabel("English Marks:");
        engLabel.setFont(font);
        add(engLabel);
        engField = new JTextField();
        engField.setFont(font);
        add(engField);

        JLabel javaLabel = new JLabel("Java Marks:");
        javaLabel.setFont(font);
        add(javaLabel);
        javaField = new JTextField();
        javaField.setFont(font);
        add(javaField);

        JLabel pythonLabel = new JLabel("Python Marks:");
        pythonLabel.setFont(font);
        add(pythonLabel);
        pythonField = new JTextField();
        pythonField.setFont(font);
        add(pythonField);

        // Submit Button
        submit = new JButton("Submit");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        submit.setBackground(Color.BLUE);
        submit.setForeground(Color.WHITE);
        submit.addActionListener(this);
        add(new JLabel()); // empty cell
        add(submit);

        setVisible(true);
    }

    // Method to handle submit button click
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();

            // Validate name (only characters)
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }
            if (!name.matches("[a-zA-Z ]+")) {
                throw new InvalidNameException("Name must contain only letters and spaces.");
            }

            // Validate roll no. (must be integer and not empty)
            if (roll.isEmpty()) {
                throw new IllegalArgumentException("Roll Number cannot be empty.");
            }
            int rollNumber;
            try {
                rollNumber = Integer.parseInt(roll);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Roll Number must be an integer.");
            }

            // Parse and validate marks
            int[] marks = new int[6];
            marks[0] = parseMarks(mathField.getText(), "Maths");
            marks[1] = parseMarks(physicsField.getText(), "Physics");
            marks[2] = parseMarks(chemField.getText(), "Chemistry");
            marks[3] = parseMarks(engField.getText(), "English");
            marks[4] = parseMarks(javaField.getText(), "Java");
            marks[5] = parseMarks(pythonField.getText(), "Python");

            int total = 0;
            boolean failedAny = false;
            StringBuilder failedSubjects = new StringBuilder();
            String[] subjectNames = {"Maths", "Physics", "Chemistry", "English", "Java", "Python"};
            for (int i = 0; i < marks.length; i++) {
                if (marks[i] < 40) {
                    failedAny = true;
                    failedSubjects.append(subjectNames[i]).append(" (" + marks[i] + "), ");
                }
                total += marks[i];
            }

            double average = total / 6.0;
            String grade, remark;

            // Grade and Remark logic
            if (average > 90) {
                grade = "H";
                remark = "Distinction! Keep it up.";
            } else if (average > 80) {
                grade = "S";
                remark = "Well done!";
            } else if (average > 70) {
                grade = "A";
                remark = "Good.";
            } else if (average > 60) {
                grade = "B";
                remark = "Efforts are acknowledged. Try harder.";
            } else if (average > 50) {
                grade = "C";
                remark = "You passed. Try to improve.";
            } else {
                grade = "F";
                remark = "Better luck next time.";
            }

            if (failedAny) {
                // Remove trailing comma and space
                String failedList = failedSubjects.toString();
                if (failedList.endsWith(", ")) {
                    failedList = failedList.substring(0, failedList.length() - 2);
                }
                remark += " You have failed in: " + failedList + ".";
            }

            showResult(name, String.valueOf(rollNumber), total, average, grade, remark);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter only numeric values for marks.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidMarksException | InvalidNameException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Something went wrong: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to parse and validate marks field
    int parseMarks(String text, String subject) throws InvalidMarksException {
        text = text.trim();
        if (text.isEmpty()) throw new InvalidMarksException(subject + " marks cannot be empty.");
        int marks = Integer.parseInt(text);
        if (marks < 0 || marks > 100) throw new InvalidMarksException(subject + " marks must be between 0 and 100.");
        return marks;
    }

    // Helper method to get grade for a subject
    String getSubjectGrade(int marks) {
        if (marks > 90) return "H";
        else if (marks > 80) return "S";
        else if (marks > 70) return "A";
        else if (marks > 60) return "B";
        else if (marks > 50) return "C";
        else return "F";
    }

    // Method to display result in a new frame
    void showResult(String name, String roll, int total, double avg, String grade, String remark) {
        JFrame resultFrame = new JFrame("Result");
        resultFrame.setSize(400, 600);
        resultFrame.setLayout(new GridLayout(18, 1));
        resultFrame.setLocationRelativeTo(this);

        int mathMarks = Integer.parseInt(mathField.getText().trim());
        int physicsMarks = Integer.parseInt(physicsField.getText().trim());
        int chemMarks = Integer.parseInt(chemField.getText().trim());
        int engMarks = Integer.parseInt(engField.getText().trim());
        int javaMarks = Integer.parseInt(javaField.getText().trim());
        int pythonMarks = Integer.parseInt(pythonField.getText().trim());

        resultFrame.add(new JLabel("Student Name: " + name));
        resultFrame.add(new JLabel("Roll Number: " + roll));
        resultFrame.add(new JLabel("Maths Marks: " + mathMarks + " | Grade: " + getSubjectGrade(mathMarks)));
        resultFrame.add(new JLabel("Physics Marks: " + physicsMarks + " | Grade: " + getSubjectGrade(physicsMarks)));
        resultFrame.add(new JLabel("Chemistry Marks: " + chemMarks + " | Grade: " + getSubjectGrade(chemMarks)));
        resultFrame.add(new JLabel("English Marks: " + engMarks + " | Grade: " + getSubjectGrade(engMarks)));
        resultFrame.add(new JLabel("Java Marks: " + javaMarks + " | Grade: " + getSubjectGrade(javaMarks)));
        resultFrame.add(new JLabel("Python Marks: " + pythonMarks + " | Grade: " + getSubjectGrade(pythonMarks)));
        resultFrame.add(new JLabel("Total Marks: " + total));
        resultFrame.add(new JLabel("Average Marks: " + String.format("%.2f", avg)));
        resultFrame.add(new JLabel("Overall Grade: " + grade));
        resultFrame.add(new JLabel("Remark: " + remark));

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> resultFrame.dispose());
        resultFrame.add(new JLabel());
        resultFrame.add(closeBtn);

        resultFrame.setVisible(true);
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentPerformanceGUI::new);
    }
}
