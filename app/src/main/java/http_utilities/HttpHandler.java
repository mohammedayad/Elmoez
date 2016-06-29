package http_utilities;

/**
 * Created by 3yad on 25/03/2016.
 */
public interface HttpHandler {
    public static final String ip_address="http://192.168.43.197:8084";
    public static final String TAG = "Elmoez";
    public static final String SIGNUP_URL = ip_address+"/ElmoezWebService/services/elmoez/signUp";
    public static final String LOGIN_URL = ip_address+"/ElmoezWebService/services/elmoez/logIn";
    public static final String EDIT_USERNAME_URL = ip_address+"/ElmoezWebService/services/elmoez/username";
    public static final String EDIT_PASSWORD_URL = ip_address+"/ElmoezWebService/services/elmoez/password";
    public static final String EDIT_IMAGE_URL = ip_address+"/ElmoezWebService/services/elmoez/image";
    public static final String REMOVE_IMAGE_URL = ip_address+"/ElmoezWebService/services/elmoez/removeimage";
    public static final String URL_FEED = "http://192.168.43.197:8084/ElmoezWebService/services/elmoez/feeds";
    public static final String UPLOAD_URL="http://192.168.43.197:8084/ElmoezWebService/services/elmoez/uploadFeedComponentWithImage";
    public static final String UPLOAD_URL_WITHOUT_IMAGE="http://192.168.43.197:8084/ElmoezWebService/services/elmoez/uploadFeedComponentWithoutImage";
    public static final String userImagesUrl="http://192.168.43.197:8084/ElmoezWebService/images/";
    public static final String EditName_URL=ip_address+"/ElmoezWebService/services/elmoez/username";
    final static String UPLOAD_PROFILE_PICTURE="http://192.168.43.197:8084/ElmoezWebService/services/elmoez/image";
    public static final int CAPTURE_IMAGE = 208;
    public static final int PICK_IMAGE = 209;

}
