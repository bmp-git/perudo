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
import perudo.utility.ErrorTypeException;

public class GameForm extends BaseForm {

    private final Button btnDoubt, btnUrge, btnPlay, btnPalifico, btnExit;
    private final TextBox txbDiceQuantity, txbDiceValue;
    private final Label lblHand, lblBid, lblPalifico, lblTime;
    private final ActionListBox lstHistory, lstUsers;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final Controller controller;
    private User user;
    private Game game;

    public GameForm(Controller controller, MultiWindowTextGUI textGUI) {
        super(textGUI);
        this.controller = controller;
        this.window.setTitle("PERUDO - GAME");
        this.window.setHints(Arrays.asList(Window.Hint.CENTERED, Window.Hint.EXPANDED));

        this.lblTime = new Label("Time left: 42s");
        this.lblHand = new Label("lblHand");
        this.lblBid = new Label("lblBid");
        this.lblPalifico = new Label("lblPalifico");

        this.btnDoubt = new Button("Doubt   ", () -> btnDoubtClicked());
        this.btnUrge = new Button("Urge    ", () -> btnUrgeClicked());
        this.btnPlay = new Button("Play    ", () -> btnPlayClicked());
        this.btnExit = new Button("Exit    ", () -> btnExitClicked());
        this.btnPalifico = new Button("Palifico", () -> btnPalificoClicked());

        this.txbDiceQuantity = new TextBox(new TerminalSize(3, 1)).setValidationPattern(Pattern.compile("^\\d{1,2}$"));
        this.txbDiceValue = new TextBox(new TerminalSize(3, 1)).setValidationPattern(Pattern.compile("^\\d{1,2}$"));
        this.lstHistory = new ActionListBox();
        this.lstUsers = new ActionListBox();
        this.executor.scheduleAtFixedRate(() -> {
            this.textGUI.getGUIThread().invokeLater(() -> {
                if (this.game != null) {
                    this.lblTime.setText("Time: " + this.game.getTurnRemainingTime().getSeconds() + "s");
                } else {
                    this.lblTime.setText("lblTime");
                }
            });
        }, 0, 1, TimeUnit.SECONDS);

        Panel mainPanel = new Panel(new BorderLayout());

        Panel leftPanel = new Panel(new BorderLayout());
        Panel midPanel = new Panel(new BorderLayout());
        Panel rightPanel = new Panel(new BorderLayout());

        mainPanel.addComponent(leftPanel.withBorder(Borders.singleLine()), BorderLayout.Location.LEFT);
        mainPanel.addComponent(midPanel, BorderLayout.Location.CENTER);
        mainPanel.addComponent(rightPanel, BorderLayout.Location.RIGHT);

        // left panel
        Panel gridLeftPanel = new Panel(new GridLayout(1));
        leftPanel.addComponent(gridLeftPanel, BorderLayout.Location.TOP);

        gridLeftPanel.addComponent(this.txbDiceQuantity.withBorder(Borders.singleLine("Quantity")));
        gridLeftPanel.addComponent(this.txbDiceValue.withBorder(Borders.singleLine("Value   ")));
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(this.btnPlay);
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(this.btnDoubt);
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(this.btnUrge);
        gridLeftPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        gridLeftPanel.addComponent(this.btnPalifico);

        leftPanel.addComponent(new Panel(new GridLayout(1)).addComponent(this.btnExit), BorderLayout.Location.BOTTOM);

        // mid panel
        Panel statusPanel = new Panel(new GridLayout(1));

        statusPanel.addComponent(lblHand);
        statusPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        statusPanel.addComponent(lblBid);
        statusPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        statusPanel.addComponent(lblPalifico);

        midPanel.addComponent(statusPanel.withBorder(Borders.singleLine("Status")), BorderLayout.Location.BOTTOM);
        midPanel.addComponent(lstHistory.withBorder(Borders.singleLine("Hisory")), BorderLayout.Location.CENTER);

        // right panel
        rightPanel.addComponent(lstUsers.withBorder(Borders.singleLine("Players")), BorderLayout.Location.CENTER);
        lstUsers.setEnabled(false);
        rightPanel.addComponent(new Panel().addComponent(lblTime).withBorder(Borders.singleLine()),
                BorderLayout.Location.BOTTOM);

        this.window.setComponent(mainPanel);
    }

    public void setUser(User user) {
        this.user = user;
    }

