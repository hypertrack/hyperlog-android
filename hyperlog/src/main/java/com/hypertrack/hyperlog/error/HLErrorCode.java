package com.hypertrack.hyperlog.error;

/**
 * Created by piyush on 27/02/17.
 */

public class HLErrorCode {

    public enum Type {
        NETWORK_DISABLED,
        NETWORK_UNAVAILABLE,
        UNHANDLED_ERROR,
        PARSE_ERROR,
        AUTH_ERROR
    }

    public class Code {

        public static final short NETWORK_DISABLED_ERROR = 107;
        public static final short NETWORK_UNAVAILABLE_ERROR = 108;

        public static final short UNHANDLED_ERROR = 151;
        public static final short OTHER_ERROR = 152;
        public static final short NO_RESPONSE_ERROR = 153;
        public static final short PARSE_ERROR = 154;
        public static final short AUTH_ERROR = 155;

        /**
         * API Level ErrorCodes
         */
        public static final short BAD_REQUEST = 400;
        public static final short AUTHORIZATION_TOKEN_NOT_PROVIDED = 401;
        public static final short FORBIDDEN_REQUEST = 403;
        public static final short NOT_FOUND = 404;
        public static final short NOT_ACCEPTABLE = 406;
        public static final short REQUEST_TIMEOUT = 408;
        public static final short GONE = 410;
        public static final short TOO_MANY_REQUESTS = 429;

        public static final short INTERNAL_SERVER_ERROR = 500;
        public static final short NOT_IMPLEMENTED_ON_SERVER = 501;
        public static final short BAD_GATEWAY = 502;
        public static final short SERVICE_UNAVAILABLE = 503;
        public static final short GATEWAY_TIMEOUT = 504;
    }

    public class Message {
        public static final String NETWORK_DISABLED_ERROR =
                "Network Connection disabled. Please check your network connectivity and try again.";

        public static final String NETWORK_UNAVAILABLE_ERROR =
                "Network Connection unstable or unavailable. Please check your network connectivity and try again.";

        public static final String UNHANDLED_ERROR = "Something went wrong. Try again later";

        public static final String NO_RESPONSE_ERROR =
                "There was no response from the server. Please try again in sometime.";

        public static final String PARSE_ERROR = "Error in parsing the response.";

        public static final String BAD_REQUEST
                = "The request could not be understood by the server due to malformed syntax.";
        public static final String AUTHORIZATION_KEY_NOT_PROVIDED
                = "Unauthorized. Verify your API key. You must use a valid publishable key. Refer to https://docs.hypertrack.com/gettingstarted/authentication.html for more information";
        public static final String FORBIDDEN_REQUEST
                = "You do not have permission to access the resource.";
        public static final String NOT_FOUND = "Not Found: The resource does not exist.";
        public static final String METHOD_NOT_ALLOWED
                = "You tried to access a resource with an invalid method.";
        public static final String NOT_ACCEPTABLE
                = "You requested a format that is not json.";
        public static final String REQUEST_TIMEOUT
                = "The request timed out. Please try again.";
        public static final String PAYMENT_REQUIRED
                = "Payment needs to be enabled for using Actions. Refer to https://docs.hypertrack.com/gettingstarted/pricing.html and " +
                "look for 'Test Environment' section for more information about test environment.";
        public static final String TOO_MANY_REQUESTS
                = "You have hit the rate limit for your account.";
        public static final String GONE
                = "The requested resource has been removed from our servers.";
        public static final String INTERNAL_SERVER_ERROR
                = "There was an error on the server and we have been notified. Try again later.";
        public static final String SERVICE_UNAVAILABLE
                = "We are temporarily offline for maintenance. Please try again later.";
    }
}
