package com.carromroyale.app;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.webkit.WebViewAssetLoader;
public class MainActivity extends Activity {
 private WebView webView; private LanBridge lanBridge;
 @Override protected void onCreate(Bundle savedInstanceState){super.onCreate(savedInstanceState);webView=new WebView(this);setContentView(webView);WebViewAssetLoader loader=new WebViewAssetLoader.Builder().addPathHandler("/assets/",new WebViewAssetLoader.AssetsPathHandler(this)).build();WebSettings s=webView.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setDatabaseEnabled(true);s.setMediaPlaybackRequiresUserGesture(false);s.setSupportZoom(false);s.setBuiltInZoomControls(false);s.setDisplayZoomControls(false);s.setAllowFileAccess(false);s.setAllowContentAccess(false);s.setLoadsImagesAutomatically(true);lanBridge=new LanBridge(webView);webView.addJavascriptInterface(lanBridge,"CarromLAN");webView.setWebChromeClient(new WebChromeClient());webView.setWebViewClient(new WebViewClient(){@Override public android.webkit.WebResourceResponse shouldInterceptRequest(WebView view,WebResourceRequest request){return loader.shouldInterceptRequest(request.getUrl());}});webView.loadUrl("https://appassets.androidplatform.net/assets/index.html");}
 @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
 @Override protected void onDestroy(){if(lanBridge!=null)lanBridge.stop();if(webView!=null){webView.stopLoading();webView.destroy();webView=null;}super.onDestroy();}
}