package perudo.view.console;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.ActionListBox;
import com.googlecode.lanterna.gui2.BorderLayout;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;

import perudo.controller.Controller;
import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.User;
import perudo.model.impl.BidImpl;

/**
 * 
 */
public class GameForm extends BaseForm {

    private final Label lblHand, lblBid, lblPalifico;
    private final TextBox txbDiceQuantity, txbDiceValue;
    private final ActionListBox lstHistory, lstUsers;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final List<String> diceCount = new ArrayList<String>();

    private final Controller controller;
    private User user;
    private Game game;

    /**
     * 
     * @param controller
     *            to do
     * @param textGUI
     *            to do
     */
    public GameForm(final Controller controller, final MultiWindowTextGUI textGUI) {
        super(textGUI);

        final Label lblTime;
        final Button btnDoubt, btnUrge, btnPlay, btnPalifico, btnExit;

        this.controller = controller;
        super.getWindow().setTitle("PERUDO - GAME");
        super.getWindow().setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.EXPANDED));

        lblTime = new Label("Time left: 42s");
        this.lblHand = new Label("lblHand");
        this.lblBid = new Label("lblBid");
        this.lblPalifico = new Label("lblPalifico");

        btnDoubt = new Button("Doubt   ", () -> btnDoubtClicked());
        btnUrge = new Button("Urge    ", () -> btnUrgeClicked());
        btnPlay = new Button("Play    ", () -> btnPlayClicked());
        btnExit = new Button("Exit    ", () -> btnExitClicked());
        btnPalifico = new Button("Palifico", () -> btnPalificoClicked());

        this.txbDiceQuantity = new TextBox(new TerminalSize(3, 1)).setValidationPattern(Pattern.compile("^\\d{1,2}$"));
        this.txbDiceValue = new TextBox(new TerminalSize(3, 1)).setValidationPattern(Pattern.compile("^\\d{1,2}$"));
        this.lstHistory = new ActionListBox();
        this.lstUsers = new ActionListBox();
        this.executor.scheduleAtFixedRate(() -> {
            super.getTextGUI().getGUIThread().invokeLater(() -> {
                if (this.game != null) {
                    lblTime.setText("Time: " + this.game.getTurnRemainingTime().getSeconds() + "s");
                } else {
                    lblTime.setText("lblTime");
                }
            });
        }, 0, 1, TimeUnit.SECONDS);

        final Panel mainPanel = new Panel(new BorderLayout());

        final Panel leftPanel = new Panel(new BorderLayout());
        final Panel midPanel = new Panel(new BorderLayout());
        final Panel rightPanel = new Panel(new BorderLayout());

        mainPanel.addComponent(leftPanel.withBorder(Borders.singleLine()), BorderLayout.Location.LEFT);
        mainPanel.addComponent(midPanel, BorderLayout.Location.CENTER);
        mainPanel.addComponent(rightPanel, BorderLayout.Location.RIGHT);

        // left panel
        final Panel gridLeftPanel = new Panel(new GridLayout(1));
        leftPanel.addComponent(gridLeftPanel, BorderLayout.Location.TOP);

        gridLeftPanel.addComponent(this.txbDiceQuantity.withBorder(Borders.singleLine("Quantity")));
        gridLeftPanel.addComponent(this.txbDiceValue.withBorder(Borders.singleLine("Value   ")));
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(btnPlay);
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(btnDoubt);
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(btnUrge);
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(btnPalifico);

        leftPanel.addComponent(new Panel(new GridLayout(1)).addComponent(btnExit), BorderLayout.Location.BOTTOM);

        // mid panel
        final Panel statusPanel = new Panel(new GridLayout(1));

        statusPanel.addComponent(this.lblHand);
        statusPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        statusPanel.addComponent(this.lblBid);
        statusPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        statusPanel.addComponent(this.lblPalifico);

        midPanel.addComponent(statusPanel.withBorder(Borders.singleLine("Status")), BorderLayout.Location.BOTTOM);
        midPanel.addComponent(this.lstHistory.withBorder(Borders.singleLine("Hisory")), BorderLayout.Location.CENTER);

        // right panel
        rightPanel.addComponent(lstUsers.withBorder(Borders.singleLine("Players")), BorderLayout.Location.CENTER);
        lstUsers.setEnabled(false);
        rightPanel.addComponent(new Panel().addComponent(lblTime).withBorder(Borders.singleLine()),
                BorderLayout.Location.BOTTOM);

        super.getWindow().setComponent(mainPanel);
    }

    /**
     * 
     * @param user
     *            to do
     */
    public void setUser(final User user) {
        this.user = user;
    }

    private void btnExitClicked() {
        this.controller.exitGame(this.user);
    }

    private void btnPlayClicked() {

        Bid bid = null;
        try {
            bid = new BidImpl(Integer.parseInt(txbDiceQuantity.getText()), Integer.parseInt(txbDiceValue.getText()));
        } catch (IllegalArgumentException e) {
            Utils.showMessageBox("Error", "Invalid bid", super.getTextGUI());
        }
        if (bid != null) {
            this.controller.play(this.user, bid);
        }

    }

    private void btnUrgeClicked() {
        this.controller.urge(this.user);
    }

    private void btnDoubtClicked() {
        this.controller.doubt(this.user);
    }

    private void btnPalificoClicked() {
        this.controller.callPalifico(this.user);
    }

    /**
     * 
     * @param game
     *            to do
     */
    public void updateGame(final Game game) {
        if (game.equals(this.game)) {
            this.setGame(game);
        }
    }

    /**
     * 
     * @param game
     *            to do
     */
    public void setGame(final Game game) {
        this.game = game;
        this.lstHistory.clearItems();
        this.addToHisoty("GAME STARTED!", "game started");
        this.refreshNewRound();
    }

    private String getCurrentTime() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    private void addToHisoty(final String title, final String str) {
        final String strTime = str + "\n\n" + this.getCurrentTime();
        this.lstHistory.addItem(title, () -> {
            Utils.showMessageBox(title, strTime, super.getTextGUI());
        });
        this.lstHistory.setSelectedIndex(this.lstHistory.getItemCount() - 1);
    }

    private void refreshNewTurn() {

        this.refreshDiceCount();
        // final String str = "Is the turn of " + this.game.getTurn().getName();
        // this.addToHisoty(str, str);

        this.refreshUsersList();

        if (this.game.getCurrentBid().isPresent()) {

            this.lblBid.setText(
                    this.game.getBidUser().get().getName() + " bid to " + this.game.getCurrentBid().get().getQuantity()
                            + " dice of " + this.game.getCurrentBid().get().getDiceValue());
            this.lblBid.setForegroundColor(TextColor.ANSI.RED);
        }

        // my turn
        if (this.game.getTurn().equals(this.user) && !this.game.isOver()) {

            // auto set a decent value in the textboxs
            if (!this.txbDiceQuantity.isFocused()) {
                try {
                    this.txbDiceQuantity.setText(Integer
                            .toString(this.game.nextBid(Integer.parseInt(this.txbDiceValue.getText())).getQuantity()));
                } catch (final NumberFormatException ex) {
                    this.txbDiceQuantity.setText(Integer.toString(this.game.nextBid(1).getQuantity()));
                    this.txbDiceValue.setText("1");
                }
            }
            Utils.showMessageBox("Your turn", "Is your turn", super.getTextGUI());
        }

    }

    private String diceToString(final Map<Integer, Integer> diceCount) {
        String dice = "";
        for (int d = 1; d <= this.game.getSettings().getMaxDiceValue(); d++) {
            for (int k = 0; k < diceCount.get(d); k++) {
                dice += d + " ";
            }
        }
        return dice;
    }

    private void refreshNewRound() {
        this.lblBid.setText("No bid yet");
        this.lblBid.setForegroundColor(TextColor.ANSI.BLACK);
        this.lblPalifico.setText(this.game.turnIsPalifico() ? "Round is palifico" : "Normal round");

        String dice = "You lose";

        if (this.game.getUserStatus(this.user).getRemainingDice() > 0) {
            dice = "Your hand: " + diceToString(this.game.getUserStatus(this.user).getDiceCount());
        }

        lblHand.setText(dice);
        this.refreshNewTurn();
    }

    private void refreshUsersList() {
        this.lstUsers.clearItems();

        for (int i = 0; i < this.game.getUsers().size(); i++) {
            String modifiers = " ";

            if (this.game.getUsers().get(i).equals(this.game.getTurn())) {
                modifiers += "(*)";
            }

            if (this.game.getUsers().get(i).equals(this.user)) {
                modifiers += "(you)";
            }

            if (this.game.getUserStatus(this.game.getUsers().get(i)).getRemainingDice() == 0) {
                modifiers += "(lose)";
            } else {
                modifiers += "(" + this.game.getUserStatus(this.game.getUsers().get(i)).getRemainingDice() + ")";
            }

            lstUsers.addItem(this.game.getUsers().get(i).getName() + modifiers, () -> {
            });
        }
    }

    /**
     * 
     * @param game
     *            to do
     * @param user
     *            to do
     */
    public void playNotify(final Game game, final User user) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        final String str = user.getName() + " bid to " + this.game.getCurrentBid().get().getQuantity() + " dice of "
                + this.game.getCurrentBid().get().getDiceValue();
        this.addToHisoty(str, str);
        this.refreshNewTurn();

    }

    private void refreshDiceCount() {
        if (!this.game.getCurrentBid().isPresent()) {
            return;
        }
        diceCount.clear();
        diceCount.add("## DICE COUNT ##");

        for (int i = 0; i < this.game.getUsers().size(); i++) {
            if (this.game.getUserStatus(this.game.getUsers().get(i)).getRemainingDice() > 0) {
                diceCount.add(" " + this.game.getUsers().get(i).getName() + ": "
                        + diceToString(this.game.getUserStatus(this.game.getUsers().get(i)).getDiceCount()));
            }
        }

        diceCount.add("Total: " + this.game.getRules().getRealBidDiceCount(this.game).get());
        diceCount.add("Bid  : " + this.game.getCurrentBid().get().getQuantity() + " dice of "
                + this.game.getCurrentBid().get().getDiceValue());
        diceCount.add("Bid from " + this.game.getBidUser().get().getName());

        diceCount.add("## DICE COUNT ##");
    }

    /**
     * 
     * @param game
     *            to do
     * @param user
     *            to do
     * @param win
     *            to do
     */
    public void doubtNotify(final Game game, final User user, final boolean win) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        this.diceCount
                .forEach(s -> this.addToHisoty(s, this.diceCount.stream().reduce((s1, s2) -> s1 + "\n" + s2).get()));

        if (win) {
            final String str = user.getName() + " doubts and wins";
            this.addToHisoty(str, str);
        } else {
            final String str = user.getName() + " doubts and loses";
            this.addToHisoty(str, str);
        }
        this.refreshNewRound();
    }

    /**
     * 
     * @param game
     *            to do
     * @param user
     *            to do
     * @param win
     *            to do
     */
    public void urgeNotify(final Game game, final User user, final boolean win) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        this.diceCount
                .forEach(s -> this.addToHisoty(s, this.diceCount.stream().reduce((s1, s2) -> s1 + "\n" + s2).get()));

        if (win) {
            final String str = user.getName() + " urges and wins";
            this.addToHisoty(str, str);
        } else {
            final String str = user.getName() + " urges and loses";
            this.addToHisoty(str, str);
        }
        this.refreshNewRound();
    }

    /**
     * 
     * @param game
     *            to do
     * @param user
     *            to do
     */
    public void exitGameNotify(final Game game, final User user) {
        if (!game.equals(this.game)) {
            return;
        }

        if (user.equals(this.user)) {
            return;
        }
        this.game = game;
        final String str = user.getName() + " exits the game";
        this.addToHisoty(str, str);
        this.refreshUsersList();
        this.refreshNewRound();
    }

    /**
     * 
     * @param game
     *            to do
     * @param user
     *            to do
     */
    public void callPalificoNotify(final Game game, final User user) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        final String str = user.getName() + " calls palifico";
        this.addToHisoty(str, str);
        this.refreshNewRound();
    }

    @Override
    public void close() {
        super.close();
        this.executor.shutdown();
    }

    /**
     * 
     * @return to do
     */
    public Game getGame() {
        return this.game;
    }

}
