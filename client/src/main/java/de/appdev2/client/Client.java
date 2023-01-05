package main.java.de.appdev2.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Einer Client-Klasse, die der Abstraktion des RMI Client-Prozesses (in Registry nach dem Service suchen, Typcasting) dient.
 *
 * @param <T> der Datentyp des Stub für den RMI Client
 */
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

    /**
     * Verbinde zum Server und setze {@link #stub}.
     *
     * @return true, falls es keine Exceptions beim Verbindungsprozess gab.
     */
    public boolean connectToServer() {
        String rmiAddress = "rmi://" + this.address + ":" + this.port + "/" + this.name;

        try {
            /* Service in der Registry wird nachgeschlagen und gecastet */
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
