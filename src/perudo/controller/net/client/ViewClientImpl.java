package perudo.controller.net.client;

import java.io.IOException;

import perudo.controller.impl.ViewDecorator;
import perudo.controller.net.DatagramStream;
import perudo.model.User;
import perudo.utility.Response;
import perudo.view.View;

/**
 * A view that intercepts calls to a deeper view. This class is used to set the
 * correct user to the DatagramStream given.
 */
public class ViewClientImpl extends ViewDecorator {

    private final DatagramStream stream;

    /**
     * Create a ViewClientImpl from another View and a DatagramStream.
     * 
     * @param view
     *            the final view
     * @param stream
     *            the stream to set the user
     */
    public ViewClientImpl(final View view, final DatagramStream stream) {
        super(view);
        this.stream = stream;
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        if (user.isOk()) {
            this.stream.setUser(user.getValue());
        }
        super.initializeNewUserRespond(user);
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.stream.close();
    }

}
