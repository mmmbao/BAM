package bam.bam.bam.dataWS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * put et post sur le Web Service
 *
 * @author Marc
 */
public class PostPutData {

    /**
     * URL
     */
    private String url;

    /**
     * methode (PUT ou POST)
     */
    private String methode;

    /**
     * liste des noms des champs
     */
    private List<String> urlNom;

    /**
     * liste des éléments
     */
    private List<String> urlData;

    /**
     * la réponse
     */
    private StringBuffer response;

    /**
     * le code de retour
     */
    private int code = -1;

    public PostPutData(String url, String methode, List<String> urlNom, List<String> urlData) {
        this.url = url;
        this.methode = methode;
        this.urlNom = urlNom;
        this.urlData = urlData;
    }

    public Boolean lancerEnregistrement() {

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(methode);

            // For POST only - START
            con.setDoOutput(true);

            String urlParameters = "";

            if(!urlNom.isEmpty())
                urlParameters = urlNom.get(0) + "=" + URLEncoder.encode(urlData.get(0), "UTF-8");

            for(int i = 1; i < urlNom.size(); i++)
                urlParameters += "&" + urlNom.get(i) + "=" + URLEncoder.encode(urlData.get(i), "UTF-8");

            OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());

            os.write(urlParameters);
            os.flush();
            os.close();
            // For POST only - END

            code = con.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) { //success

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return true;
            }

            // si trop de bam envoyé
            if(code == 429 || code == 403)
                return true;

            return false;

        } catch (Exception e)
        {
            return false;
        }
    }

    public int getResponseCode()
    {
        return code;
    }

    public String getResponse()
    {
        return response.toString();
    }
}
