package com.college.scms;

import com.college.scms.persistence.DataStore;
import com.college.scms.service.CampusService;
import com.college.scms.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // System look and feel is optional.
        }

        Path dataDirectory = Path.of("StudentCourseManagementSystem", "data");
        CampusService service = new CampusService(new DataStore(dataDirectory));

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(service);
            frame.setVisible(true);
        });
    }
}
