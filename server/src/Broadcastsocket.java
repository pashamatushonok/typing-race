import java.io.IOException;
import java.net.*;

public class Broadcastsocket {

    private DatagramSocket datagramsock;

    public Broadcastsocket(){
        try {
            datagramsock = new DatagramSocket(35500);
            datagramsock.setBroadcast(true);
        } catch (SocketException e){
            e.printStackTrace();
        }
    }

    public InetAddress getclientIP(){
        byte[] buff = new byte[4];
        DatagramPacket packet = new DatagramPacket(buff,4);
        try {
            datagramsock.receive(packet);
        } catch (IOException e){
            System.out.println("Can't get ip adress of client");
        }
        try {
            return InetAddress.getByAddress(packet.getData());
        } catch (UnknownHostException e){
            return null;
        }
    }

    Socket getclientSocket(InetAddress adr){
       try {
           return new Socket(adr, 35600);
       } catch (IOException e){
           return null;
       }
    }


}