    private void btnExitClicked() {
        this.controller.exitGame(this.user);
    }

    private void btnPlayClicked() {

        Bid bid = null;
        try {
            bid = new BidImpl(Integer.parseInt(txbDiceQuantity.getText()), Integer.parseInt(txbDiceValue.getText()));
        } catch (NumberFormatException | ErrorTypeException e) {
            Utils.showMessageBox("Error", "Invalid bid", this.textGUI);
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

    public void updateGame(Game game) {
        if (game.equals(this.game)) {
            this.setGame(game);
        }
    }

    private String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void setGame(Game game) {
        this.game = game;
        this.lstHistory.clearItems();
        this.addToHisoty("GAME STARTED!", "game started");
        this.refreshNewRound();
    }

    private void addToHisoty(final String title, final String str) {
        final String strTime = str + "\n\n" + this.getCurrentTime();
        this.lstHistory.addItem(title, () -> {
            Utils.showMessageBox(title, strTime, this.textGUI);
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

        // auto set a decent value in the textboxs
        if (this.game.getTurn().equals(this.user) && !this.game.isOver()) {
            if (this.game.turnIsPalifico() && this.game.getCurrentBid().isPresent()) {

                this.txbDiceQuantity.setText(Integer.toString(this.game.getCurrentBid().get().getQuantity() + 1));

            } else if (this.game.getCurrentBid().isPresent()) {
                int value = this.game.getCurrentBid().get().getDiceValue();
                try {
                    value = Integer.parseInt(this.txbDiceValue.getText());
                    this.txbDiceQuantity
                            .setText(Integer.toString((this.game.getCurrentBid().get().nextBid(value).getQuantity())));
                } catch (Exception ex) {
                }

            }

            Utils.showMessageBox("Your turn", "Is your turn", this.textGUI);
        }

    }

    private String diceToString(Map<Integer, Integer> diceCount) {
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

    public void playNotify(final Game game, final User user) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        String str = user.getName() + " bid to " + this.game.getCurrentBid().get().getQuantity() + " dice of "
                + this.game.getCurrentBid().get().getDiceValue();
        this.addToHisoty(str, str);
        this.refreshNewTurn();

    }

    private final List<String> diceCount = new ArrayList<String>();

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

        diceCount.add("Total: " + this.game.getRealBidDiceCount());
        diceCount.add("Bid  : " + this.game.getCurrentBid().get().getQuantity() + " dice of "
                + this.game.getCurrentBid().get().getDiceValue());
        diceCount.add("Bid from " + this.game.getBidUser().get().getName());

        diceCount.add("## DICE COUNT ##");
    }

    public void doubtNotify(Game game, User user, boolean win) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        this.diceCount
                .forEach(s -> this.addToHisoty(s, this.diceCount.stream().reduce((s1, s2) -> s1 + "\n" + s2).get()));

        if (win) {
            String str = user.getName() + " doubts and wins";
            this.addToHisoty(str, str);
        } else {
            String str = user.getName() + " doubts and loses";
            this.addToHisoty(str, str);
        }
        this.refreshNewRound();
    }

    public void urgeNotify(Game game, User user, boolean win) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        this.diceCount
                .forEach(s -> this.addToHisoty(s, this.diceCount.stream().reduce((s1, s2) -> s1 + "\n" + s2).get()));

        if (win) {
            String str = user.getName() + " urges and wins";
            this.addToHisoty(str, str);
        } else {
            String str = user.getName() + " urges and loses";
            this.addToHisoty(str, str);
        }
        this.refreshNewRound();
    }

    public void exitGameNotify(Game game, User user) {
        if (!game.equals(this.game)) {
            return;
        }

        if (user.equals(this.user)) {
            return;
        }
        this.game = game;
        String str = user.getName() + " exits the game";
        this.addToHisoty(str, str);
        this.refreshUsersList();
        this.refreshNewRound();
    }

    public void callPalificoNotify(Game game, User user) {
        if (!game.equals(this.game)) {
            return;
        }

        this.game = game;
        String str = user.getName() + " calls palifico";
        this.addToHisoty(str, str);
        this.refreshNewRound();
    }

    public Window getWindow() {
        return this.window;
    }

    @Override
    public void close() {
        super.close();
        this.executor.shutdown();
    }

    public Game getGame() {
        return this.game;
    }

}
