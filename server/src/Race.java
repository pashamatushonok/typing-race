import java.io.*;
import java.net.Socket;
import java.util.Random;

public class Race implements Runnable {

    Socket firstsock;
    Socket secsock;

    BufferedReader infirst;
    BufferedReader insecond;
    BufferedWriter outfirst;
    BufferedWriter outsecond;
    File filetext;

    public Race(Socket sock1, Socket sock2) {
        this.firstsock = sock1;
        this.secsock = sock2;
        try {
            this.infirst = new BufferedReader(new InputStreamReader(firstsock.getInputStream()));
            this.insecond = new BufferedReader(new InputStreamReader(secsock.getInputStream()));
            this.outfirst = new BufferedWriter(new OutputStreamWriter(firstsock.getOutputStream()));
            this.outsecond = new BufferedWriter(new OutputStreamWriter(secsock.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (waitclient(infirst)) {
        }
        while (waitclient(insecond)) {
        }
        Random rand = new Random();
        int textnum = rand.nextInt(Main.textlist.length);
        //String textname = Integer.toString(textnum) + ".txt";
        filetext = Main.textlist[textnum];
        if (sendtext(filetext, outfirst) && sendtext(filetext, outsecond)) {
            System.out.println("text sends succesfully");
        } else {
            System.out.println("text send error");
        }
        choosewinner();

    }

    protected boolean waitclient(BufferedReader in) {
        String message;
        try {
            message = in.readLine();
        } catch (IOException e) {
            return false;
        }
        if (message.equals("ready")) {
            return false;
        } else {
            return true;
        }
    }

    boolean sendtext(File filetext, BufferedWriter out) {
        String text = getfilecontent(filetext.getPath());
        try {
            out.write(text);
            out.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    String getfilecontent(String path) {
        BufferedReader filereader = null;
        try {
            filereader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        try {
            String line = filereader.readLine();
            while (line != null) {
                sb.append(line).append("\n");
                line = filereader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    void choosewinner() {
        int firstclienttime = 0;
        int secondtclienttime = 0;
        try {
            firstclienttime = Integer.parseInt(infirst.readLine());
            secondtclienttime = Integer.parseInt(insecond.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (firstclienttime < secondtclienttime) {
            try {
                outfirst.write("you win");
                outsecond.write("you loose");
                outfirst.flush();
                outsecond.flush();
            } catch (IOException e){e.printStackTrace();}
        }
        if (firstclienttime > secondtclienttime){
            try {
                outfirst.write("you loose");
                outsecond.write("you win");
                outfirst.flush();
                outsecond.flush();
            } catch (IOException e){e.printStackTrace();}
        }
    }
}
