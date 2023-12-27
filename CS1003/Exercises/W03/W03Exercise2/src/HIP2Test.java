import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class HIP2Test {
    public static void main(String [] argv) {

			try {
				// Setup input
				HIP2Reader h = new HIP2Reader(argv[0]);

				// Output Setup
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document document = db.newDocument();

				Node base = document.createElement("star-catalog");

				// Loop through input and output stars
				while (h.hasNextStar()) {
					HIP2Reader.HIP2Star s = h.getNextStar();

					// Calculate the distance from star
					double distance = 1000.00 / s.parallax;

					// Setup DOM nodes
					Element star = document.createElement("star");
					star.setAttribute("StarID", Integer.toString(s.StarID));
					star.setAttribute("right-ascension", Double.toString(s.RA));
					star.setAttribute("declination", Double.toString(s.Decl));
					star.setAttribute("distance", Double.toString(distance));
					star.setAttribute("magnitude", Double.toString(s.magnitude));
					base.appendChild(star);
					
				}
				document.appendChild(base);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();

				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(new File(argv[1]));
				transformer.transform(source, result);
				
			} catch (Exception e) {
				e.printStackTrace();
	    	}			
    	}
}
