package com.carromroyale.app;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LanBridge {
 private static final int TCP_PORT=37021, UDP_PORT=37020;
 private final WebView web;
 private volatile ServerSocket server;
 private volatile Socket socket;
 private volatile BufferedWriter writer;
 private volatile Thread discoveryThread,acceptThread,advertiseThread;
 private volatile boolean running=false;
 private final AtomicBoolean connected=new AtomicBoolean(false);
 LanBridge(WebView web){this.web=web;}
 private void js(String fn,String value){final String q=JSONObject.quote(value==null?"":value);web.post(()->web.evaluateJavascript("window."+fn+"("+q+")",null));}
 private void status(String s){js("onLanStatus",s);}
 @JavascriptInterface public synchronized void startHosting(){
  stop();running=true;status("HOSTING · WAITING FOR PLAYER");
  advertiseThread=new Thread(()->{try(DatagramSocket udp=new DatagramSocket()){udp.setBroadcast(true);byte[] b=("CARROM_ROYALE|"+TCP_PORT).getBytes("UTF-8");while(running&&!connected.get()){try{udp.send(new DatagramPacket(b,b.length,InetAddress.getByName("255.255.255.255"),UDP_PORT));}catch(Exception ignored){}try{Thread.sleep(850);}catch(InterruptedException ignored){break;}}}catch(Exception e){if(running)status("HOTSPOT ADVERTISEMENT ERROR");}},"CarromAdvertise");advertiseThread.start();
  acceptThread=new Thread(()->{try{server=new ServerSocket();server.setReuseAddress(true);server.bind(new InetSocketAddress(TCP_PORT));Socket s=server.accept();attach(s);}catch(Exception e){if(running)status("HOST ERROR · "+e.getMessage());}},"CarromHost");acceptThread.start();
 }
 @JavascriptInterface public synchronized void startDiscovery(){
  stop();running=true;status("SEARCHING FOR HOST…");
  discoveryThread=new Thread(()->{try(DatagramSocket udp=new DatagramSocket(null)){udp.setReuseAddress(true);udp.bind(new InetSocketAddress(UDP_PORT));udp.setSoTimeout(1200);byte[] buf=new byte[256];while(running&&!connected.get()){DatagramPacket p=new DatagramPacket(buf,buf.length);try{udp.receive(p);String msg=new String(p.getData(),p.getOffset(),p.getLength(),"UTF-8");if(msg.startsWith("CARROM_ROYALE|")){String ip=p.getAddress().getHostAddress();js("onLanPeerFound",ip);status("HOST FOUND · "+ip);return;}}catch(SocketTimeoutException ignored){}}}catch(Exception e){if(running)status("DISCOVERY ERROR · "+e.getMessage());}},"CarromDiscovery");discoveryThread.start();
 }
 @JavascriptInterface public void connectToHost(String ip){if(!running)running=true;new Thread(()->{try{Socket s=new Socket();s.connect(new InetSocketAddress(ip,TCP_PORT),7000);attach(s);}catch(Exception e){status("COULD NOT CONNECT · "+e.getMessage());}},"CarromConnect").start();}
 private synchronized void attach(Socket s)throws Exception{socket=s;socket.setTcpNoDelay(true);writer=new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),"UTF-8"));connected.set(true);status("CONNECTED");js("onLanConnected",s.getInetAddress().getHostAddress());new Thread(()->{try(BufferedReader r=new BufferedReader(new InputStreamReader(s.getInputStream(),"UTF-8"))){String line;while((line=r.readLine())!=null)js("onLanMessage",line);}catch(Exception ignored){}finally{connected.set(false);if(running)status("PLAYER DISCONNECTED");}},"CarromRead").start();}
 @JavascriptInterface public void sendLanMessage(String message){try{BufferedWriter w=writer;if(w!=null&&connected.get())synchronized(w){w.write(message);w.newLine();w.flush();}}catch(Exception e){status("SEND FAILED");}}
 @JavascriptInterface public synchronized void stop(){running=false;connected.set(false);try{if(socket!=null)socket.close();}catch(Exception ignored){}try{if(server!=null)server.close();}catch(Exception ignored){}socket=null;server=null;writer=null;if(discoveryThread!=null)discoveryThread.interrupt();if(acceptThread!=null)acceptThread.interrupt();if(advertiseThread!=null)advertiseThread.interrupt();status("DISCONNECTED");}
}