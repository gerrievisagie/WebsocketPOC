package com.eroad.esp.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WebsocketServer extends WebSocketServer {
    static Logger logger = LoggerFactory.getLogger(WebsocketServer.class);
    static Map clientMap = new HashMap<String,WebSocket>();
    Scanner scan = new Scanner(System.in);

    public WebsocketServer(InetSocketAddress address) {
        super(address);
    }

    public static void main(String[] args ) {
        logger.info("Starting websocket server");

        WebSocketServer server = new WebsocketServer(new InetSocketAddress("127.0.0.1", 8887));
        server.run();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("OnOpen called {} {}",webSocket,clientHandshake);
        clientMap.put(String.valueOf(webSocket.hashCode()),webSocket);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("onClose called {} {} {} {}",webSocket,i,s,b);
        clientMap.remove(String.valueOf(webSocket.hashCode()));
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        logger.info("onMessage called {} {} ",webSocket,s);
        messageHandler(webSocket,s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.info("onError called {} {} ",webSocket,e);
    }

    @Override
    public void onStart() {
        logger.info("onStart called");
    }

    private void messageHandler(WebSocket webSocket, String s){
        switch (s.split(":")[0]){
            case "list"         : webSocket.send(new String().join(",",clientMap.keySet()));break;
            case "messageAll"   : clientMap.forEach((key, clientSocket) -> ((WebSocket)clientSocket).send(s.split(":")[1]));break;
            case "message"      :((WebSocket)clientMap.get(String.valueOf(s.split(":")[1]))).send(s.split(":")[2]);break;
            default             : break;
        }
    }
}
