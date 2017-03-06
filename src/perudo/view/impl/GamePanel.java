package perudo.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Optional;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.panels.GamePlayPanel;
import perudo.view.impl.panels.GameTurnPanel;
import perudo.view.impl.panels.HistoryPanel;
import perudo.view.impl.panels.MenuBottomPanel;
import perudo.view.impl.panels.PlayersHandsListPanel;
import perudo.view.impl.panels.TimePanel;

public class GamePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TURN_NAME = "It's your turn.";
    private static final String TURN_TEXT = "It's your turn, make a play!";

    private Optional<Game> game;
    private Optional<User> user;
    private final GUIFactory factory;

    private final JPanel pnlCenter;
    private final MenuBottomPanel pnlBottomMenu;
    private final TimePanel pnlTime;
    private final HistoryPanel pnlHistory;
    private PlayersHandsListPanel pnlHand;
    private GamePlayPanel pnlGamePlay;
    private GameTurnPanel pnlGameTurn;

    public GamePanel() {
        super();
        this.game = Optional.empty();
        this.user = Optional.empty();
        this.factory = new StandardGUIFactory();
        this.setLayout(new BorderLayout());
        this.pnlCenter = this.factory.createPanel(new BorderLayout());
        this.pnlBottomMenu = (MenuBottomPanel) this.factory.createMenuBottomPanel();
        this.pnlTime = new TimePanel();
        this.pnlHistory = new HistoryPanel();

        this.add(this.pnlCenter, BorderLayout.CENTER);
        this.add(this.pnlBottomMenu, BorderLayout.SOUTH);
        this.add(this.pnlTime, BorderLayout.EAST);
        JScrollPane scroll = new JScrollPane(this.pnlHistory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scroll, BorderLayout.WEST);
    }

    public void setGame(final Game game) {
        this.game = Optional.ofNullable(game);
        this.pnlTime.setGame(game);
        if (this.pnlGamePlay == null) {
            this.pnlGamePlay = new GamePlayPanel(this.game.get(), this.user.get());
        }

        if (this.pnlHand == null) {
            this.pnlHand = new PlayersHandsListPanel(this.game.get(), this.user.get());
        } else {
            this.pnlHand.setGame(this.game.get());
        }
        if (this.pnlGameTurn == null) {
            this.pnlGameTurn = new GameTurnPanel(this.game.get(), this.user.get());
        } else {
            this.pnlGameTurn.setTurnPanel(this.game.get(), this.user.get());
        }
        this.pnlCenter.add(this.pnlGameTurn, BorderLayout.NORTH);
        this.pnlCenter.add(this.pnlHand, BorderLayout.CENTER);
        this.pnlCenter.add(this.pnlGamePlay, BorderLayout.SOUTH);
        this.pnlCenter.revalidate();
        this.repaint();
        if (this.checkTurn()) {
            JOptionPane.showConfirmDialog(GamePanel.this, TURN_TEXT, TURN_NAME, JOptionPane.DEFAULT_OPTION);
        }
    }

    private boolean checkTurn() {
        return this.game.get().getTurn().equals(this.user.get());
    }

    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
        this.pnlBottomMenu.setUser(this.user.get());
    }

    public void playNotify(final Game game, final User user) {
        this.pnlHistory.addInfo(user.getName() + " play " + game.getCurrentBid().get().getQuantity() + " dices of "
                + game.getCurrentBid().get().getDiceValue());
        this.setGame(game);
    }

    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.pnlHistory.addInfo(user.getName() + " doubt " + (win ? "and win" : "and lose"));
        this.setGame(game);
    }

    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.pnlHistory.addInfo(user.getName() + " urged " + (win ? "and win" : "and lose"));
        this.setGame(game);
    }

    public void callPalificoNotify(final Game game, final User user) {
        this.pnlHistory.addInfo(user.getName() + " called palifico");
        this.setGame(game);
    }

    public void exitGameNotify(final Game game, final User user) {
        this.pnlHistory.addInfo(user.getName() + " exited");
        this.setGame(game);
    }

}
