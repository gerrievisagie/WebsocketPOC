package com.eroad.esp.websocket.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class SimpleClient extends WebSocketClient {
    static Logger logger = LoggerFactory.getLogger(SimpleClient.class);
    Scanner scan = new Scanner(System.in);

    public SimpleClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public SimpleClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handShakeData) {
        logger.info("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        logger.info("received message: \n" + message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        logger.info("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        logger.info("an error occurred:" + ex);
    }

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new SimpleClient(new URI("ws://127.0.0.1:8887"));
        client.connect();
        ((SimpleClient)client).start();
    }

    public void start(){
        while(true) {
            printOptions();
            String option = scan.next();
            switch (option) {
                case "0":
                    logger.error("exiting ... ");
                    this.close();
                    System.exit(0);
                    break;
                case "1":getClientIds();
                    break;
                case "2": sendMessageAll();
                    break;
                case "3": sendMessageToClient();
                    break;
                default:
                    logger.error("Option not Supported");
            }
        }
    }

    private void printOptions(){
        logger.info("Please select from the following options");
        logger.info("~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-~-");
        logger.info("1. Get client id's");
        logger.info("2. Send message to all");
        logger.info("3. Send message to specific client");
        logger.info("0. Exit");
    }

    private void sendMessageAll(){
        logger.info("Enter message");
        String message = scan.nextLine();
        send("messageAll:"+message);
        logger.info("Message sent");
    }
    private void sendMessageToClient(){
        getClientIds();
        logger.info("Enter client ID");
        String clientId = scan.next();
        scan.nextLine();
        logger.info("Enter message");
        String message = scan.nextLine();
        send("message:"+clientId+":"+message);
        logger.info("Message sent: {}"+message);
    }

    private void getClientIds(){
        logger.info("Getting List");
        send("list:");
    }

}
