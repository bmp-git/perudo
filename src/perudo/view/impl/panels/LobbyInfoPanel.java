package perudo.view.impl.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.StandardGUIFactory;

public class LobbyInfoPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ENTER_LOBBY = "Enter lobby";
    private static final String EXIT_LOBBY = "Exit lobby";
    private static final String START_LOBBY = "Start lobby";
    private static final int MINUMUM_PLAYERS = 2;

    private final GUIFactory factory;
    private Lobby lobby;
    private final User user;
    private final JButton btnEnterLobby;
    private final JButton btnExitLobby;
    private final JButton btnStartLobby;
    
    public LobbyInfoPanel(final Lobby lobby, final User user) {
        this.factory = new StandardGUIFactory();
        this.lobby = lobby;
        this.user = user;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel name = (JLabel) factory.createLabel(this.lobby.getInfo().getName());
        this.add(name);
        this.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lbl = new JLabel(this.lobby.getOwner().getName(),
                new ImageIcon(StandardGUIFactory.class.getResource("/images/crown_gold.png")), JLabel.RIGHT);
        lbl.setBorder(factory.createBorder(Color.BLACK, 7));
        this.add(lbl);
        this.add(Box.createRigidArea(new Dimension(0, 10)));

        this.lobby.getUsers().forEach(u -> {
            if (!u.equals(this.lobby.getOwner())) {
                JLabel label = new JLabel(u.getName());
                label.setBorder(factory.createBorder(Color.BLACK, 7));
                this.add(label);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        });
        for (int i = 0; i < this.lobby.getFreeSpace(); i++) {
            if (!this.user.equals(this.lobby.getOwner())) {
                JLabel label = new JLabel("Empty space.");
                label.setBorder(this.factory.createBorder(Color.BLACK, 7));
                this.add(label);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
            } else {
                JLabel label = new JLabel("Add bot +");
                class ML implements MouseListener {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Add bot
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        // Add bot
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        ((JLabel) e.getSource()).setBorder(BorderFactory
                                .createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), new EmptyBorder(7, 7, 7, 7)));
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        ((JLabel) e.getSource()).setBorder(BorderFactory
                                .createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), new EmptyBorder(7, 7, 7, 7)));
                        setCursor(Cursor.getDefaultCursor());
                    }

                }
                label.addMouseListener(new ML());
                label.setBorder(this.factory.createBorder(Color.BLACK, 7));
                this.add(label);
                this.add(Box.createRigidArea(new Dimension(0, 10)));
            }

        }
        
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(this.lobby.getInfo().toString()));
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.btnEnterLobby = (JButton) factory.createButton(ENTER_LOBBY);
        this.btnEnterLobby.addActionListener(e -> {
            ControllerSingleton.getController().joinLobby(this.user, this.lobby);
        });
        this.add(this.btnEnterLobby);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.btnExitLobby = (JButton) factory.createButton(EXIT_LOBBY);
        this.btnExitLobby.addActionListener(e -> {
            ControllerSingleton.getController().exitLobby(this.user);
        });
        this.add(this.btnExitLobby);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.btnStartLobby = (JButton) factory.createButton(START_LOBBY);
        this.btnStartLobby.addActionListener(e -> {
            ControllerSingleton.getController().startLobby(this.user);
        });
        this.add(this.btnStartLobby);
        if (this.lobby.getOwner().equals(this.user) && this.lobby.getUsers().size() >= MINUMUM_PLAYERS) {
            this.btnStartLobby.setEnabled(true);
        } else {
            this.btnStartLobby.setEnabled(false);
        }
        if (this.lobby.getUsers().contains(this.user)) {
            this.btnExitLobby.setEnabled(true);
            this.btnEnterLobby.setEnabled(false);
        } else {
            this.btnExitLobby.setEnabled(false);
            this.btnEnterLobby.setEnabled(true);
        }
    }

    public Lobby getLobby() {
        return this.lobby;
    }

    public void setLobby(final Lobby lobby) {
        this.lobby = lobby;
    }

}
