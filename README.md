# cordova-plugin-xiaomi

A cordova plugin, a JS version of Xiaomi OAUTH SDK

# Feature

getAccessToken,getProfile,getOpenId

# Example



# Install

1. ```cordova plugin add cordova-plugin-xiaomi  --variable xiaomiappid=YOUR_XIAOMI_APPID```, or using [plugman](https://npmjs.org/package/plugman), [phonegap](https://npmjs.org/package/phonegap), [ionic](http://ionicframework.com/)

2. ```cordova build ios``` or ```cordova build android```

3. (iOS only) if your cordova version <5.1.1,check the URL Type using XCode

# Usage

## getAccessToken
```Javascript
Xiaomi.getAccessToken(function (result) {
    console.log(result)
}, function (error) {
    console.log(error);
});
```

## getOpenId 

** should call this method after getAccessToken **

```Javascript
Xiaomi.getOpenId(function (result) {
    console.log(result)
}, function (error) {
    console.log(error);
});
```

## getProfile

** should call this method after getAccessToken **

```Javascript
Xiaomi.getProfile(function (result) {
    console.log(result)
}, function (error) {
    console.log(error);
});
```



# FAQ

See [FAQ](https://github.com/jasonz1987/cordova-plugin-xiaomi/wiki/FAQ).

# TODO

+ add example

+ support ios platform

+ add other sdk

# More

[Xiaomi Official Android SDK](https://github.com/xiaomipassport/oauth-Android-sdk)

[Xiaomi Official IOS SDK](https://github.com/xiaomipassport/oauth-iOS-sdk)

# LICENSE

[MIT LICENSE](http://opensource.org/licenses/MIT)
