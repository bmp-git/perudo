package perudo.view.console;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;

import perudo.model.UserType;

/**
 * 
 */
public class BotSelectForm extends BaseForm {

    private UserType type;

    /**
     * 
     * @param textGUI
     *            to do
     */
    public BotSelectForm(final MultiWindowTextGUI textGUI) {
        super(textGUI);
        final Button btnCancel = new Button("Cancel", () -> this.close());
        this.setUserType(null);

        final Panel panel = new Panel(new GridLayout(1));
        final List<Button> btnBots = Arrays.asList(UserType.values()).stream().filter(t -> t.isBot())
                .map(t -> new Button(t.toString(), () -> {
                    this.setUserType(t);
                    this.close();
                })).collect(Collectors.toList());

        btnBots.forEach(b -> panel.addComponent(b));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(btnCancel);

        super.getWindow().setTitle("BOTS");
        super.getWindow().setHints(Arrays.asList(Window.Hint.CENTERED));

        super.getWindow().setComponent(panel);
    }

    private void setUserType(final UserType type) {
        this.type = type;
    }

    /**
     * 
     * @return to do
     */
    public Optional<UserType> getBotType() {
        return Optional.ofNullable(type);
    }

}
