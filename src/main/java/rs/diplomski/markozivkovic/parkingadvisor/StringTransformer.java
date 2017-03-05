package rs.diplomski.markozivkovic.parkingadvisor;

import spark.ResponseTransformer;
/**
 * @author Marko Zivkovic
 *
 */
public class StringTransformer implements ResponseTransformer {
	// implementacija jedinog metoda klase 
	@Override
	public String render(Object model) {
		return (String) model;
	}

}
