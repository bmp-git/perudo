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

public class BotSelectForm extends BaseForm{

    private final Button btnCancel;
    private final List<Button> btnBots;
    private UserType type;
    private final Panel panel;

    public BotSelectForm(MultiWindowTextGUI textGUI) {
        super(textGUI);
        type = null;

        panel = new Panel(new GridLayout(1));
        btnCancel = new Button("Cancel", () -> this.close());
        btnBots = Arrays.asList(UserType.values()).stream().filter(t -> t.isBot()).map(t -> new Button(t.toString(), () -> {
            this.type = t;
            this.close();
        })).collect(Collectors.toList());

        btnBots.forEach(b -> panel.addComponent(b));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(btnCancel);
        
        this.window.setTitle("BOTS");
        this.window.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        this.window.setComponent(panel);
    }

    public Optional<UserType> getBotType() {
        return Optional.ofNullable(type);
    }

}
