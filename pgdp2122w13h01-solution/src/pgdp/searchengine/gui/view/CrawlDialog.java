package pgdp.searchengine.gui.view;

import pgdp.searchengine.gui.controller.AdminController;

import javax.swing.*;
import java.awt.*;

public class CrawlDialog extends JDialog {
    private JPanel body;
    private JPanel buttons;

    public CrawlDialog(AdminController adminController) {
        setLayout(new BorderLayout());

        body = new JPanel();
        body.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        body.add(new JLabel("Amount"), constraints);

        constraints.gridy = 1;
        JSpinner numberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        numberSpinner.setPreferredSize(new Dimension(300, 30));
        body.add(numberSpinner, constraints);

        constraints.gridy = 2;
        body.add(new JLabel("Address"), constraints);

        constraints.gridy = 3;
        JTextField addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(300, 30));
        body.add(addressField, constraints);

        add(body, "Center");

        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        buttons.add(Box.createHorizontalGlue());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionEvent -> dispose());
        buttons.add(cancelButton);

        JButton crawlButton = new JButton("Crawl");
        crawlButton.addActionListener(actionEvent -> {
            adminController.crawlFromAddress((Integer) numberSpinner.getValue(), addressField.getText());
            dispose();
        });
        buttons.add(crawlButton);

        add(buttons, "South");
    }

}
