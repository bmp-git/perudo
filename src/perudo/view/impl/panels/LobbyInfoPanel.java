package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.UserType;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.Icon;
import perudo.view.impl.StandardGUIFactory;

public class LobbyInfoPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ENTER_LOBBY = "Enter lobby";
	private static final String EXIT_LOBBY = "Exit lobby";
	private static final String START_LOBBY = "Start lobby";
	private static final String EMPTY_SPACE = "Empty space";
	private static final String BOT_SPACE = "Add bot";

	private static final int MINUMUM_PLAYERS = 2;
	private static final int TITLE_SIZE = 30;
	private static final int LEFT_RIGHT_PADDING = 20;
	private static final int VERTICAL_PADDING = 10;
	private static final int PLAYER_BOX_PADDING = 7;

	private final GUIFactory factory;
	private Lobby lobby;
	private final User user;
	private final JButton btnEnterLobby;
	private final JButton btnExitLobby;
	private final JButton btnStartLobby;

	private final JPanel pnlCenter;
	private final JPanel pnlPlayers;
	private final JPanel pnlButtons;
	private final JPanel pnlInfo;

	public LobbyInfoPanel(final Lobby lobby, final User user) {
		super();
		this.setLayout(new BorderLayout());
		this.factory = new StandardGUIFactory();
		this.lobby = lobby;
		this.user = user;
		this.pnlCenter = this.factory.createPanel(new BorderLayout());
		this.pnlButtons = this.factory.createPanel(new FlowLayout());
		this.pnlInfo = this.factory.createPanel();
		this.pnlInfo.setLayout(new BoxLayout(this.pnlInfo, BoxLayout.Y_AXIS));
		this.pnlInfo.setBorder(BorderFactory.createEmptyBorder(0, LEFT_RIGHT_PADDING, 0, LEFT_RIGHT_PADDING));
		this.pnlPlayers = this.factory.createPanel();
		this.pnlPlayers.setLayout(new BoxLayout(this.pnlPlayers, BoxLayout.Y_AXIS));
		this.pnlPlayers.setBorder(BorderFactory.createEmptyBorder(0, LEFT_RIGHT_PADDING, 0, LEFT_RIGHT_PADDING));
		JLabel name = (JLabel) factory.createLabel(this.lobby.getInfo().getName(), Color.GREEN);
		name.setFont(new Font("Consolas", Font.PLAIN, TITLE_SIZE));
		JLabel lbl = (JLabel) this.factory.createLabel(this.lobby.getOwner().getName(), Icon.OWNER.getIcon(),
				JLabel.RIGHT);
		lbl.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
		this.pnlPlayers.add(lbl);
		this.pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));

		this.lobby.getUsers().forEach(u -> {
			if (!u.equals(this.lobby.getOwner())) {
				JLabel label = (JLabel) this.factory.createLabel(u.getName());
				label.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
				this.pnlPlayers.add(label);
				this.pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
			}
		});
		for (int i = 0; i < this.lobby.getFreeSpace(); i++) {
			if (!this.user.equals(this.lobby.getOwner())) {
				JLabel label = (JLabel) this.factory.createLabel(EMPTY_SPACE);
				label.setBorder(this.factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
				this.pnlPlayers.add(label);
				this.pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
			} else {
				JLabel label = (JLabel) this.factory.createLabel(BOT_SPACE, Icon.PLUS.getIcon(), JLabel.RIGHT);
				class ML implements MouseListener {
					@Override
					public void mouseClicked(MouseEvent e) {
						ControllerSingleton.getController().addBotToLobby(user, lobby, UserType.BOT_EASY);
					}

					@Override
					public void mousePressed(MouseEvent e) {
						ControllerSingleton.getController().addBotToLobby(user, lobby, UserType.BOT_EASY);
					}

					@Override
					public void mouseReleased(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						((JLabel) e.getSource()).setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createLineBorder(Color.GRAY), new EmptyBorder(PLAYER_BOX_PADDING,
										PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING)));
						setCursor(new Cursor(Cursor.HAND_CURSOR));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						((JLabel) e.getSource()).setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createLineBorder(Color.BLACK), new EmptyBorder(PLAYER_BOX_PADDING,
										PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING)));
						setCursor(Cursor.getDefaultCursor());
					}

				}
				label.addMouseListener(new ML());
				label.setBorder(this.factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
				this.pnlPlayers.add(label);
				this.pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
			}

		}

		this.pnlInfo.add(this.factory.createLabel(String.valueOf(this.lobby.getInfo().getMaxPlayer()),
				Icon.PLAYER.getIcon(), JLabel.RIGHT));
		this.pnlInfo.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
		this.pnlInfo.add(this.factory.createLabel(String.valueOf(this.lobby.getInfo().getInitialDiceNumber()),
				Icon.DICE.getIcon(), JLabel.RIGHT));
		this.pnlInfo.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
		this.pnlInfo.add(this.factory.createLabel(String.valueOf(this.lobby.getInfo().getMaxTurnTime().getSeconds()),
				Icon.TIME.getIcon(), JLabel.RIGHT));
		this.pnlInfo.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));

		this.btnEnterLobby = (JButton) factory.createButton(ENTER_LOBBY);
		this.btnEnterLobby.addActionListener(e -> {
			ControllerSingleton.getController().joinLobby(this.user, this.lobby);
		});
		this.pnlButtons.add(this.btnEnterLobby);
		this.btnExitLobby = (JButton) factory.createButton(EXIT_LOBBY,Icon.EXIT.getIcon());
		this.btnExitLobby.addActionListener(e -> {
			ControllerSingleton.getController().exitLobby(this.user);
		});
		this.pnlButtons.add(this.btnExitLobby);
		this.btnStartLobby = (JButton) factory.createButton(START_LOBBY);
		this.btnStartLobby.addActionListener(e -> {
			ControllerSingleton.getController().startLobby(this.user);
		});
		this.pnlButtons.add(this.btnStartLobby);
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

		JPanel ptop = this.factory.createPanel(new FlowLayout());
		ptop.add(name);
		this.pnlCenter.add(ptop, BorderLayout.PAGE_START);
		this.pnlCenter.add(this.pnlPlayers, BorderLayout.CENTER);
		this.pnlCenter.add(this.pnlInfo, BorderLayout.EAST);
		this.add(new JScrollPane(this.pnlCenter), BorderLayout.CENTER);
		this.add(this.pnlButtons, BorderLayout.SOUTH);
	}

	public Lobby getLobby() {
		return this.lobby;
	}

	public void setLobby(final Lobby lobby) {
		this.lobby = lobby;
	}

}
