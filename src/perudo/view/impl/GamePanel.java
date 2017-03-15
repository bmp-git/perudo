package perudo.view.impl;

import java.awt.BorderLayout;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.components.TopMenuGame;
import perudo.view.impl.panels.GamePlayPanel;
import perudo.view.impl.panels.GameTurnPanel;
import perudo.view.impl.panels.HistoryPanel;
import perudo.view.impl.panels.MenuBottomPanel;
import perudo.view.impl.panels.PlayersHandsListPanel;
import perudo.view.impl.panels.PlayersListPanel;
import perudo.view.impl.panels.TimePanel;

/**
 * Models a JPanel made for playing a game of Perudo.
 * 
 */
public class GamePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TURN_NAME = "It's your turn.";
    private static final String TURN_TEXT = "It's your turn, make a play!";

    private final GUIFactory factory;
    private Optional<Game> game;
    private Optional<User> user;

    private final JPanel pnlCenter;
    private final PlayersListPanel pnlPlayers;
    private final MenuBottomPanel pnlBottomMenu;
    private final TimePanel pnlTime;
    private final HistoryPanel pnlHistory;
    private final GamePlayPanel pnlGamePlay;
    private final GameTurnPanel pnlGameTurn;
    private final PlayersHandsListPanel pnlHand;
    private final TopMenuGame pnlMenu;

    /**
     * Create all game sub panels.
     */
    public GamePanel() {
        super();
        this.factory = GUIFactorySingleton.getFactory();
        this.game = Optional.empty();
        this.user = Optional.empty();
        this.setLayout(new BorderLayout());
        final JPanel pnlRight = this.factory.createPanel(new BorderLayout());
        this.pnlCenter = this.factory.createPanel(new BorderLayout());
        this.pnlBottomMenu = new MenuBottomPanel();
        this.pnlTime = new TimePanel();
        this.pnlHistory = new HistoryPanel();
        this.pnlGamePlay = new GamePlayPanel();
        this.pnlGameTurn = new GameTurnPanel();
        this.pnlHand = new PlayersHandsListPanel();
        this.pnlPlayers = new PlayersListPanel();
        this.pnlMenu = new TopMenuGame();

        pnlRight.add(this.pnlTime, BorderLayout.NORTH);
        pnlRight.add(this.pnlPlayers, BorderLayout.CENTER);

        this.add(this.pnlMenu, BorderLayout.NORTH);
        this.add(this.pnlCenter, BorderLayout.CENTER);
        this.add(this.pnlBottomMenu, BorderLayout.SOUTH);
        this.add(pnlRight, BorderLayout.EAST);
        final JScrollPane scroll = this.factory.createScrollPaneWithoutBorder(this.pnlHistory);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scroll, BorderLayout.WEST);
    }

    /**
     * Set for first time or update the game to the panel and update all
     * subpanels. To set a different game you must make a new GamePanel.
     * 
     * @param game
     *            the game to set
     */
    public void setGame(final Game game) {
        this.game = Optional.ofNullable(game);
        this.pnlTime.setGame(game);
        this.pnlPlayers.setGame(game);
        this.pnlGamePlay.setGame(this.game.get(), this.user.get());
        this.pnlHand.setGame(this.game.get(), this.user.get());
        this.pnlGameTurn.setTurnPanel(this.game.get(), this.user.get());
        this.pnlCenter.removeAll();
        this.pnlCenter.add(this.pnlGameTurn, BorderLayout.NORTH);
        this.pnlCenter.add(this.factory.createScrollPaneWithoutBorder(this.pnlHand), BorderLayout.CENTER);
        this.pnlCenter.add(this.pnlGamePlay, BorderLayout.SOUTH);
        this.pnlCenter.revalidate();

        this.repaint();
        if (this.checkTurn() && !this.game.get().isOver()) {
            JOptionPane.showConfirmDialog(GamePanel.this, TURN_TEXT, TURN_NAME, JOptionPane.DEFAULT_OPTION);
        }
    }

    private boolean checkTurn() {
        return this.game.get().getTurn().equals(this.user.get());
    }

    /**
     * Set the user to the panel and update subpanels that need it.
     * 
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
        this.pnlMenu.setUser(this.user.get());
        this.pnlBottomMenu.setUser(this.user.get());
    }

    /**
     * Notify a play to the game panel.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has played
     */
    public void playNotify(final Game game, final User user) {
        this.pnlHistory.addInfo(user.getName() + " play " + game.getCurrentBid().get().getQuantity() + " dices of "
                + game.getCurrentBid().get().getDiceValue());
        this.setGame(game);
    }

    /**
     * Notify a doubt to the game panel.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has doubted
     * @param win
     *            the doubt result
     */
    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.pnlHistory.addInfo(user.getName() + " doubt " + (win ? "and win" : "and lose"));
        this.setGame(game);
    }

    /**
     * Notify a urge to the game panel.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has urged
     * @param win
     *            the urge result
     */
    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.pnlHistory.addInfo(user.getName() + " urged " + (win ? "and win" : "and lose"));
        this.setGame(game);
    }

    /**
     * Notify a palifico call to the game panel.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has called palifico
     */
    public void callPalificoNotify(final Game game, final User user) {
        this.pnlHistory.addInfo(user.getName() + " called palifico");
        this.setGame(game);
    }

    /**
     * Notify a exit to the game panel.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who exited
     */
    public void exitGameNotify(final Game game, final User user) {
        this.pnlHistory.addInfo(user.getName() + " exited");
        if (!this.game.get().isOver()) {
            this.setGame(game);
        } else {
            this.pnlPlayers.setGame(game);
            this.pnlHand.setGame(this.game.get(), this.user.get());
            this.pnlCenter.revalidate();
        }
    }

    /**
     * Notify the game end to the game panel.
     * 
     * @param game
     *            the updated game
     */
    public void gameEndedNotify(final Game game) {
        this.pnlHistory.addInfo("Game finished");
        this.pnlGameTurn.youWin(!game.hasLost(this.user.get()));
        this.pnlGamePlay.setPanelEnabled(false);
        this.close();
    }

    /**
     * Get the game associated to the panel.
     * 
     * @return the game associated
     */
    public Game getGame() {
        return this.game.orElse(null);
    }
    /**
     * Close gamePanel.
     */
    public void close() {
        this.pnlTime.stop();
    }
}
