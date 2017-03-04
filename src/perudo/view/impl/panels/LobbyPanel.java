package perudo.view.impl.panels;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import perudo.model.Lobby;
import perudo.view.GUIFactory;
import perudo.view.impl.Icon;
import perudo.view.impl.StandardGUIFactory;

public class LobbyPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int ROWS = 2;
	private static final int COLUMNS = 1;
	private static final int VERTICAL_GAP = 5;
	private static final int BORDER_PADDING = 7;

	private final GUIFactory factory;
	private Lobby lobby;
	private final JLabel lblLobby;
	private final JLabel lblPlayers;

	public LobbyPanel(final Lobby lobby) {
		this.lobby = lobby;
		this.factory = new StandardGUIFactory();
		this.setLayout(new GridLayout(ROWS, COLUMNS, 0, VERTICAL_GAP));
		this.lblLobby = (JLabel) this.factory.createLabel((this.lobby.getInfo().getName()));
		int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
		this.lblPlayers = (JLabel) this.factory.createLabel(players + "/" + this.lobby.getInfo().getMaxPlayer(),
				Icon.PLAYER.getIcon(), JLabel.LEFT);

		this.setBorder(factory.createBorder(Color.BLACK, BORDER_PADDING));
		this.add(this.lblLobby);
		this.add(this.lblPlayers);

	}

	public Lobby getLobby() {
		return this.lobby;
	}

	public void setLobby(final Lobby lobby) {
		this.lobby = lobby;
		this.lblLobby.setText((this.lobby.getInfo().getName()));
		int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
		this.lblPlayers.setText(players + "/" + this.lobby.getInfo().getMaxPlayer());
	}
}
