package main.java.de.appdev2.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server<T extends Remote> {
    private final int port;
    private final String name;
    private final String address;
    private final T remoteObject;

    public Server(String address, int port, String name, T skeleton){
        this.port = port;
        this.name = name;
        this.remoteObject = skeleton;
        this.address = address;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public T getRemoteObject() {
        return this.remoteObject;
    }

    public boolean host() {
        String rmiAddress = "rmi://" + this.address + ":" + this.port + "/" + this.name;

        try {
            LocateRegistry.createRegistry(this.port);

            Naming.bind(rmiAddress, this.remoteObject);
        } catch (RemoteException | MalformedURLException | AlreadyBoundException e) {
            e.printStackTrace();

            return false;
        }

        System.out.println("Hosting server under address " + rmiAddress);

        return true;
    }
}
