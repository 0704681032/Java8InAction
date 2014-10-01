package com.vince.nio.socket;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {

	
	private static int bufferSize = 1024;
	static InetSocketAddress addr = new InetSocketAddress("localhost",12345);
	
	static class Download implements Runnable{
		
		String outfile = "c:\\test.jpg";
		@Override
		public void run() {
			try {
				FileOutputStream fout = new FileOutputStream(outfile);
				
				SocketChannel client = SocketChannel.open();
				client.configureBlocking(false);
				Selector selector = Selector.open();
				client.register(selector, SelectionKey.OP_CONNECT);
				
				client.connect(addr);
				
				
				ByteBuffer buf = ByteBuffer.allocate(bufferSize);
				int total = 0;
				while(true){
					selector.select();
					Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
					while(iter.hasNext()){
						SelectionKey key = iter.next();
						iter.remove();
						if(key.isConnectable()){
							SocketChannel channel = (SocketChannel)key.channel();
							//�жϴ�ͨ�����Ƿ����ڽ������Ӳ�����
							if(channel.isConnectionPending()){
								//����׽���ͨ�������ӹ���
								channel.finishConnect();
								channel.write(buf);
								channel.register(selector, SelectionKey.OP_READ);
							}else if(key.isReadable()){
								channel = (SocketChannel)key.channel();
								int count = channel.read(buf);
								System.out.println(count);
								if(count>0){
									total+=count;
									buf.clear();
									if(count<bufferSize){
										byte[] overByte = new byte[count];
										for (int i = 0; i < count; i++) {
											overByte[i] = buf.get(i);
										}
										fout.write(overByte);
									}else{
										fout.write(buf.array());
									}
								}else{
									client.close();
								}
							}
						}
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) {

	}

}
