package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Optional;

import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.impl.BidImpl;
import perudo.utility.ErrorTypeException;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;
import perudo.view.impl.components.DiceLabel;

public class BidPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 64;

    private final GUIFactory factory;
    private Game game;

    private final JButton btnNextDice;
    private final JButton btnPrevDice;
    private DiceLabel lblDice;

    private final JButton btnNextNum;
    private final JButton btnPrevNum;
    private JLabel lblNum;

    public BidPanel(final Game game) {
        super();
        this.factory = new StandardGUIFactory();
        this.game = game;
        this.setLayout(new BorderLayout());
        JPanel pnlDice = factory.createPanel();
        pnlDice.setLayout(new FlowLayout());

        this.lblDice = new DiceLabel(Math.round(game.getSettings().getMaxDiceValue() / 2), DICE_SIZE);
        this.btnNextDice = (JButton) this.factory.createButton("Next");
        this.btnPrevDice = (JButton) this.factory.createButton("Prev");

        this.btnNextDice.addActionListener(a -> {
            lblDice.setValue(lblDice.getValue() + 1, DICE_SIZE);
            updateDiceButtons();
            repaintPanel(pnlDice);
        });
        this.btnPrevDice.addActionListener(a -> {
            lblDice.setValue(lblDice.getValue() - 1, DICE_SIZE);
            updateDiceButtons();
            repaintPanel(pnlDice);
        });

        pnlDice.add(this.btnPrevDice);
        pnlDice.add(this.lblDice);
        pnlDice.add(this.btnNextDice);

        JPanel pnlNum = factory.createPanel();
        pnlNum.setLayout(new FlowLayout());
        this.lblNum = (JLabel) this.factory.createLabel(String.valueOf(1));
        this.btnNextNum = (JButton) this.factory.createButton("Next");
        this.btnPrevNum = (JButton) this.factory.createButton("Prev");
        this.btnNextNum.addActionListener(a -> {
            lblNum.setText(String.valueOf(Integer.parseInt(lblNum.getText()) + 1));
            updateNumButtons();
            repaintPanel(pnlNum);
        });
        this.btnPrevNum.addActionListener(a -> {
            lblNum.setText(String.valueOf(Integer.parseInt(lblNum.getText()) - 1));
            updateNumButtons();
            repaintPanel(pnlNum);
        });
        pnlNum.add(this.btnPrevNum);
        pnlNum.add(this.lblNum);
        pnlNum.add(this.btnNextNum);

        updateDiceButtons();
        updateNumButtons();

        this.add(pnlDice, BorderLayout.CENTER);
        this.add(pnlNum, BorderLayout.SOUTH);
    }

    private void updateDiceButtons() {
        if (this.lblDice.getValue() >= game.getSettings().getMaxDiceValue()) {
            btnNextDice.setEnabled(false);
            btnPrevDice.setEnabled(true);
        } else if (this.lblDice.getValue() <= 1) {
            btnPrevDice.setEnabled(false);
            btnNextDice.setEnabled(true);
        } else {
            btnNextDice.setEnabled(true);
            btnPrevDice.setEnabled(true);
        }
    }

    private void updateNumButtons() {
        if (Integer.parseInt(this.lblNum.getText()) >= 10) {
            btnNextNum.setEnabled(false);
            btnPrevNum.setEnabled(true);
        } else if (Integer.parseInt(this.lblNum.getText()) <= 1) {
            btnPrevNum.setEnabled(false);
            btnNextNum.setEnabled(true);
        } else {
            btnNextNum.setEnabled(true);
            btnPrevNum.setEnabled(true);
        }
    }

    private void repaintPanel(final JPanel panel) {
        panel.repaint();
        panel.revalidate();
    }

    public Bid getBid() {
        try {
            return new BidImpl(Integer.valueOf(this.lblNum.getText()), this.lblDice.getValue());
        } catch (NumberFormatException | ErrorTypeException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }
}
