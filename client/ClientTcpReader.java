package zadanie_dom.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientTcpReader implements Runnable{
    private final BufferedReader reader;

    public ClientTcpReader(BufferedReader reader){
        this.reader = reader;
    }

    private void handleReader() throws IOException {
        while(true){
            var message = reader.readLine();
            System.out.println(message);
        }
    }

    public void run(){
        try{
            handleReader();
        }catch(IOException ex) {
            System.out.println("Something went wrong at TCP reader. Reason: " + ex.getMessage());
        }
    }
}
