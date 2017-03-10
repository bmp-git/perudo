package perudo.view.impl.panels;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import perudo.model.Lobby;
import perudo.view.GUIFactory;
import perudo.view.impl.GUIFactorySingleton;
import perudo.view.impl.Icon;

/**
 * Panel rappresenting a lobby.
 */
public class LobbyPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int ROWS = 2;
    private static final int COLUMNS = 1;
    private static final int VERTICAL_GAP = 5;
    private static final int BORDER_PADDING = 7;

    private Lobby lobby;
    private final JLabel lblLobby;
    private final JLabel lblPlayers;

    /**
     * Initialize the panel with the lobby.
     * 
     * @param lobby
     *            the lobby to set to create the panel
     */
    public LobbyPanel(final Lobby lobby) {
        this.lobby = lobby;
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.setLayout(new GridLayout(ROWS, COLUMNS, 0, VERTICAL_GAP));
        this.lblLobby = (JLabel) factory.createLabel((this.lobby.getInfo().getName()));
        final int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
        this.lblPlayers = (JLabel) factory.createLabel(players + "/" + this.lobby.getInfo().getMaxPlayer(),
                Icon.PLAYER.getIcon(), JLabel.LEFT);

        this.setBorder(factory.createBorder(Color.BLACK, BORDER_PADDING));
        this.add(this.lblLobby);
        this.add(this.lblPlayers);

    }

    /**
     * Get the lobby in panel.
     * 
     * @return the lobby of panel
     */
    public Lobby getLobby() {
        return this.lobby;
    }

    /**
     * Set the lobby in panel and update it.
     * 
     * @param lobby
     *            the lobby to set in the panel
     */
    public void setLobby(final Lobby lobby) {
        this.lobby = lobby;
        this.lblLobby.setText((this.lobby.getInfo().getName()));
        final int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
        this.lblPlayers.setText(players + "/" + this.lobby.getInfo().getMaxPlayer());
    }
}
