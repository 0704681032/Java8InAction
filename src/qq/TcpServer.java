package qq;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by jyy on 2014/10/21.
 * http://fokman.iteye.com/blog/1076212
 *
 */
public class TcpServer implements Runnable {

    private ServerSocketChannel socketChannel;
    private Selector selector;
    private int activeSockets;
    private int port;

    public TcpServer(){
        activeSockets = 0;
        port = 9999;
        try {
            socketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            ServerSocket socket = socketChannel.socket();
            socket.bind(new InetSocketAddress(port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                selector.select();
            } catch (IOException e) {
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            activeSockets = keys.size();
            if(activeSockets==0){
                continue;
            }

            for(SelectionKey key:keys){
                if(key.isAcceptable()){
                    doServerSocketEvent(key);
                    continue;
                }
                if(key.isReadable()){
                    doClientReadEvent(key);
                    continue;
                }
                if(key.isWritable()){
                    doClientWriteEvent(key);
                    continue;
                }
            }
        }
    }

    private void doServerSocketEvent(SelectionKey key) {
        SocketChannel client = null;
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        try {
            client = server.accept();
            if(client == null){
                return;
            }
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void doClientWriteEvent(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        byte[] data = "0000000000".getBytes();
        buffer.put(data);
        buffer.flip();
        try {
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doClientReadEvent(SelectionKey key) {
        ByteBuffer receiveBuffer;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = (SocketChannel)key.channel();
        if(socketChannel.isOpen()){
            int nBytes = 0;
            buffer.clear();
            try {
                nBytes = socketChannel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.flip();
            if (nBytes > 0) {
                receiveBuffer = ByteBuffer.allocate(nBytes);
                receiveBuffer.clear();
                buffer.get(receiveBuffer.array());
                receiveBuffer.flip();
            }
        }
    }

    public static void main(String[] args) {
        TcpServer server = new TcpServer();
        new Thread(server).start();
    }
}