package perudo.view.console.test;

import java.io.IOException;
import java.util.Arrays;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.controller.net.NetworkControllerImpl;
import perudo.controller.net.NetworkServerListener;
import perudo.controller.net.TcpIPv4ServerListener;
import perudo.controller.net.client.ControllerClientImpl;
import perudo.utility.impl.LoggerSingleton;
import perudo.view.console.ViewImpl;

public class RemoteApp {

    private static String[] arguments;

    public static void main(String[] args) throws IOException, InterruptedException {
        LoggerSingleton.get().enable();
        NetworkServerListener listener = null;
        Controller controller = null;
        args = new String[] { "2.224.173.8", "45555" };
        if (args.length < 2) {
            int port = 45555;
            listener = TcpIPv4ServerListener.startNewServerListener(port, Arrays.asList());
            controller = NetworkControllerImpl.newNetworkController(StandardControllerImpl.newStandardControllerImpl(),
                    listener);

            args = new String[] { "127.0.0.1", Integer.toString(port) };
        }
        arguments = args;

        Thread th = new Thread(() -> {
            try {
                runGUI();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        th.start();
        Thread th1 = new Thread(() -> {
            try {
                runGUI();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        th1.start();
        Thread th2 = new Thread(() -> {
            try {
                runGUI();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        th2.start();

        th.join();
        th1.join();
        th2.join();

        System.out.println("bye");
        if (listener != null) {
            listener.close();
        }
        if (controller != null) {
            controller.close();
        }
        System.out.println("bye");
    }

    private static void runGUI() throws IOException {
        ControllerClientImpl controller = (ControllerClientImpl) ControllerClientImpl.createFromServerName(arguments[0],
                Integer.parseInt(arguments[1]));

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        ViewImpl view = new ViewImpl(screen, controller);
        view.waitEnd();
        screen.stopScreen();
        controller.close();
    }

}
