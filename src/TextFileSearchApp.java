import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TextFileSearchApp {
    private List<JTextArea> textAreas = new ArrayList<>();
    private JPanel textAreaPanel;
    private JTextArea searchResultsArea;

    public TextFileSearchApp() {
        JFrame frame = new JFrame("Multi Text File Loader App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton loadButton = new JButton("Load Text File");
        JButton clearButton = new JButton("Clear All");
        JButton quitButton = new JButton("Quit");

        textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JScrollPane scrollPane = new JScrollPane(textAreaPanel);

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchResultsArea = new JTextArea(20, 40);
        searchResultsArea.setEditable(false); // Make it read-only

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        Path filePath = fileChooser.getSelectedFile().toPath();
                        String fileContent = Files.readString(filePath);
                        JTextArea textArea = new JTextArea(20, 40);
                        textArea.setText(fileContent);
                        textAreas.add(textArea);
                        textAreaPanel.add(textArea);
                        frame.revalidate();
                        frame.repaint();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error loading the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchString = searchField.getText();
                searchResultsArea.setText(""); // Clear previous search results

                for (JTextArea textArea : textAreas) {
                    String text = textArea.getText();
                    try (Stream<String> lines = text.lines()) {
                        lines.filter(line -> line.contains(searchString))
                                .forEach(line -> searchResultsArea.append(line + "\n"));
                    }
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreas.clear();
                textAreaPanel.removeAll();
                frame.revalidate();
                frame.repaint();
                searchResultsArea.setText(""); // Clear search results
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(searchResultsArea, BorderLayout.EAST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TextFileSearchApp();
        });
    }
}