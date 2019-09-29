package sample;
import java.io.*;
import java.net.*;

public class Client {

    int port = 35600;
    Socket socket;
    BufferedReader in;
    BufferedWriter out;


    public void sendmyIP() {
        String broadcastAdrStr = "255.255.255.255";
        try {
            InetAddress broadcastadr = InetAddress.getByName(broadcastAdrStr);
            byte[] myadr = InetAddress.getLocalHost().getAddress();
            DatagramSocket socket = new DatagramSocket(35500);
            socket.setBroadcast(true);
            DatagramPacket dataadr = new DatagramPacket(myadr, 4, broadcastadr, 35500);
            socket.send(dataadr);
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host in broadcast");
        } catch (SocketException e) {
            System.out.println("Socket Exception in broadcast");
        } catch (IOException e) {
            System.out.println("IOException in broadcast");
        }
    }

    public Socket getsock(){
        try {
            ServerSocket ss = new ServerSocket(35600);
            return ss.accept();
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    boolean sendready(BufferedWriter out){
        try {
            out.write("ready\n");
            out.flush();
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    String gettext(BufferedReader in){
        try{
            return in.readLine();
        } catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    void initio(Socket sock){
        try {
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
