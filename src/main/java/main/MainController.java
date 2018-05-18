package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;


public class MainController {
    private String serverIp;
    private int serverPort;

    private String getServerIp() {
        return serverIp;
    }

    private void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    private int getServerPort() {
        return serverPort;
    }

    private void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    private int getCurrentVolume() throws IOException {
        String serverResponse;
        Socket clientSocket = new Socket(getServerIp(), getServerPort());
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes("currentVolume" + '\n');
        serverResponse = inFromServer.readLine();
        System.out.println("FROM SERVER: " + serverResponse);
        clientSocket.close();
        return Integer.parseInt(serverResponse);
    }

    private void changeVolume(int volume) throws IOException {
        String newVolumeValue;
        Socket clientSocket = new Socket(getServerIp(), getServerPort());
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        newVolumeValue = volume + "";
        outToServer.writeBytes(newVolumeValue + '\n');
        clientSocket.close();
    }

    public static void main(String[] args) throws Exception {
        MainController mainController = new MainController();
        mainController.setServerPort(6789);
        mainController.setServerIp("localhost");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Enter new volume value: ");
                String input = br.readLine();
                if ("q".equals(input)) {
                    System.out.println("Exit!");
                    System.exit(0);
                } else {
                    System.out.println("Trying to change volume to " + input);

                    try {
                        mainController.changeVolume(Integer.parseInt(input));
                    } catch (SocketException e) {
                        System.out.println("SocketException!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
