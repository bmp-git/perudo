package perudo.controller.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

import perudo.controller.Controller;
import perudo.controller.net.Datagram;
import perudo.controller.net.DatagramStream;
import perudo.controller.net.DatagramStreamImpl;
import perudo.controller.net.MethodInvoker;
import perudo.controller.net.NetworkServerListener;
import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorTypeException;
import perudo.view.View;

public class NetworkControllerImpl implements Controller {
    private final Controller controller;
    private final MethodInvoker methodInvoker;
    private final List<DatagramStream> streams;
    private final BiConsumer<InputStream, OutputStream> datagramStreamCreator;
    private final BiConsumer<Datagram, DatagramStream> invoker;

    public static Controller newNetworkController(Controller controller, NetworkServerListener serverListener) {
        return new NetworkControllerImpl(controller, serverListener);
    }
    
    private NetworkControllerImpl(Controller controller, NetworkServerListener serverListener) {
        this.controller = controller;
        this.methodInvoker = new MethodInvoker(Controller.class);
        this.streams = new CopyOnWriteArrayList<>();

        this.invoker = (dg, dgs) -> {
            try {
                if (dg.getMethodName().contains("initializeNewUser")) {
                    controller.initializeNewUser(NetworkViewImpl.newNetworkView(dgs));
                } else {
                    methodInvoker.execute(controller, dg);
                }
            } catch (ErrorTypeException e) {
                e.printStackTrace();
            }
        };

        this.datagramStreamCreator = (is, os) -> {
            try {
                final DatagramStream datagramStream = DatagramStreamImpl.initializeNewDatagramStream(is, os,
                        Arrays.asList(this.invoker));
                this.streams.add(datagramStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        serverListener.addNewConnectionObserver(this.datagramStreamCreator);

    }

    @Override
    public void initializeNewUser(View view) {
        this.controller.initializeNewUser(view);

    }

    @Override
    public void changeUserName(User user, String name) {
        this.controller.changeUserName(user, name);

    }

    @Override
    public void getUsers(User user) {
        this.controller.getUsers(user);

    }

    @Override
    public void createLobby(User user, GameSettings info) {
        this.controller.createLobby(user, info);

    }

    @Override
    public void getLobbies(User user) {
        this.controller.getLobbies(user);

    }

    @Override
    public void joinLobby(User user, Lobby lobby) {
        this.controller.joinLobby(user, lobby);

    }

    @Override
    public void exitLobby(User user) {
        this.controller.exitLobby(user);

    }

    @Override
    public void startLobby(User user) {
        this.controller.startLobby(user);

    }

    @Override
    public void getGames(User user) {
        this.controller.getGames(user);

    }

    @Override
    public void play(User user, Bid bid) {
        this.controller.play(user, bid);

    }

    @Override
    public void doubt(User user) {
        this.controller.doubt(user);

    }

    @Override
    public void urge(User user) {
        this.controller.urge(user);

    }

    @Override
    public void callPalifico(User user) {
        this.controller.callPalifico(user);

    }

    @Override
    public void exitGame(User user) {
        this.controller.exitGame(user);

    }

    @Override
    public void close(User user) {
        this.controller.close(user);

    }

}
