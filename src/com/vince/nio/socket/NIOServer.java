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
 * 服务器端
 * @author lamp
 *
 */
public class NIOServer {
	
	private static int BLOCK = 1024;
	
	//处理客户端的内部类
	public class HandlerClient{
		
		private FileChannel channel;
		private ByteBuffer buffer;
		private String filePath;
		
		public HandlerClient(String filePath) throws IOException{
			channel = new FileInputStream(filePath).getChannel();
			buffer = ByteBuffer.allocate(BLOCK);
		}
		
		//读取数据
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
	
	//构造一个服务端口，服务管道
	public Selector getSelector(int port) throws IOException{
		ServerSocketChannel server = ServerSocketChannel.open();
		Selector sel = Selector.open();
		server.socket().bind(new InetSocketAddress(port));
		//调整此通道的非阻塞模式。
		server.configureBlocking(false);
		server.register(sel, SelectionKey.OP_ACCEPT);
		return sel;
	}
	
	//服务启动的开始入口
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
	
	//处理事件
	private void handleKey(SelectionKey key)throws IOException{
		if(key.isAcceptable()){//接收请求
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
			
			channel.register(selector, SelectionKey.OP_READ);
			
		}else if(key.isReadable()){//读信息
			SocketChannel channel = (SocketChannel) key.channel();
			int count = channel.read(clientBuffer);
			if(count>0){
				clientBuffer.flip();
			}
		}else if(key.isWritable()){//写事件 
			
		}
	}
	
	
	
	
	public static void main(String[] args) {
		int port = 12345;
		try {
			NIOServer server = new NIOServer(port);
			System.out.println("服务器正在"+port+"端口监听...");
			while(true){
				server.listen();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
