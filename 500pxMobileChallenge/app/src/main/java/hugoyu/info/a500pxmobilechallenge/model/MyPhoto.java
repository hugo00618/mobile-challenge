package hugoyu.info.a500pxmobilechallenge.model;

/**
 * Created by Hugo on 2017-09-28.
 */

public class MyPhoto {

    private String iso;
    private String shutterSpeed;
    private String aperture;
    private String photoUrl;
    private String userDisplayName;
    private String userPicUrl;

    public MyPhoto(String iso, String shutterSpeed, String aperture,
                   String photoUrl, String userDisplayName, String userPicUrl) {
        this.iso = iso;
        this.shutterSpeed = shutterSpeed;
        this.aperture = aperture;
        this.photoUrl = photoUrl;
        this.userDisplayName = userDisplayName;
        this.userPicUrl = userPicUrl;
    }

    public String getIso() {
        return iso;
    }

    public String getShutterSpeed() {
        return shutterSpeed;
    }

    public String getAperture() {
        return aperture;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }
}
