import java.io.*;

public class HIP2Reader {
    public class HIP2Star {
	public int StarID;
	public double RA;
	public double Decl;
	public double parallax;
	public double magnitude;
    };
    
    private BufferedReader br;

    // constructor, takes a filename
    public HIP2Reader(String filename) throws FileNotFoundException {
	br = new BufferedReader( new FileReader( filename));
    }

    private HIP2Star nextStar = null;

    private void ParseNextStar() throws IOException {
	String line = br.readLine();
	if (line != null) {
	    nextStar = new HIP2Star();
	    nextStar.StarID = Integer.parseInt(line.substring(0,6).trim());
	    nextStar.RA = Double.parseDouble(line.substring(15,28).trim());
	    nextStar.Decl = Double.parseDouble(line.substring(29,42).trim());
	    nextStar.parallax = Double.parseDouble(line.substring(43,50).trim());
	    nextStar.magnitude = Double.parseDouble(line.substring(129,136).trim());
	}
    }

    // true if there is another star
    public boolean hasNextStar() throws IOException {
	if (nextStar == null)
	    ParseNextStar();
	return (nextStar != null);
    }
    
    // the next star
    public HIP2Star getNextStar() {
	HIP2Star s = nextStar;
	nextStar = null;			
	return s;
    }
	
}
