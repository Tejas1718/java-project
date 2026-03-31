package com.college.scms.ui;

import com.college.scms.model.Course;
import com.college.scms.model.Student;
import com.college.scms.service.CampusService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class MainFrame extends JFrame {
    private static final Color BACKGROUND = new Color(245, 247, 252);
    private static final Color PANEL = Color.WHITE;
    private static final Color PRIMARY = new Color(18, 92, 173);
    private static final Color ACCENT = new Color(17, 153, 142);
    private static final Color TEXT = new Color(34, 45, 65);
    private static final Color MUTED = new Color(114, 126, 149);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font CARD_NUMBER_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font CARD_LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    private final CampusService service;
    private final DefaultTableModel studentModel;
    private final DefaultTableModel courseModel;
    private final DefaultTableModel enrollmentModel;
    private final JTextArea dashboardArea;
    private final JComboBox<String> enrollmentStudentBox;
    private final JComboBox<String> enrollmentCourseBox;

    private final JLabel totalStudentsValue;
    private final JLabel totalCoursesValue;
    private final JLabel totalEnrollmentsValue;
    private final JLabel averageMarksValue;

    public MainFrame(CampusService service) {
        this.service = service;
        applyLookAndFeelDefaults();

        setTitle("Student Course & Performance Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 760);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);

        studentModel = createTableModel(new String[]{"ID", "Name", "Department", "Semester", "Email"});
        courseModel = createTableModel(new String[]{"ID", "Title", "Credits", "Instructor", "Capacity"});
        enrollmentModel = createTableModel(new String[]{"Student ID", "Student", "Course ID", "Course", "Marks", "Attendance", "Grade"});

        dashboardArea = new JTextArea();
        dashboardArea.setEditable(false);
        dashboardArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        dashboardArea.setLineWrap(true);
        dashboardArea.setWrapStyleWord(true);
        dashboardArea.setBackground(PANEL);
        dashboardArea.setForeground(TEXT);
        dashboardArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        enrollmentStudentBox = new JComboBox<>();
        enrollmentCourseBox = new JComboBox<>();
        styleComboBox(enrollmentStudentBox);
        styleComboBox(enrollmentCourseBox);

        totalStudentsValue = createCardValue();
        totalCoursesValue = createCardValue();
        totalEnrollmentsValue = createCardValue();
        averageMarksValue = createCardValue();

        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        root.add(createHeroPanel(), BorderLayout.NORTH);
        root.add(createTabs(), BorderLayout.CENTER);
        add(root);

        refreshAllData();
    }

    private void applyLookAndFeelDefaults() {
        UIManager.put("TabbedPane.selected", PANEL);
        UIManager.put("TabbedPane.contentAreaColor", BACKGROUND);
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT);
    }

    private JPanel createHeroPanel() {
        JPanel hero = new GradientPanel(new Color(12, 62, 135), new Color(24, 133, 190));
        hero.setLayout(new BorderLayout(16, 16));
        hero.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Student Course & Performance Management System");
        title.setFont(TITLE_FONT);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("A polished Java Swing project for college demo, record editing, and performance tracking");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(220, 236, 255));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitle);
        titlePanel.add(Box.createVerticalStrut(14));
        titlePanel.add(createHeroTagRow());

        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 12));
        cards.setOpaque(false);
        cards.add(createStatCard("Students", totalStudentsValue, new Color(255, 255, 255, 58)));
        cards.add(createStatCard("Courses", totalCoursesValue, new Color(255, 255, 255, 58)));
        cards.add(createStatCard("Enrollments", totalEnrollmentsValue, new Color(255, 255, 255, 58)));
        cards.add(createStatCard("Avg. Marks", averageMarksValue, new Color(255, 255, 255, 58)));

        hero.add(titlePanel, BorderLayout.NORTH);
        hero.add(cards, BorderLayout.CENTER);
        return hero;
    }

    private JPanel createTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setBackground(PANEL);
        tabs.setForeground(TEXT);
        tabs.setBorder(BorderFactory.createEmptyBorder());
        tabs.setUI(new ModernTabbedPaneUI());
        tabs.addTab("Dashboard", createDashboardPanel());
        tabs.addTab("Students", createStudentsPanel());
        tabs.addTab("Courses", createCoursesPanel());
        tabs.addTab("Enrollments", createEnrollmentsPanel());

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BACKGROUND);
        wrapper.add(tabs, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 4, 4, 4));

        JPanel insights = new JPanel(new GridLayout(1, 2, 14, 14));
        insights.setBackground(BACKGROUND);
        insights.add(createInfoCard(
                "Project Highlights",
                "This system manages students, courses, enrollments, marks, attendance, and grades in one desktop application."
        ));
        insights.add(createInfoCard(
                "Demo Flow",
                "Show dashboard metrics, open Students tab to update a record, then edit enrollment marks to demonstrate live analytics."
        ));

        JPanel reportPanel = createSectionPanel("Performance Summary");
        reportPanel.add(new JScrollPane(dashboardArea), BorderLayout.CENTER);

        JButton refreshButton = createPrimaryButton("Refresh Dashboard");
        refreshButton.addActionListener(e -> refreshDashboard());

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(PANEL);
        bottomBar.add(refreshButton, BorderLayout.EAST);
        reportPanel.add(bottomBar, BorderLayout.SOUTH);

        panel.add(insights, BorderLayout.NORTH);
        panel.add(reportPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 4, 4, 4));

        JTable table = createStyledTable(studentModel);

        JTextField idField = createTextField();
        JTextField nameField = createTextField();
        JTextField departmentField = createTextField();
        JTextField semesterField = createTextField();
        JTextField emailField = createTextField();

        JPanel form = createSectionPanel("Student Form");
        JPanel fields = createVerticalForm();
        fields.add(createLabeledField("Student ID", idField));
        fields.add(createLabeledField("Full Name", nameField));
        fields.add(createLabeledField("Department", departmentField));
        fields.add(createLabeledField("Semester", semesterField));
        fields.add(createLabeledField("Email", emailField));
        form.add(createFormScrollPane(fields), BorderLayout.CENTER);
        form.setPreferredSize(new Dimension(400, 0));

        JButton loadButton = createNeutralButton("Load Selected");
        loadButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                showError("Select a student row to load.");
                return;
            }
            idField.setText(studentModel.getValueAt(row, 0).toString());
            nameField.setText(studentModel.getValueAt(row, 1).toString());
            departmentField.setText(studentModel.getValueAt(row, 2).toString());
            semesterField.setText(studentModel.getValueAt(row, 3).toString());
            emailField.setText(studentModel.getValueAt(row, 4).toString());
            idField.setEditable(false);
        });

        JButton addButton = createPrimaryButton("Add Student");
        addButton.addActionListener(e -> {
            try {
                int semester = parseRequiredInt(semesterField, "Semester");
                service.addStudent(new Student(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        departmentField.getText().trim(),
                        semester,
                        emailField.getText().trim()
                ));
                clearStudentForm(idField, nameField, departmentField, semesterField, emailField);
                refreshAllData();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        JButton updateButton = createAccentButton("Update Student");
        updateButton.addActionListener(e -> {
            try {
                int semester = parseRequiredInt(semesterField, "Semester");
                service.updateStudent(new Student(
                        idField.getText().trim(),
                        nameField.getText().trim(),
                        departmentField.getText().trim(),
                        semester,
                        emailField.getText().trim()
                ));
                clearStudentForm(idField, nameField, departmentField, semesterField, emailField);
                refreshAllData();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        JButton clearButton = createNeutralButton("Clear Form");
        clearButton.addActionListener(e -> clearStudentForm(idField, nameField, departmentField, semesterField, emailField));

        JButton deleteButton = createDangerButton("Delete Selected");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                showError("Select a student row to delete.");
                return;
            }
            service.deleteStudent(studentModel.getValueAt(row, 0).toString());
            clearStudentForm(idField, nameField, departmentField, semesterField, emailField);
            refreshAllData();
        });

        JPanel actions = createActionGrid(loadButton, addButton, updateButton, clearButton, deleteButton);
        form.add(actions, BorderLayout.SOUTH);

        panel.add(form, BorderLayout.WEST);
        panel.add(createTablePanel("Student Records", table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 4, 4, 4));

        JTable table = createStyledTable(courseModel);

        JTextField idField = createTextField();
        JTextField titleField = createTextField();
        JTextField creditsField = createTextField();
        JTextField instructorField = createTextField();
        JTextField capacityField = createTextField();

        JPanel form = createSectionPanel("Course Form");
        JPanel fields = createVerticalForm();
        fields.add(createLabeledField("Course ID", idField));
        fields.add(createLabeledField("Course Title", titleField));
        fields.add(createLabeledField("Credits", creditsField));
        fields.add(createLabeledField("Instructor", instructorField));
        fields.add(createLabeledField("Capacity", capacityField));
        form.add(createFormScrollPane(fields), BorderLayout.CENTER);
        form.setPreferredSize(new Dimension(400, 0));

        JButton loadButton = createNeutralButton("Load Selected");
        loadButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                showError("Select a course row to load.");
                return;
            }
            idField.setText(courseModel.getValueAt(row, 0).toString());
            titleField.setText(courseModel.getValueAt(row, 1).toString());
            creditsField.setText(courseModel.getValueAt(row, 2).toString());
            instructorField.setText(courseModel.getValueAt(row, 3).toString());
            capacityField.setText(courseModel.getValueAt(row, 4).toString());
            idField.setEditable(false);
        });

        JButton addButton = createPrimaryButton("Add Course");
        addButton.addActionListener(e -> {
            try {
                int credits = parseRequiredInt(creditsField, "Credits");
                int capacity = parseRequiredInt(capacityField, "Capacity");
                service.addCourse(new Course(
                        idField.getText().trim(),
                        titleField.getText().trim(),
                        credits,
                        instructorField.getText().trim(),
                        capacity
                ));
                clearCourseForm(idField, titleField, creditsField, instructorField, capacityField);
                refreshAllData();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        JButton updateButton = createAccentButton("Update Course");
        updateButton.addActionListener(e -> {
            try {
                int credits = parseRequiredInt(creditsField, "Credits");
                int capacity = parseRequiredInt(capacityField, "Capacity");
                service.updateCourse(new Course(
                        idField.getText().trim(),
                        titleField.getText().trim(),
                        credits,
                        instructorField.getText().trim(),
                        capacity
                ));
                clearCourseForm(idField, titleField, creditsField, instructorField, capacityField);
                refreshAllData();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        JButton clearButton = createNeutralButton("Clear Form");
        clearButton.addActionListener(e -> clearCourseForm(idField, titleField, creditsField, instructorField, capacityField));

        JButton deleteButton = createDangerButton("Delete Selected");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                showError("Select a course row to delete.");
                return;
            }
            service.deleteCourse(courseModel.getValueAt(row, 0).toString());
            clearCourseForm(idField, titleField, creditsField, instructorField, capacityField);
            refreshAllData();
        });

        JPanel actions = createActionGrid(loadButton, addButton, updateButton, clearButton, deleteButton);
        form.add(actions, BorderLayout.SOUTH);

        panel.add(form, BorderLayout.WEST);
        panel.add(createTablePanel("Course Records", table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createEnrollmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 4, 4, 4));

        JTable table = createStyledTable(enrollmentModel);

        JTextField marksField = createTextField();
        JTextField attendanceField = createTextField();

        JPanel form = createSectionPanel("Enrollment Form");
        JPanel fields = createVerticalForm();
        fields.add(createLabeledField("Student", enrollmentStudentBox));
        fields.add(createLabeledField("Course", enrollmentCourseBox));
        fields.add(createLabeledField("Marks", marksField));
        fields.add(createLabeledField("Attendance %", attendanceField));
        form.add(createFormScrollPane(fields), BorderLayout.CENTER);
        form.setPreferredSize(new Dimension(400, 0));

        JButton loadButton = createNeutralButton("Load Selected");
        loadButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                showError("Select an enrollment row to load.");
                return;
            }
            selectComboValue(enrollmentStudentBox, enrollmentModel.getValueAt(row, 0).toString());
            selectComboValue(enrollmentCourseBox, enrollmentModel.getValueAt(row, 2).toString());
            marksField.setText(enrollmentModel.getValueAt(row, 4).toString());
            attendanceField.setText(enrollmentModel.getValueAt(row, 5).toString());
        });

        JButton addButton = createPrimaryButton("Add Enrollment");
        addButton.addActionListener(e -> {
            try {
                double marks = parseRequiredDouble(marksField, "Marks");
                double attendance = parseRequiredDouble(attendanceField, "Attendance");
                service.addEnrollment(
                        parseSelection(enrollmentStudentBox),
                        parseSelection(enrollmentCourseBox),
                        marks,
                        attendance
                );
                clearEnrollmentForm(marksField, attendanceField);
                refreshAllData();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        JButton updateButton = createAccentButton("Update Enrollment");
        updateButton.addActionListener(e -> {
            try {
                double marks = parseRequiredDouble(marksField, "Marks");
                double attendance = parseRequiredDouble(attendanceField, "Attendance");
                service.updateEnrollment(
                        parseSelection(enrollmentStudentBox),
                        parseSelection(enrollmentCourseBox),
                        marks,
                        attendance
                );
                clearEnrollmentForm(marksField, attendanceField);
                refreshAllData();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        JButton clearButton = createNeutralButton("Clear Form");
        clearButton.addActionListener(e -> clearEnrollmentForm(marksField, attendanceField));

        JButton deleteButton = createDangerButton("Delete Selected");
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                showError("Select an enrollment row to delete.");
                return;
            }
            service.deleteEnrollment(
                    enrollmentModel.getValueAt(row, 0).toString(),
                    enrollmentModel.getValueAt(row, 2).toString()
            );
            clearEnrollmentForm(marksField, attendanceField);
            refreshAllData();
        });

        JPanel actions = createActionGrid(loadButton, addButton, updateButton, clearButton, deleteButton);
        form.add(actions, BorderLayout.SOUTH);

        panel.add(form, BorderLayout.WEST);
        panel.add(createTablePanel("Enrollment Records", table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTablePanel(String title, JTable table) {
        JPanel panel = createSectionPanel(title);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new RoundedPanel(24, PANEL);
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel headingWrap = new JPanel(new BorderLayout());
        headingWrap.setOpaque(false);

        JPanel accentBar = new JPanel();
        accentBar.setPreferredSize(new Dimension(58, 6));
        accentBar.setBackground(ACCENT);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(TEXT);

        headingWrap.add(accentBar, BorderLayout.NORTH);
        headingWrap.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        headingWrap.add(heading, BorderLayout.SOUTH);
        panel.add(headingWrap, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createInfoCard(String title, String body) {
        JPanel card = new RoundedPanel(24, new Color(252, 253, 255));
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 17));
        heading.setForeground(TEXT);

        JTextArea text = new JTextArea(body);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        text.setForeground(MUTED);
        text.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        card.add(heading, BorderLayout.NORTH);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color backgroundColor) {
        JPanel card = new RoundedPanel(26, backgroundColor);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(CARD_LABEL_FONT);
        labelComponent.setForeground(new Color(239, 247, 255));
        labelComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(labelComponent);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        return card;
    }

    private JLabel createCardValue() {
        JLabel label = new JLabel("0");
        label.setFont(CARD_NUMBER_FONT);
        label.setForeground(TEXT);
        return label;
    }

    private JPanel createVerticalForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        return panel;
    }

    private JScrollPane createFormScrollPane(JPanel fields) {
        JScrollPane scrollPane = new JScrollPane(fields);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createLabeledField(String labelText, Component component) {
        JPanel wrapper = new JPanel(new BorderLayout(4, 6));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT);

        component.setPreferredSize(new Dimension(290, 36));
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(component, BorderLayout.CENTER);
        return wrapper;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMargin(new Insets(6, 10, 6, 10));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(198, 208, 223)),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        field.setBackground(new Color(251, 252, 255));
        return field;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(new Color(251, 252, 255));
        box.setBorder(BorderFactory.createLineBorder(new Color(198, 208, 223)));
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setGridColor(new Color(232, 236, 242));
        table.setSelectionBackground(new Color(219, 234, 255));
        table.setSelectionForeground(TEXT);
        table.setShowVerticalLines(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultRenderer(Object.class, new SoftTableCellRenderer());

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(18, 92, 173));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createEmptyBorder());

        return table;
    }

    private DefaultTableModel createTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel createActionGrid(JButton... buttons) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setOpaque(false);
        for (JButton button : buttons) {
            panel.add(button);
        }
        if (buttons.length % 2 != 0) {
            panel.add(new JLabel());
        }
        return panel;
    }

    private JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY, Color.WHITE);
    }

    private JButton createAccentButton(String text) {
        return createButton(text, ACCENT, Color.WHITE);
    }

    private JButton createNeutralButton(String text) {
        return createButton(text, new Color(236, 240, 247), TEXT);
    }

    private JButton createDangerButton(String text) {
        return createButton(text, new Color(198, 61, 47), Color.WHITE);
    }

    private JButton createButton(String text, Color background, Color foreground) {
        JButton button = new ColorfulButton(text, background, foreground);
        button.setFocusPainted(false);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setMargin(new Insets(10, 12, 10, 12));
        button.setBorder(new EmptyBorder(10, 14, 10, 14));
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private void selectComboValue(JComboBox<String> box, String id) {
        for (int index = 0; index < box.getItemCount(); index++) {
            String item = box.getItemAt(index);
            if (item.startsWith(id + " - ")) {
                box.setSelectedIndex(index);
                return;
            }
        }
    }

    private String parseSelection(JComboBox<String> box) {
        Object selection = box.getSelectedItem();
        if (selection == null) {
            throw new IllegalArgumentException("Please select a value.");
        }
        return selection.toString().split(" - ", 2)[0];
    }

    private int parseRequiredInt(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " must be a valid whole number.");
        }
    }

    private double parseRequiredDouble(JTextField field, String fieldName) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    private void refreshAllData() {
        refreshStudents();
        refreshCourses();
        refreshEnrollments();
        refreshDashboard();
    }

    private void refreshStudents() {
        studentModel.setRowCount(0);
        List<Student> students = service.getStudents();
        for (Student student : students) {
            studentModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getDepartment(),
                    student.getSemester(),
                    student.getEmail()
            });
        }

        enrollmentStudentBox.removeAllItems();
        for (Student student : students) {
            enrollmentStudentBox.addItem(student.getId() + " - " + student.getName());
        }
    }

    private void refreshCourses() {
        courseModel.setRowCount(0);
        List<Course> courses = service.getCourses();
        for (Course course : courses) {
            courseModel.addRow(new Object[]{
                    course.getId(),
                    course.getTitle(),
                    course.getCredits(),
                    course.getInstructor(),
                    course.getCapacity()
            });
        }

        enrollmentCourseBox.removeAllItems();
        for (Course course : courses) {
            enrollmentCourseBox.addItem(course.getId() + " - " + course.getTitle());
        }
    }

    private void refreshEnrollments() {
        enrollmentModel.setRowCount(0);
        List<CampusService.EnrollmentView> views = service.getEnrollmentViews();
        for (CampusService.EnrollmentView view : views) {
            enrollmentModel.addRow(new Object[]{
                    view.studentId(),
                    view.studentName(),
                    view.courseId(),
                    view.courseTitle(),
                    String.format("%.2f", view.marks()),
                    String.format("%.2f", view.attendance()),
                    view.grade()
            });
        }
    }

    private void refreshDashboard() {
        CampusService.DashboardStats stats = service.getDashboardStats();
        totalStudentsValue.setText(String.valueOf(stats.totalStudents()));
        totalCoursesValue.setText(String.valueOf(stats.totalCourses()));
        totalEnrollmentsValue.setText(String.valueOf(stats.totalEnrollments()));
        averageMarksValue.setText(String.format("%.1f", stats.averageMarks()));

        StringBuilder builder = new StringBuilder();
        builder.append("ACADEMIC DASHBOARD\n\n");
        builder.append("Total Students      : ").append(stats.totalStudents()).append('\n');
        builder.append("Total Courses       : ").append(stats.totalCourses()).append('\n');
        builder.append("Total Enrollments   : ").append(stats.totalEnrollments()).append('\n');
        builder.append("Average Marks       : ").append(String.format("%.2f", stats.averageMarks())).append('\n');
        builder.append("Average Attendance  : ").append(String.format("%.2f", stats.averageAttendance())).append("%\n");
        builder.append("High Performers     : ").append(stats.highPerformers()).append('\n');
        builder.append("\nTOP PERFORMERS\n");
        builder.append("--------------\n");

        List<String> topPerformers = service.topPerformers();
        if (topPerformers.isEmpty()) {
            builder.append("No enrollments available yet.\n");
        } else {
            for (String performer : topPerformers) {
                builder.append(performer).append('\n');
            }
        }

        builder.append("\nPROJECT DEMO TIP\n");
        builder.append("----------------\n");
        builder.append("Use 'Load Selected' in any tab, edit the values, then press 'Update' to show live changes.\n");
        dashboardArea.setText(builder.toString());
    }

    private void clearStudentForm(JTextField idField, JTextField nameField, JTextField departmentField, JTextField semesterField, JTextField emailField) {
        clearFields(idField, nameField, departmentField, semesterField, emailField);
        idField.setEditable(true);
    }

    private void clearCourseForm(JTextField idField, JTextField titleField, JTextField creditsField, JTextField instructorField, JTextField capacityField) {
        clearFields(idField, titleField, creditsField, instructorField, capacityField);
        idField.setEditable(true);
    }

    private void clearEnrollmentForm(JTextField marksField, JTextField attendanceField) {
        clearFields(marksField, attendanceField);
        if (enrollmentStudentBox.getItemCount() > 0) {
            enrollmentStudentBox.setSelectedIndex(0);
        }
        if (enrollmentCourseBox.getItemCount() > 0) {
            enrollmentCourseBox.setSelectedIndex(0);
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Action Failed", JOptionPane.ERROR_MESSAGE);
    }

    private JPanel createHeroTagRow() {
        JPanel tags = new JPanel(new GridLayout(1, 3, 10, 10));
        tags.setOpaque(false);
        tags.add(createHeroTag("Java Swing UI", new Color(255, 255, 255, 38)));
        tags.add(createHeroTag("Editable Records", new Color(255, 255, 255, 32)));
        tags.add(createHeroTag("Live Analytics", new Color(255, 255, 255, 28)));
        return tags;
    }

    private JLabel createHeroTag(String text, Color bg) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setBorder(new EmptyBorder(8, 12, 8, 12));
        return label;
    }

    private static final class GradientPanel extends JPanel {
        private final Color start;
        private final Color end;

        private GradientPanel(Color start, Color end) {
            this.start = start;
            this.end = end;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Paint paint = new GradientPaint(0, 0, start, getWidth(), getHeight(), end);
            g2.setPaint(paint);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
            g2.setColor(new Color(255, 255, 255, 30));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 34, 34);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static final class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;

        private RoundedPanel(int radius, Color fill) {
            this.radius = radius;
            this.fill = fill;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.setColor(new Color(219, 226, 236));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1.0, getHeight() - 1.0, radius, radius));
            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static final class SoftTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                component.setBackground(new Color(219, 234, 255));
                component.setForeground(TEXT);
            } else {
                component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 250, 254));
                component.setForeground(TEXT);
            }
            if (component instanceof JLabel label) {
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
            }
            return component;
        }
    }

    private static final class ColorfulButton extends JButton {
        private final Color baseColor;

        private ColorfulButton(String text, Color baseColor, Color textColor) {
            super(text);
            this.baseColor = baseColor;
            setForeground(textColor);
            setFocusPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ButtonModel model = getModel();
            Color fill = baseColor;
            if (!isEnabled()) {
                fill = new Color(180, 188, 200);
            } else if (model.isPressed()) {
                fill = fill.darker();
            } else if (model.isRollover()) {
                fill = brighten(fill);
            }

            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 4, 18, 18);

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 6, 18, 18);

            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 7, 18, 18);

            g2.dispose();
            super.paintComponent(graphics);
        }

        @Override
        protected void paintBorder(Graphics graphics) {
        }

        private Color brighten(Color color) {
            int red = Math.min(255, color.getRed() + 18);
            int green = Math.min(255, color.getGreen() + 18);
            int blue = Math.min(255, color.getBlue() + 18);
            return new Color(red, green, blue);
        }
    }

    private static final class ModernTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected void installDefaults() {
            super.installDefaults();
            highlight = Color.WHITE;
            lightHighlight = Color.WHITE;
            shadow = new Color(210, 218, 229);
            darkShadow = new Color(210, 218, 229);
            focus = BACKGROUND;
            tabAreaInsets = new Insets(8, 8, 0, 8);
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isSelected ? PRIMARY : new Color(232, 238, 248));
            g2.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 18, 18);
            g2.dispose();
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        }
    }
}
