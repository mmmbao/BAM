package bam.bam.bam.modeles;

import java.util.Calendar;

import bam.bam.utilities.Utility;

/**
 * classe stoquant les informations d'un bam
 *
 * @author Marc
 */
public class Bam {

    /**
     * id du bam
     */
    private int id;

    /**
     * desciption du bam
     */
    private String bam_description;

    /**
     * titre du bam
     */
    private String bam_title;

    /**
     * prix du bam
     */
    private float bam_price;

    /**
     * state du bam
     */
    private int bam_state;

    /**
     * latitude du bam
     */
    private double bam_latitude_position;

    /**
     * longitude du bam
     */
    private double bam_longitude_position;

    /**
     * date de création du bam
     */
    private String bam_creation_date;

    /**
     * date de fin du bam
     */
    private String bam_end_date;

    /**
     * utilisateur du bam
     */
    private int bam_user_id;

    public Bam(int id, String bam_description, String bam_title, float bam_price,int bam_state,
               double bam_latitude_position, double bam_longitude_position,
               String bam_creation_date, String bam_end_date, int bam_user_id) {

        this.id = id;
        this.bam_description = bam_description;
        this.bam_title = bam_title;
        this.bam_price = bam_price;
        this.bam_state = bam_state;
        this.bam_latitude_position = bam_latitude_position;
        this.bam_longitude_position = bam_longitude_position;
        this.bam_creation_date = bam_creation_date;
        this.bam_end_date = bam_end_date;
        this.bam_user_id = bam_user_id;
    }

    public Bam(String bam_description, String bam_title, float bam_price,int bam_state,
               double bam_latitude_position, double bam_longitude_position,
               String bam_creation_date, String bam_end_date, int bam_user_id) {

        this.id = -1;
        this.bam_description = bam_description;
        this.bam_title = bam_title;
        this.bam_price = bam_price;
        this.bam_state = bam_state;
        this.bam_latitude_position = bam_latitude_position;
        this.bam_longitude_position = bam_longitude_position;
        this.bam_creation_date = bam_creation_date;
        this.bam_end_date = bam_end_date;
        this.bam_user_id = bam_user_id;
    }

    public Bam() {
        this.id = -1;
    }

    /**
     * obtenir le temps restant
     *
     * @return le temps restant
     */
    public int getTime()
    {
        if(bam_state == 2 || Utility.stringToDate(bam_end_date).getTime() <= Calendar.getInstance().getTimeInMillis())
            return -1;
        else
            return (int)((Utility.stringToDate(bam_end_date).getTime() - Calendar.getInstance().getTimeInMillis())/1000);
    }

    public int getBam_State() {
        return bam_state;
    }

    public void setBam_State(int bam_state) {
        this.bam_state = bam_state;
    }

    public int getId() {
        return id;
    }

    public String getBam_description() {
        return bam_description;
    }

    public String getBam_title() {
        return bam_title;
    }

    public float getBam_price() {
        return bam_price;
    }

    public double getBam_latitude_position() {
        return bam_latitude_position;
    }

    public double getBam_longitude_position() {
        return bam_longitude_position;
    }

    public String getBam_creation_date() {
        return bam_creation_date;
    }

    public String getBam_end_date() {
        return bam_end_date;
    }

    public int getBam_user_id() {
        return bam_user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bam bam = (Bam) o;

        if (getId() != bam.getId()) return false;
        if (Double.compare(bam.getBam_latitude_position(), getBam_latitude_position()) != 0)
            return false;
        if (Double.compare(bam.getBam_longitude_position(), getBam_longitude_position()) != 0)
            return false;
        return getBam_user_id() == bam.getBam_user_id();

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        temp = Double.doubleToLongBits(getBam_latitude_position());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getBam_longitude_position());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getBam_user_id();
        return result;
    }

    public void setId(int id) {
        this.id = id;
    }
}
