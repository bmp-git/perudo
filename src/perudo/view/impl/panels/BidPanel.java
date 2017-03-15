package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.impl.BidImpl;
import perudo.view.GUIFactory;
import perudo.view.impl.GUIFactorySingleton;
import perudo.view.impl.Icon;
import perudo.view.impl.components.DiceLabel;

/**
 * Panel representing dices and values to bid.
 */
public class BidPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 48;

    private Game game;

    private final JButton btnNextDice;
    private final JButton btnPrevDice;
    private final DiceLabel lblDice;

    private final JButton btnNextNum;
    private final JButton btnPrevNum;
    private final JLabel lblNum;

    /**
     * Initialize panel elements.
     */
    public BidPanel() {
        super();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.setLayout(new BorderLayout());
        final JPanel pnlDice = factory.createPanel();
        pnlDice.setLayout(new FlowLayout());

        this.lblDice = new DiceLabel();
        this.btnNextDice = (JButton) factory.createButton("", Icon.RIGHT.getIcon());
        this.btnNextDice.addActionListener(a -> {
            this.lblDice.setValue(this.lblDice.getValue() + 1, DICE_SIZE);
            updateDiceButtons();
            changeDice(this.lblDice.getValue());
            repaintPanel(pnlDice);

        });
        this.btnPrevDice = (JButton) factory.createButton("", Icon.LEFT.getIcon());
        this.btnPrevDice.addActionListener(a -> {
            lblDice.setValue(lblDice.getValue() - 1, DICE_SIZE);
            updateDiceButtons();
            changeDice(this.lblDice.getValue());
            repaintPanel(pnlDice);
        });
        pnlDice.add(this.btnPrevDice);
        pnlDice.add(this.lblDice);
        pnlDice.add(this.btnNextDice);

        final JPanel pnlNum = factory.createPanel();
        pnlNum.setLayout(new FlowLayout());
        this.lblNum = (JLabel) factory.createLabel(String.valueOf(1));
        this.btnNextNum = (JButton) factory.createButton("", Icon.RIGHT.getIcon());
        this.btnNextNum.addActionListener(a -> {
            this.lblNum.setText(String.valueOf(Integer.parseInt(lblNum.getText()) + 1));
            updateNumButtons(this.lblDice.getValue());
            repaintPanel(pnlNum);
        });
        this.btnPrevNum = (JButton) factory.createButton("", Icon.LEFT.getIcon());
        this.btnPrevNum.addActionListener(a -> {
            this.lblNum.setText(String.valueOf(Integer.parseInt(this.lblNum.getText()) - 1));
            updateNumButtons(this.lblDice.getValue());
            repaintPanel(pnlNum);
        });
        pnlNum.add(this.btnPrevNum);
        pnlNum.add(this.lblNum);
        pnlNum.add(this.btnNextNum);

        this.add(pnlDice, BorderLayout.CENTER);
        this.add(pnlNum, BorderLayout.SOUTH);
    }

    private void updateDiceButtons() {
        if (this.game.turnIsPalifico() && this.game.getCurrentBid().isPresent()) {
            this.btnNextDice.setEnabled(false);
            this.btnPrevDice.setEnabled(false);
        } else if (this.lblDice.getValue() >= game.getSettings().getMaxDiceValue()) {
            this.btnNextDice.setEnabled(false);
            this.btnPrevDice.setEnabled(true);
        } else if (this.lblDice.getValue() <= 1) {
            this.btnPrevDice.setEnabled(false);
            this.btnNextDice.setEnabled(true);
        } else {
            this.btnNextDice.setEnabled(true);
            this.btnPrevDice.setEnabled(true);
        }
    }

    private void changeDice(final int diceValue) {
        this.lblNum.setText(String.valueOf(this.game.getMinDiceQuantity(diceValue)));
        this.updateNumButtons(diceValue);
    }

    private void updateNumButtons(final int diceValue) {
        if (Integer.parseInt(this.lblNum.getText()) == this.game.getMaxDiceQuantity(diceValue)
                && Integer.parseInt(this.lblNum.getText()) == this.game.getMinDiceQuantity(diceValue)) {
            this.btnNextNum.setEnabled(false);
            this.btnPrevNum.setEnabled(false);
        } else if (Integer.parseInt(this.lblNum.getText()) >= this.game.getMaxDiceQuantity(diceValue)) {
            this.btnNextNum.setEnabled(false);
            this.btnPrevNum.setEnabled(true);
        } else if (Integer.parseInt(this.lblNum.getText()) <= this.game.getMinDiceQuantity(diceValue)) {
            this.btnPrevNum.setEnabled(false);
            this.btnNextNum.setEnabled(true);
        } else {
            this.btnNextNum.setEnabled(true);
            this.btnPrevNum.setEnabled(true);
        }
    }

    private void repaintPanel(final JPanel panel) {
        panel.repaint();
        panel.revalidate();
    }

    /**
     * Set the new game and update panel buttons.
     * 
     * @param game
     *            the updated game
     */
    public void setGame(final Game game) {
        this.game = game;
        if (this.game.getCurrentBid().isPresent()) {
            this.lblDice.setValue(this.game.getCurrentBid().get().getDiceValue(), DICE_SIZE);
        } else {
            this.lblDice.setValue(Math.round(game.getSettings().getMaxDiceValue() / 2f), DICE_SIZE);
        }
        this.lblNum.setText(String.valueOf(this.game.getMinDiceQuantity(this.lblDice.getValue())));
        this.updateDiceButtons();
        this.updateNumButtons(Integer.valueOf(this.lblDice.getValue()));
    }

    /**
     * Get the bid selected in panel.
     * 
     * @return the bid
     */
    public Bid getBid() {
        try {
            return new BidImpl(Integer.parseInt(this.lblNum.getText()), this.lblDice.getValue());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }
}
