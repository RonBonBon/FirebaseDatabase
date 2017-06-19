package hackeru.binyamin.firebasedatabase;

/**
 * A Chat Item.
 * 1) Must have an empty Default Constructor.
 * 2) Must have public getters and setters.
 */

public class ChatItem {
    private String message;
    private String userID;
    private String profileImage;
    private String date;

    //Default Constructor for robots
    public ChatItem() {
    }

    //Constructor
    public ChatItem(String message, String userID, String profileImage, String date) {
        this.message = message;
        this.userID = userID;
        this.profileImage = profileImage;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
