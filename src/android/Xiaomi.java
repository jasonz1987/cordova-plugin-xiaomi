package com.jasonz.cordova.xiaomi;

import com.xiaomi.account.openauth.XMAuthericationException;
import com.xiaomi.account.openauth.XiaomiOAuthConstants;
import com.xiaomi.account.openauth.XiaomiOAuthFuture;
import com.xiaomi.account.openauth.XiaomiOAuthResults;
import com.xiaomi.account.openauth.XiaomiOAuthorize;

public class Xiaomi extends CordovaPlugin {

    public static final String TAG = "Cordova.Plugin.Xiaomi";

    public static final String XIAOMI_APPID = "wechatappid";
    public static final String XIAOMI_REDIRECT_URL = "http://xiaomi.com";
    private static final String XIAOMI_USER_CANCLE = "cancelled by user";
    private static final String XIAOMI_IO_ERROR = "xiaomi io error";
    private static final String XIAOMI_GET_TOKEN_FAIL = "xiaomi get token fail";
    private static final String XIAOMI_AUTH_ERROR = "xiaomi AUTH error";
    private static final String XIAOMI_NO_TOKEN = "xiaomi no token";

    
    protected CallbackContext currentCallbackContext;
    protected String appId;
    protected String redirectUri;

    XiaomiOAuthResults results;
    

    @Override
    protected void pluginInitialize() {

        super.pluginInitialize();

        instance = this;

        Log.d(TAG, "plugin initialized.");
    }

    public CallbackContext getCurrentCallbackContext() {
        return currentCallbackContext;
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, String.format("%s is called. Callback ID: %s.", action, callbackContext.getCallbackId()));

        if (action.equals("login")) {
            return login(args, callbackContext);
        }
        return false;
    }


    protected boolean getAccessToken(CordovaArgs args, CallbackContext callbackContext) {
        
        
        XiaomiOAuthFuture<XiaomiOAuthResults> future = new XiaomiOAuthorize()
        .setAppId(getAppId())
        .setRedirectUrl(getRedirectUri())
        .setScope([])
        .startGetAccessToken(webView.getContext());
        
        
        
        // run in background
        cordova.getThreadPool().execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    results = future.getResult();
                    
                    if (results.hasError()) {
                        int errorCode = results.getErrorCode();
                        String errorMessage = results.getErrorMessage();
                        callbackContext.error(errorMessage);
                    } else {
                        String accessToken = results.getAccessToken();
                        String macKey = results.getMacKey();
                        String macAlgorithm = results.getMacAlgorithm();
                        PluginResult result = new PluginResult(PluginResult.Status.OK,results.toString());
                        callbackContext.sendPluginResult(result);
                    }
                } catch (IOException e1) {
                    // error
                    currentCallbackContext = null;
                    
                    // send error
                    callbackContext.error(XIAOMI_IO_ERROR);
                } catch (OperationCanceledException e1) {
                    // user cancel
                    // clear callback context
                    currentCallbackContext = null;
                    
                    // send json exception error
                    callbackContext.error(XIAOMI_USER_CANCLE);
                } catch (XMAuthericationException e1) {
                    // error
                    callbackContext.error(XIAOMI_AUTH_ERROR);
                }
            }
        });
        
        sendNoResultPluginResult(callbackContext);

    
        return true;
    }
    
    protected boolean getProfile(CordovaArgs args, CallbackContext callbackContext) {
        
        if(results = null) {
            currentCallbackContext = null;
            callbackContext.error(XIAOMI_NO_TOKEN));
            return false;
        }
        
        // 获取openid
        XiaomiOAuthFuture<String> future = new XiaomiOAuthorize()
        .callOpenApi(MainActivity.this,
                     getAppId(),
                     XiaomiOAuthConstants.OPEN_API_PATH_PROFILE,
                     results.getAccessToken(),
                     results.getMacKey(),
                     results.getMacAlgorithm());
        
        
        // run in background
        cordova.getThreadPool().execute(new Runnable() {
            
            @Override
            public void run() {
                try {
                    XiaomiOAuthResults result = future.getResult();
                    
                    if (result.hasError()) {
                        int errorCode = result.getErrorCode();
                        String errorMessage = result.getErrorMessage();
                    } else {
                        String profile = result.toString();
                        
                        PluginResult result = new PluginResult(PluginResult.Status.OK,profile);
                        callbackContext.sendPluginResult(result);
                        
                    }
                } catch (IOException e1) {
                    // error
                } catch (OperationCanceledException e1) {
                    // user cancel
                } catch (XMAuthericationException e1) {
                    // error
                }
            }
        });

        
    }
    
    
    

    protected String getAppId() {
        if (this.appId == null) {
            this.appId = preferences.getString(XIAOMI_APPID, "");
        }

        return this.appId;
    }
    
    protected String getRedirectUri() {
        if (this.redirectUri == null) {
            this.redirectUri = preferences.getString(XIAOMI_REDIRECT_URL, "");
        }

        return this.redirectUri;
    }
    
    protected  int[] getScopeFromUi() {
        int [] scopes = {XiaomiOAuthConstants.SCOPE_PROFILE,XiaomiOAuthConstants.SCOPE_RELATION,XiaomiOAuthConstants.SCOPE_OPEN_ID,XiaomiOAuthConstants.SCOPE_PHONE};
        return scopes;
    }

    private void sendNoResultPluginResult(CallbackContext callbackContext) {
        // save current callback context
        currentCallbackContext = callbackContext;

        // send no result and keep callback
        PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }
}
