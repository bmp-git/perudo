package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.GUIFactorySingleton;
import perudo.view.impl.GUIUtility;

/**
 * Panel rappresenting a list of lobby panels.
 */
public class LobbyListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int TOP_BOT_INSETS = 5;

    private final JPanel pnlLobbyList;
    private final GridBagConstraints cnst;
    private Optional<User> user;

    /**
     * Initialize the list panel.
     */
    public LobbyListPanel() {
        this.setLayout(new BorderLayout());
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.user = Optional.empty();
        final CreateLobbyPanel pnlCreateLobby = new CreateLobbyPanel();
        final ChangeNamePanel pnlChangeName = new ChangeNamePanel();

        this.pnlLobbyList = factory.createPanel();
        this.pnlLobbyList.setLayout(new GridBagLayout());
        final JPanel pnlExtern = factory.createPanel();
        pnlExtern.setLayout(new FlowLayout());
        pnlExtern.add(this.pnlLobbyList);
        pnlExtern.setBorder(new TitledBorder("Lobbies list"));
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(TOP_BOT_INSETS, 0, TOP_BOT_INSETS, 0);

        final JPanel paneldown = factory.createPanel();
        paneldown.setLayout(new GridLayout(2, 1));
        final JButton btnCreateLobby = (JButton) factory.createButton("Create new lobby");
        btnCreateLobby.addActionListener(e -> {
            final int n = JOptionPane.showConfirmDialog(this, pnlCreateLobby, CreateLobbyPanel.TITLE,
                    JOptionPane.OK_CANCEL_OPTION, 0, GUIUtility.getIcon(CreateLobbyPanel.ICON_RESPATH));
            if (n == JOptionPane.YES_OPTION && this.user.isPresent() && pnlCreateLobby.getName().trim().length() > 0) {
                ControllerSingleton.getController().createLobby(this.user.get(), pnlCreateLobby.getGameSettings());
            }
        });
        paneldown.add(btnCreateLobby);
        final JButton btnChangeUsername = (JButton) factory.createButton("Change username");
        btnChangeUsername.addActionListener(e -> {
            final int n = JOptionPane.showConfirmDialog(this, pnlChangeName, ChangeNamePanel.TITLE, JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.YES_OPTION && this.user.isPresent() && pnlChangeName.getName().trim().length() > 0) {
                ControllerSingleton.getController().changeUserName(this.user.get(), pnlChangeName.getName());
            }
        });
        paneldown.add(btnChangeUsername);
        this.add(factory.createScrollPaneWithoutBorder(pnlExtern), BorderLayout.CENTER);
        this.add(paneldown, BorderLayout.SOUTH);

    }

    /**
     * Set the user using panel.
     * 
     * @param user
     *            the user usign panel
     */
    public void setUser(final User user) {
        this.user = Optional.of(user);
    }

    /**
     * Add lobby to list.
     * 
     * @param lobby
     *            the lobby to list
     */
    public void addLobby(final LobbyPanel lobby) {
        this.pnlLobbyList.add(lobby, cnst);
        this.cnst.gridy++;
    }

    /**
     * Update the gived lobby.
     * 
     * @param lobby
     *            the lobby to update
     */
    public void updateLobby(final Lobby lobby) {
        for (int i = 0; i < this.pnlLobbyList.getComponentCount(); i++) {
            if (((LobbyPanel) this.pnlLobbyList.getComponent(i)).getLobby().equals(lobby)) {
                ((LobbyPanel) this.pnlLobbyList.getComponent(i)).setLobby(lobby);
            }
        }
    }

    /**
     * Remove the lobby.
     * 
     * @param lobby
     *            the lobby to remove
     */
    public void removeLobby(final Lobby lobby) {
        for (int i = 0; i < this.pnlLobbyList.getComponentCount(); i++) {
            if (((LobbyPanel) this.pnlLobbyList.getComponent(i)).getLobby().equals(lobby)) {
                this.pnlLobbyList.remove(this.pnlLobbyList.getComponent(i));
                break;
            }
        }
    }

    /**
     * Get the list of lobbies.
     * 
     * @return the list of lobbies
     */
    public List<Lobby> getLobbies() {
        final List<Lobby> l = new ArrayList<>();
        for (int i = 0; i < this.pnlLobbyList.getComponentCount(); i++) {
            l.add(((LobbyPanel) this.pnlLobbyList.getComponent(i)).getLobby());
        }
        return l;
    }

}
