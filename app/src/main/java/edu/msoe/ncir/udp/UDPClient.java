package edu.msoe.ncir.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class UDPClient {
    private static UDPClient myInstance = null;
    private DatagramSocket socket;
    private InetAddress address;
    public int port;

    /**
     * Constructs a UDP client for the device.
     */
    protected UDPClient() {}

    /**
     * Only allows the creation of one UDP Client
     * @returns the singleton UPDClient
     */
    public static synchronized UDPClient getInstance() {
        if (myInstance == null) {
            myInstance = new UDPClient();
        }
        return myInstance;
    }

    /**
     * Constructs the UDP socket
     * @param ipaddr
     * @param port
     */
    public void buildConnection (String ipaddr, int port) {
        this.port = port;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            address = InetAddress.getByName(ipaddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sends the broadcast message for getting the IP address of the device
     * @param msg the message being sent to broadcast
     * @return the reply from the device with the broadcast address and device name
     */
    public String sendBroadcast(String msg){
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
        } catch (SocketException e3) {
            e3.printStackTrace();
        }
        byte[] buffer = msg.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] retBuffer = new byte [100];
        packet = new DatagramPacket(retBuffer, retBuffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    /**
     * sends a simple UDP message from the socket
     * @param msg the string being
     */
    public void send(String msg){
        try {
            socket = new DatagramSocket();
        } catch (SocketException e3) {
            e3.printStackTrace();
        }
        byte[] buffer = msg.getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * receives a simple UDP message from the socket
     * @return the string from the socket
     */
    public String receive() {
        byte[] retBuffer = new byte [100];
        DatagramPacket packet = new DatagramPacket(retBuffer, retBuffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    /**
     * close the udp socket
     */
    public void close() {
        socket.close();
    }


}
