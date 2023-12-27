import java.util.*;
import java.io.*;

public class Loader {



    public String postcodes[];
    public Float latitudes[];
    public Float longitudes[];
    
    public Loader(String filename) throws IOException {
	
	ArrayList<String> al_postcodes;
	ArrayList<Float> al_latitudes;
	ArrayList<Float> al_longitudes;
	String s;
	BufferedReader br = new BufferedReader(new FileReader(filename));
	al_postcodes = new ArrayList<String>();
	al_latitudes = new ArrayList<Float>();
	al_longitudes = new ArrayList<Float>();
	int count = 0;
	br.readLine(); /* discard headers */
	while((s = br.readLine()) != null) {
	    String bits[] = s.split(",");
	    al_postcodes.add(bits[0]);
	    al_latitudes.add(Float.parseFloat(bits[1]));
	    al_longitudes.add(Float.parseFloat(bits[2]));
	}
	br.close();
	postcodes = al_postcodes.toArray( new String[1]);
	latitudes = al_latitudes.toArray(new Float [1]);
	longitudes = al_longitudes.toArray(new Float [1]);
    }
    
}
