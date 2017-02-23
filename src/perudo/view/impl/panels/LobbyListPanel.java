package perudo.view.impl.panels;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.GUIUtility;
import perudo.view.impl.StandardGUIFactory;

public class LobbyListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;
    private final JPanel pnlExtern;
    private final JPanel pnlLobbyList;
    private final CreateLobbyPanel pnlCreateLobby;
    private final ChangeNamePanel pnlChangeName;

    private final GridBagConstraints cnst;
    private final JButton btnCreateLobby;
    private final JButton btnChangeUsername;
    private Optional<User> user;

    
    public LobbyListPanel() {
        this.setLayout(new BorderLayout());
        this.factory = new StandardGUIFactory();
        this.user = Optional.empty();
        this.pnlCreateLobby = (CreateLobbyPanel) this.factory.createCreateLobbyPanel();
        this.pnlChangeName = (ChangeNamePanel) this.factory.createChangeNamePanel();

        this.pnlLobbyList = this.factory.createPanel();
        this.pnlLobbyList.setLayout(new GridBagLayout());
        pnlExtern = this.factory.createPanel();
        pnlExtern.setLayout(new FlowLayout());
        pnlExtern.add(this.pnlLobbyList);
        this.pnlExtern.setBorder(new TitledBorder("Lobbies list"));
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(5,0,5,0);
        
        JPanel paneldown = this.factory.createPanel();
        paneldown.setLayout(new GridLayout(2,1));
        this.btnCreateLobby = (JButton) this.factory.createButton("Create new lobby");
        this.btnCreateLobby.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(this, this.pnlCreateLobby, CreateLobbyPanel.TITLE, JOptionPane.OK_CANCEL_OPTION,0,GUIUtility.getIcon(CreateLobbyPanel.ICON_RESPATH));
            if(n == JOptionPane.YES_OPTION && this.user.isPresent() && this.pnlCreateLobby.getName().trim().length() > 0) {
                    ControllerSingleton.getController().createLobby(this.user.get(), this.pnlCreateLobby.getGameSettings());
            }
        });
        paneldown.add(this.btnCreateLobby);
        this.btnChangeUsername = (JButton) this.factory.createButton("Change username");
        this.btnChangeUsername.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(this, this.pnlChangeName, ChangeNamePanel.TITLE, JOptionPane.OK_CANCEL_OPTION);
            if(n == JOptionPane.YES_OPTION && this.user.isPresent() && this.pnlChangeName.getName().trim().length() > 0) {
                ControllerSingleton.getController().changeUserName(this.user.get(), this.pnlChangeName.getName());
            }
        });
        paneldown.add(this.btnChangeUsername);
        this.add(new JScrollPane(pnlExtern), BorderLayout.CENTER);
        this.add(paneldown,BorderLayout.SOUTH);
        
    }
    
    public void setUser(User user) {
        this.user = Optional.of(user);
    }

    public void addLobby(final LobbyPanel lobby) {    
        this.pnlLobbyList.add(lobby,cnst);
        this.cnst.gridy++;
    }
    
    public void updateLobby(final Lobby lobby) {
        for (int i = 0; i < this.pnlLobbyList.getComponentCount(); i++) {
            if (((LobbyPanel) this.pnlLobbyList.getComponent(i)).getLobby().equals(lobby)) {
                ((LobbyPanel) this.pnlLobbyList.getComponent(i)).setLobby(lobby);
            }
        }
    }
    
    public void removeLobby(Lobby lobby) {
        for (int i = 0; i < this.pnlLobbyList.getComponentCount(); i++) {
            if (((LobbyPanel) this.pnlLobbyList.getComponent(i)).getLobby().equals(lobby)) {
                this.pnlLobbyList.remove(this.pnlLobbyList.getComponent(i));
                break;
            }
        }
    }

}
