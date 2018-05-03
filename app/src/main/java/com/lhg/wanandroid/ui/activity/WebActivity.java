package com.lhg.wanandroid.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lhg.wanandroid.R;
import com.lhg.wanandroid.base.BaseActivity;

public class WebActivity extends BaseActivity {

    private Toolbar toolbar;
    private WebView web;
    private ProgressBar progressBar;
    private String path;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        super.initView();
        toolbar = findViewById(R.id.toolbar);
        web = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress);
    }

    @Override
    protected void loadData() {
        super.loadData();
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getStringExtra("url");
        }
        web.loadUrl(path);
        toolbar.setTitle("载入中..");
        initWebClient();
        initWebSettings();
    }


    private void initWebClient() {
        web.setWebViewClient(new MyWebViewClient());
        web.setWebChromeClient(new MyWebChromeClient());
        String url = web.getUrl();
        Log.e("WEB", "当前页面的URL为：" + url);
        // web.goBackOrForward(3);
        // Log.e("WEB", "当前页面能否前进或者后退3步：" + web.canGoBackOrForward(3));
        // web.destroy(); 销毁当前webView，但是是在容器中将其移除之后
        // web.getScrollY(); 此方法返回的是当前页面顶端距离网页真实顶端的距离
        // web.getHeight(); 此方法返回的是webView的控件高度
        // web.getContentHeight(); 此方法返回的是整个HTML页面的高度


        //判断web是否到达底部
        if (web.getContentHeight() * web.getScaleY() == web.getHeight() + web.getScrollY()) {
            //到达底部
            Log.e("WEB", "已经到达底部");
        }
        if (web.getScrollY() == 0) {
            //处于顶端
            Log.e("WEB", "已经到达顶部");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
                return true;
            } else if (web.canGoBack()) {
                web.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initListener() {
        super.initListener();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWebSettings() {
        WebSettings settings = web.getSettings();
        //设置web支持JS
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //默认false，设置WebView是否可以由JavaScript自动打开窗口，通常与JavaScript的window.open()配合使用。
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //启用或禁用WebView访问文件数据
        settings.setAllowFileAccess(true);
        //禁止或允许WebView从网络上加载图片。从禁止到允许的转变的话，在WebView调用reload()的时候才会生效。
        settings.setBlockNetworkImage(false);
        //设置界面是否支持缩放
        settings.setSupportZoom(true);
        //显示或不显示缩放按钮
        settings.setBuiltInZoomControls(true);
        //设置是否支持多窗口
        settings.setSupportMultipleWindows(true);
        //指定WebView的页面布局显示形式，调用该方法会引起页面重绘。默认值为LayoutAlgorithm#NARROW_COLUMNS。
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //通知WebView是否需要设置一个节点获取焦点当WebView.requestFocus(int,android.graphics.Rect)被调用时，默认为true。
        settings.setNeedInitialFocus(true);
        //启用或者禁用缓存功能
        settings.setAppCacheEnabled(true);
        //设置缓存路径，且setAppCacheEnabled必须设置为true
        settings.setAppCachePath(web.getContext().getCacheDir().getAbsolutePath());

        //设置缓存模式
        //LOAD_DEFAULT 默认的缓存模式，在进行页面前进或后退的操作时，如果缓存可用并未过期就优先加载缓存，否则从网络上加载数据。
        //LOAD_CACHE_ELSE_NETWORK 只要缓存可用就加载缓存，不管它们是不是过期。如果缓存不可用就从网络上加载数据。
        //LOAD_NO_CACHE 不加载缓存，只从网络获取数据
        //LOAD_CACHE_ONLY 不从网络加载数据，只从缓存加载数据。
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //是否启用数据库缓存
        settings.setDatabaseEnabled(true);
        //启用或禁用DOM缓存
        settings.setDomStorageEnabled(true);
        //settings.setUserAgentString(String ua);设置WebView的UserAgent值。

        //设置编码格式，通常都设为“UTF-8”。
        settings.setDefaultTextEncodingName("utf-8");
        //设置标准的字体族，默认“sans-serif”。
        settings.setStandardFontFamily("sans-serif");
        //设置草书字体族，默认“cursive”。
        settings.setCursiveFontFamily("cursive");
        //设置CursiveFont字体族，默认“cursive”。
        settings.setFantasyFontFamily("cursive");
        //设置混合字体族，默认“monospace”。
        settings.setFixedFontFamily("monospace");
        //设置梵文字体族，默认“sans-serif”
        settings.setSansSerifFontFamily("sans-serif");
        //设置衬线字体族，默认“sans-serif”
        settings.setSerifFontFamily("sans-serif");
        //设置默认填充字体大小，默认16，取值区间为[1-72]，超过范围，使用其上限值。
        settings.setDefaultFixedFontSize(16);
        //设置默认字体大小，默认16，取值区间[1-72]，超过范围，使用其上限值。
        settings.setDefaultFontSize(16);
        //设置最小字体，默认8. 取值区间[1-72]，超过范围，使用其上限值。
        settings.setMinimumFontSize(8);
        //设置最小逻辑字体，默认8. 取值区间[1-72]，超过范围，使用其上限值。
        settings.setMinimumLogicalFontSize(8);

        /**
         * 缓存策略
         */
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null)
            if (networkInfo.isAvailable()) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);//网络正常时使用默认缓存策略
            } else {
                settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);//网络不可用时只使用缓存
            }
    }

    /**
     * 处理各种通知、请求事件，这个是与通信有关的Client
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            //该方法在加载页面资源时会回调，每一个资源（比如图片）的加载都会调用一次。
            //url为图片的地址
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //该方法在WebView开始加载页面且仅在Main frame loading（即整页加载）时回调，一次Main frame的加载只会回调该方法一次。
            //我们可以在这个方法里设定开启一个加载的动画，告诉用户程序在等待网络的响应。
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //该方法只在WebView完成一个页面加载时调用一次（同样也只在Main frame loading时调用）
            //我们可以可以在此时关闭加载动画，进行其他操作。
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //该方法在web页面加载错误时回调，这些错误通常都是由无法与服务器正常连接引起的，最常见的就是网络问题。
            //但类似于服务器返回错误码的那种错误（即HTTP ERROR），是不会回调的
            //这个是API23引进的新方法，在页面局部加载发生错误时也会被调用
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            //当服务器返回一个HTTP ERROR并且它的status code>=400时，该方法便会回调。
            //这个方法的作用域并不局限于Main Frame，任何资源的加载引发HTTP ERROR都会引起该方法的回调

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            //当WebView加载某个资源引发SSL错误时会回调该方法，这时WebView要么执行handler.cancel()取消加载，
            //要么执行handler.proceed()方法继续加载（默认为cancel）
            //安装证书不安全，此时将证书不安全的因素全部忽略掉
            if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                    || error.getPrimaryError() == SslError.SSL_EXPIRED
                    || error.getPrimaryError() == SslError.SSL_INVALID
                    || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {

                handler.proceed();
            } else {
                handler.cancel();
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
            //当WebView需要请求某个数据时，这个方法可以拦截该请求来告知app并且允许app本身返回一个数据来替代我们原本要加载的数据。
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // WebView如果要加载一个url会向ActivityManager寻求一个适合的处理者来加载该url（比如系统自带的浏览器）
            // 此方法会在本App中添加一个内置“浏览器”来加载url
            String url;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            } else {
                url = request.toString();
            }
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return false;
            }
            return true;
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            //当WebView得页面Scale值发生改变时回调。
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
            //默认值为false，重写此方法并return true可以让我们在WebView内处理按键事件。
        }

    }

    /**
     * 辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等偏外部事件的“外交大臣”，这个是与界面显示有关的Client
     */
    private class MyWebChromeClient extends WebChromeClient {

        private Bitmap mDefaultVideoPoster;//默认的视频展示图

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            //当页面加载的进度发生改变时回调，用来告知主程序当前页面的加载进度。
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            //用来接收web页面的icon，我们可以在这里将该页面的icon设置到Toolbar。
            //toolbar.setLogo(new BitmapDrawable(icon));
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            //用来接收web页面的title，我们可以在这里将页面的title设置到Toolbar。
            toolbar.setTitle(title);
        }

        /**
         * 以下两个方法是为了支持web页面进入全屏模式而存在的（比如播放视频），如果不实现这两个方法，该web上的内容便不能进入全屏模式。
         */
        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            super.onShowCustomView(view, requestedOrientation, callback);
            //该方法在当前页面进入全屏模式时回调，主程序必须提供一个包含当前web内容（视频 or Something）的自定义的View。
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            //该方法在当前页面退出全屏模式时回调，主程序应在这时隐藏之前show出来的View。
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            //当我们的Web页面包含视频时，我们可以在HTML里为它设置一个预览图，WebView会在绘制页面时根据它的宽高为它布局。
            //需要重写该方法，在我们尚未获取web页面上的video预览图时，给予它一个本地的图片，避免空指针的发生。
            if (mDefaultVideoPoster == null) {
                mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                return mDefaultVideoPoster;
            }
            return super.getDefaultVideoPoster();
        }

        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
            //重写该方法可以在视频loading时给予一个自定义的View，可以是加载圆环 or something。
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
            //处理Javascript中的Alert对话框。
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
            //处理Javascript中的Prompt对话框
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
            //处理Javascript中的Confirm对话框
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            //该方法在用户进行了web上某个需要上传文件的操作时回调。我们应该在这里打开一个文件选择器
            //如果要取消这个请求我们可以调用filePathCallback.onReceiveValue(null)并返回true。
        }

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            super.onPermissionRequest(request);
            //该方法在web页面请求某个尚未被允许或拒绝的权限时回调，主程序在此时调用grant(String [])或deny()方法。
            // 如果该方法没有被重写，则默认拒绝web页面请求的权限。
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
            //该方法在web权限申请权限被取消时回调，这时应该隐藏任何与之相关的UI界面。
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
        web.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        web.onPause();
        web.pauseTimers();
    }

    private void JsInteraction() {
        /**
         * JS与WebView交互基本分为两个方面：
         *
         * 1.WebView调用JS
         * 2.JS调用WebView
         */

        /*
        <script type="text/javascript">
            function readyToGo() {
                alert("Hello")
            }
            function alertMessage(message) {
                alert(message)
            }
            function getYourCar(){
                return "Car";
            }
        </script>
         */
        /**
         * 基本格式为webView.loadUrl("javascript:methodName(parameterValues)");
         */
        //WebView调用JavaScript无参无返回值函数
        web.loadUrl("javascript:readyToGo()");
        //WebView调用JavScript有参无返回值函数
        web.loadUrl("javascript:alertMessage(\"" + "content" + "\")");
        //WebView调用JavaScript有参数有返回值的函数
        evaluateJavaScript(web);

        //JavaScript通过WebView调用Java代码
        //1.编写Java原生方法并用使用@JavascriptInterface注解
        //2.注册JavaScriptInterface
        web.addJavascriptInterface(this, "android");
        //3.编写JavaScript代码
        /*
            function toastClick(){
                window.android.show("JavaScript called~!");
            }
        */
        // 带有返回值也是一样 例如：
        /*
            function showHello(){
                var str=window.Android.getMessage();
                console.log(str);
            }
         */

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void evaluateJavaScript(WebView webView) {
        webView.evaluateJavascript("getYourCar()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.d("findCar", s);
            }
        });
    }

    @JavascriptInterface
    public void show(String s) {
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }
}
