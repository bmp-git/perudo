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
import perudo.view.impl.GUIFactorySingleton;
import perudo.view.impl.Icon;

/**
 * Panel rappresenting the information of a lobby.
 */
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

    private final Lobby lobby;

    /**
     * Initialize and create the panel of the specified lobby and user.
     * 
     * @param lobby
     *            the lobby interested
     * @param user
     *            the user using panel
     */
    public LobbyInfoPanel(final Lobby lobby, final User user) {
        super();
        this.setLayout(new BorderLayout());
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.lobby = lobby;
        final JPanel pnlCenter = factory.createPanel(new BorderLayout());
        final JPanel pnlButtons = factory.createPanel(new FlowLayout());
        final JPanel pnlInfo = factory.createPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(0, LEFT_RIGHT_PADDING, 0, LEFT_RIGHT_PADDING));
        final JPanel pnlPlayers = factory.createPanel();
        pnlPlayers.setLayout(new BoxLayout(pnlPlayers, BoxLayout.Y_AXIS));
        pnlPlayers.setBorder(BorderFactory.createEmptyBorder(0, LEFT_RIGHT_PADDING, 0, LEFT_RIGHT_PADDING));
        final JLabel name = (JLabel) factory.createLabel(this.lobby.getInfo().getName(), Color.BLUE);
        name.setFont(new Font("Consolas", Font.PLAIN, TITLE_SIZE));
        final JLabel owner = (JLabel) factory.createLabel(this.lobby.getOwner().getName(), Icon.OWNER.getIcon(), JLabel.RIGHT);
        owner.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
        pnlPlayers.add(owner);
        pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));

        this.lobby.getUsers().forEach(u -> {
            if (!u.equals(this.lobby.getOwner())) {
                if (user.equals(this.lobby.getOwner()) && u.getType().isBot()) {
                    final JLabel playerbot = (JLabel) factory.createLabel(u.getName(), Icon.MINUS.getIcon(), JLabel.RIGHT);
                    class ML implements MouseListener {
                        @Override
                        public void mouseClicked(final MouseEvent event) {
                            ControllerSingleton.getController().closeNow(u);
                        }

                        @Override
                        public void mousePressed(final MouseEvent event) {
                            ControllerSingleton.getController().closeNow(u);
                        }

                        @Override
                        public void mouseReleased(final MouseEvent event) {
                        }

                        @Override
                        public void mouseEntered(final MouseEvent event) {
                            ((JLabel) event.getSource()).setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(Color.GRAY), new EmptyBorder(PLAYER_BOX_PADDING,
                                            PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING)));
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                        }

                        @Override
                        public void mouseExited(final MouseEvent event) {
                            ((JLabel) event.getSource()).setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(Color.BLACK), new EmptyBorder(PLAYER_BOX_PADDING,
                                            PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING)));
                            setCursor(Cursor.getDefaultCursor());
                        }

                    }
                    playerbot.addMouseListener(new ML());
                    playerbot.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
                    pnlPlayers.add(playerbot);
                    pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
                } else {
                    final JLabel player = (JLabel) factory.createLabel(u.getName());
                    player.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
                    pnlPlayers.add(player);
                    pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
                }
            }
        });
        for (int i = 0; i < this.lobby.getFreeSpace(); i++) {
            if (!user.equals(this.lobby.getOwner())) {
                final JLabel space = (JLabel) factory.createLabel(EMPTY_SPACE);
                space.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
                pnlPlayers.add(space);
                pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
            } else {
                final JLabel bot = (JLabel) factory.createLabel(BOT_SPACE, Icon.PLUS.getIcon(), JLabel.RIGHT);
                class ML implements MouseListener {
                    @Override
                    public void mouseClicked(final MouseEvent event) {
                        ControllerSingleton.getController().addBotToLobby(user, lobby, UserType.BOT_EASY);
                    }

                    @Override
                    public void mousePressed(final MouseEvent event) {
                        ControllerSingleton.getController().addBotToLobby(user, lobby, UserType.BOT_EASY);
                    }

                    @Override
                    public void mouseReleased(final MouseEvent event) {
                    }

                    @Override
                    public void mouseEntered(final MouseEvent event) {
                        ((JLabel) event.getSource()).setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.GRAY),
                                new EmptyBorder(PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING)));
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    }

                    @Override
                    public void mouseExited(final MouseEvent event) {
                        ((JLabel) event.getSource()).setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.BLACK),
                                new EmptyBorder(PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING, PLAYER_BOX_PADDING)));
                        setCursor(Cursor.getDefaultCursor());
                    }

                }
                bot.addMouseListener(new ML());
                bot.setBorder(factory.createBorder(Color.BLACK, PLAYER_BOX_PADDING));
                pnlPlayers.add(bot);
                pnlPlayers.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
            }

        }

        pnlInfo.add(
                factory.createLabel(String.valueOf(this.lobby.getInfo().getMaxPlayer()), Icon.PLAYER.getIcon(), JLabel.RIGHT));
        pnlInfo.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
        pnlInfo.add(factory.createLabel(String.valueOf(this.lobby.getInfo().getInitialDiceNumber()), Icon.DICE.getIcon(),
                JLabel.RIGHT));
        pnlInfo.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));
        pnlInfo.add(factory.createLabel(String.valueOf(this.lobby.getInfo().getMaxTurnTime().getSeconds()), Icon.TIME.getIcon(),
                JLabel.RIGHT));
        pnlInfo.add(Box.createRigidArea(new Dimension(0, VERTICAL_PADDING)));

        final JButton btnEnterLobby = (JButton) factory.createButton(ENTER_LOBBY, Icon.ENTER.getIcon());
        btnEnterLobby.addActionListener(e -> {
            ControllerSingleton.getController().joinLobby(user, this.lobby);
        });
        pnlButtons.add(btnEnterLobby);
        final JButton btnExitLobby = (JButton) factory.createButton(EXIT_LOBBY, Icon.EXIT.getIcon());
        btnExitLobby.addActionListener(e -> {
            ControllerSingleton.getController().exitLobby(user);
        });
        pnlButtons.add(btnExitLobby);
        final JButton btnStartLobby = (JButton) factory.createButton(START_LOBBY, Icon.START.getIcon());
        btnStartLobby.addActionListener(e -> {
            ControllerSingleton.getController().startLobby(user);
        });
        pnlButtons.add(btnStartLobby);
        if (this.lobby.getOwner().equals(user) && this.lobby.getUsers().size() >= MINUMUM_PLAYERS) {
            btnStartLobby.setEnabled(true);
        } else {
            btnStartLobby.setEnabled(false);
        }
        if (this.lobby.getUsers().contains(user)) {
            btnExitLobby.setEnabled(true);
            btnEnterLobby.setEnabled(false);
        } else {
            btnExitLobby.setEnabled(false);
            btnEnterLobby.setEnabled(true);
        }

        final JPanel ptop = factory.createPanel(new FlowLayout());
        ptop.add(name);
        pnlCenter.add(ptop, BorderLayout.PAGE_START);
        pnlCenter.add(pnlPlayers, BorderLayout.CENTER);
        pnlCenter.add(pnlInfo, BorderLayout.EAST);
        this.add(new JScrollPane(pnlCenter), BorderLayout.CENTER);
        this.add(pnlButtons, BorderLayout.SOUTH);
    }

    /**
     * Get the lobby described by the panel.
     * 
     * @return the lobby
     */
    public Lobby getLobby() {
        return this.lobby;
    }
}
