package perudo.view.console;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import perudo.model.Lobby;

public class Utils {
    public static String lobbyToString(final Lobby lobby) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(lobby.getInfo().getName());
        sb.append("\nCapacity: ");
        sb.append(lobby.getUsers().size());
        sb.append(" / ");
        sb.append(lobby.getInfo().getMaxPlayer());
        sb.append("\nInitial dice: ");
        sb.append(lobby.getInfo().getInitialDiceNumber());
        sb.append("\nDice faces: ");
        sb.append(lobby.getInfo().getMaxDiceValue());
        sb.append("\nTurn time: ");
        sb.append(lobby.getInfo().getMaxTurnTime().getSeconds());
        sb.append("s");
        sb.append("\n\nOwner: ");
        sb.append(lobby.getOwner() == null? "EMPTY":lobby.getOwner().getName());
        return sb.toString();
    }
    
    public static void showMessageBox(String title, String message, MultiWindowTextGUI textGUI){
        new MessageDialogBuilder().setTitle(title).setText(message).addButton(MessageDialogButton.OK).build()
        .showDialog(textGUI);
    }
}
