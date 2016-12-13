var exec = require('cordova/exec');

module.exports = {

    /**
     * Sending an auth request to Xiaomi
     *
     * @example
     * <code>
     * Xiaomi.getAccessToken(function (response) { alert(response.code); });
     * </code>
     */
    getAccessToken: function (scope, state, onSuccess, onError) {
        if (typeof scope == "function") {
            return exec(scope, state, "Xiaomi", "getAccessToken");
        }

        if (typeof state == "function") {
            return exec(state, onSuccess, "Xiaomi", "getAccessToken", [scope]);
        }

        return exec(onSuccess, onError, "Xiaomi", "getAccessToken", [scope, state]);
    },

    getProfile: function (scope, state, onSuccess, onError) {
        if (typeof scope == "function") {
            return exec(scope, state, "Xiaomi", "getProfile");
        }

        if (typeof state == "function") {
            return exec(state, onSuccess, "Xiaomi", "getProfile", [scope]);
        }

        return exec(onSuccess, onError, "Xiaomi", "getProfile", [scope, state]);
    },
};
