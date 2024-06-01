package org.linkevich;

import org.linkevich.communication.process.ProcessInitiator;
import org.linkevich.communication.process.ProcessPlayer;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Application's mainClass.
 */
public class Main {

    /**
     * Application entry point.
     * Once the process starts, an attempt is made to initialize as a Host.
     * If there is already another Host on the {@link Configuration#DEFAULT_PORT},
     * an attempt made to initialize as a Guest.
     * After the successful initialization as a Guest, communication with the Host begins.
     */
    public static void main(String[] args) {
        try {
            startAsHost();
        } catch (IOException e) {
            if (e instanceof BindException) {
                logDebug("Host already exists!");
                startAsGuest();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates {@link ServerSocket}, instantiates the "responder" Player.
     */
    private static void startAsHost() throws IOException {
        logDebug("Starting as the Host");
        try (ServerSocket serverSocket = new ServerSocket(Configuration.DEFAULT_PORT)) {
            ProcessPlayer processPlayer = new ProcessPlayer("responder", serverSocket.accept());
            logDebug("Host started");
            processPlayer.start();
        }
    }

    /**
     * Creates {@link Socket} and instantiates the "initiator" Player.
     */
    private static void startAsGuest() {
        logDebug("Starting as the Guest");
        try (Socket socket = new Socket(Configuration.DEFAULT_HOST, Configuration.DEFAULT_PORT)) {
            ProcessPlayer processPlayer = new ProcessInitiator("initiator", socket);
            logDebug("Guest started");
            processPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void logDebug(String text) {
        PidLogger.log(text);
    }
}
