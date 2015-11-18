package bam.bam.bam.modeles;

/**
 * classe stoquant les informations d'un utilisateur
 *
 * @author Marc
 */
public class User {

    /**
     * id de l'appareil
     */
    private final String user_device_id;

    /**
     * id
     */
    private int id;

    /**
     * pseudo
     */
    private String user_pseudo;

    /**
     * numéro de téléphone
     */
    private String user_phone_number;

    /**
     * photo
     */
    private String photo_data ;

    public User(int id, String user_pseudo,String user_phone_number,String photo_data ,String user_device_id) {

        this.id = id;
        this.photo_data  = photo_data ;
        this.user_pseudo = user_pseudo;
        this.user_phone_number = user_phone_number;
        this.user_device_id = user_device_id;
    }

    public User(String user_pseudo,String user_phone_number,String photo_data ,String user_device_id) {

        this.id = -1;
        this.photo_data  = photo_data ;
        this.user_pseudo = user_pseudo;
        this.user_phone_number = user_phone_number;
        this.user_device_id = user_device_id;
    }

    public int getId() {
        return id;
    }

    public String getPhoto_data () {
        return photo_data ;
    }

    public void setPhoto_data (String photo_data ) {
        this.photo_data  = photo_data ;
    }

    public String getUser_pseudo() {
        return user_pseudo;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_device_id() {
        return user_device_id;
    }
}
