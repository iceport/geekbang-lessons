package sun.net.www.protocol.ttl;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new TTLConnection(url);
    }


}

class TTLConnection extends URLConnection {

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected TTLConnection(URL url) {
        super(url);
    }

    @Override
    public void connect() throws IOException {

    }


    @Override
    public Object getContent() throws IOException {
        URL url = this.getURL();
        String path = url.getPath();
        return path.substring(1);
    }
}
