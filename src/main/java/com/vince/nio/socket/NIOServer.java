package com.vince.nio.socket;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * ��������
 * @author lamp
 *
 */
public class NIOServer {
	
	private static int BLOCK = 1024;
	
	//����ͻ��˵��ڲ���
	public class HandlerClient{
		
		private FileChannel channel;
		private ByteBuffer buffer;
		private String filePath;
		
		public HandlerClient(String filePath) throws IOException{
			channel = new FileInputStream(filePath).getChannel();
			buffer = ByteBuffer.allocate(BLOCK);
		}
		
		//��ȡ����
		public ByteBuffer readBlock(){
			buffer.clear();
			try {
				int count = channel.read(buffer);
				buffer.flip();
				if(count <= 0){
					return null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return buffer;
		}
		public void close(){
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Selector selector;
	private String filename = "d:\\vince\\cc.jpg";
	private ByteBuffer clientBuffer = ByteBuffer.allocate(BLOCK);
	public NIOServer(int port) throws IOException{
		selector = this.getSelector(port);
	}
	
	//����һ������˿ڣ�����ܵ�
	public Selector getSelector(int port) throws IOException{
		ServerSocketChannel server = ServerSocketChannel.open();
		Selector sel = Selector.open();
		server.socket().bind(new InetSocketAddress(port));
		//������ͨ���ķ�����ģʽ��
		server.configureBlocking(false);
		server.register(sel, SelectionKey.OP_ACCEPT);
		return sel;
	}
	
	//���������Ŀ�ʼ���
	public void listen(){
		
		try{
			while(true){
				selector.select();
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				while(iter.hasNext()){
					SelectionKey key = iter.next();
					iter.remove();
					handleKey(key);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//�����¼�
	private void handleKey(SelectionKey key)throws IOException{
		if(key.isAcceptable()){//��������
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
			
			channel.register(selector, SelectionKey.OP_READ);
			
		}else if(key.isReadable()){//����Ϣ
			SocketChannel channel = (SocketChannel) key.channel();
			int count = channel.read(clientBuffer);
			if(count>0){
				clientBuffer.flip();
			}
		}else if(key.isWritable()){//д�¼� 
			
		}
	}
	
	
	
	
	public static void main(String[] args) {
		int port = 12345;
		try {
			NIOServer server = new NIOServer(port);
			System.out.println("����������"+port+"�˿ڼ���...");
			while(true){
				server.listen();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
