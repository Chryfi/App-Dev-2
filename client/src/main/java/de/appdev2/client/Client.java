package main.java.de.appdev2.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class Client<T extends Remote> {
    private T stub;
    private final String address;
    private final String name;
    private final int port;

    public Client(String address, int port, String name) {
        this.address = address;
        this.name = name;
        this.port = port;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    public int getPort() {
        return this.port;
    }

    public T getStub() {
        return this.stub;
    }

    public boolean connectToServer() {
        String rmiAddress = "rmi://" + this.address + ":" + this.port + "/" + this.name;

        try {
            this.stub = (T) Naming.lookup(rmiAddress);

            System.out.println("Verbindung zu " + rmiAddress + " aufgebaut.");

            return true;
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();

            return false;
        } catch (ClassCastException e) {
            System.out.println("Die Klasse konnte nicht gecastet werden.");
            e.printStackTrace();

            return false;
        }
    }
}
