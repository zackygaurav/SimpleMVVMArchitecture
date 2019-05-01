package in.itechvalley.topmovies.util;

public class Constants
{
    public static class StatusCodes
    {
        /*
         * 2XX Success
         * */
        public static final int STATUS_CODE_SUCCESS = 200;

        /*
         * 4XX Client Error
         * */
        public static final int ERROR_CODE_FAILURE_UNKNOWN = 400;
        public static final int ERROR_CODE_UNKNOWN_HOST_EXCEPTION = 401;
        public static final int ERROR_CODE_SOCKET_TIMEOUT = 408;

        /*
         * 5XX Server Errors
         * */
        public static final int ERROR_CODE_RESPONSE_FAILED = 500;
        public static final int ERROR_CODE_RESPONSE_BODY_NULL = 503;
    }
}
