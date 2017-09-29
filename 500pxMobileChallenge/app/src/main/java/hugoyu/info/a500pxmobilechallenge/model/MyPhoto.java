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
    int width, height;

    public MyPhoto(String iso, String shutterSpeed, String aperture,
                   String photoUrl, String userDisplayName, String userPicUrl, int width, int height) {
        this.iso = iso;
        this.shutterSpeed = shutterSpeed;
        this.aperture = aperture;
        this.photoUrl = photoUrl;
        this.userDisplayName = userDisplayName;
        this.userPicUrl = userPicUrl;
        this.width = width;
        this.height = height;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getAspectRatio() {
        return width * 1.0 / height;
    }
}
