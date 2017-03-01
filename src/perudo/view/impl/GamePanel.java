package perudo.view.impl;

import java.awt.BorderLayout;
import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.panels.BidPanel;
import perudo.view.impl.panels.HistoryPanel;
import perudo.view.impl.panels.MenuBottomPanel;
import perudo.view.impl.panels.TimePanel;

public class GamePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Optional<Game> game;
    private Optional<User> user;
    private final GUIFactory factory;

    private final MenuBottomPanel pnlBottomMenu;
    private final TimePanel pnlTime;
    private final HistoryPanel pnlHistory;
    //private TablePanel pnlTable;
    private BidPanel pnlBid;

    public GamePanel() {
        super();
        this.game = Optional.empty();
        this.user = Optional.empty();
        this.factory = new StandardGUIFactory();
        this.setLayout(new BorderLayout());
        this.pnlBottomMenu = (MenuBottomPanel) this.factory.createMenuBottomPanel();
        this.pnlTime = new TimePanel();
        this.pnlHistory = new HistoryPanel();
        this.pnlBid = null;
        /*this.pnlTable = null;
        try {
            this.pnlTable = new TablePanel(ImageIO.read(new File(GamePanel.class.getResource("/images/table.jpg").getPath()))
                    .getScaledInstance(720, 480, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        

        //this.add(this.pnlTable, BorderLayout.CENTER);
        //this.add(this.pnlBid, BorderLayout.CENTER);

        this.add(this.pnlBottomMenu, BorderLayout.SOUTH);
        this.add(this.pnlTime, BorderLayout.EAST);
        JScrollPane scroll = new JScrollPane(this.pnlHistory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scroll, BorderLayout.WEST);
    }

    public void setGame(final Game game) {
        this.game = Optional.ofNullable(game);
        this.pnlTime.setGame(game);
        this.pnlBid = new BidPanel(game);
        this.add(this.pnlBid, BorderLayout.CENTER);
    }

    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
        this.pnlBottomMenu.setUser(this.user.get());
    }
    
    public void playNotify(Game game, User user) {
        this.setGame(game);
        this.pnlHistory.addInfo(user.getName()+" has play "+game.getCurrentBid().get().getQuantity()+" dices of "+game.getCurrentBid().get().getDiceValue());
    }
}
