package pgdp.searchengine.gui.view;

import pgdp.searchengine.pagerepository.AbstractLinkedDocument;

import javax.swing.*;

public abstract class AbstractDocumentPane extends JPanel {
    protected JLabel idLabel;
    protected JLabel addressLabel;

    public AbstractDocumentPane(int id, String address) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        idLabel = new JLabel();
        addressLabel = new JLabel();

        setId(id);
        setAddress(address);

        add(idLabel);
        add(addressLabel);

        setBorder(BorderFactory.createEtchedBorder());
    }

    public void setId(int id) {
        idLabel.setText("ID: " + id);
    }

    public void setAddress(String address) {
        addressLabel.setText(address);
    }
}
