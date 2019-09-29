import java.io.File;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    static File[] textlist;

    public static void main(String[] args){
        Broadcastsocket sock = new Broadcastsocket();
        InetAddress firstclientIP = sock.getclientIP();
        InetAddress secondclientIP = sock.getclientIP();
        Socket firstclient = sock.getclientSocket(firstclientIP);
        Socket secondclient = sock.getclientSocket(secondclientIP);
        textlist = new File("./texts/").listFiles();
        Thread race = new Thread(new Race(firstclient, secondclient));
        race.start();
    }
}
