package me.theminecoder.appbase.objects;

/**
 * @author theminecoder
 */
public class HostPort {

    private String hostname;
    private int port;

    HostPort(){
    }

    public HostPort(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
