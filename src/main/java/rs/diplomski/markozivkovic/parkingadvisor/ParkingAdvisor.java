package rs.diplomski.markozivkovic.parkingadvisor;

// Spark Microframework (Osnova cele aplikacije)
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

// Rad sa fajlom 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

// Logger
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Rad sa YAML formatom u kome je izradjen konfiguracioni fajl
import org.yaml.snakeyaml.Yaml;

/**
 * @author Marko Zivkovic
 *
 */
public class ParkingAdvisor {

	public static void main(String[] args) throws FileNotFoundException {

		// Obezbedjujemo instancu logger-a za svaku klasu posebno
		Logger log = LoggerFactory.getLogger(ParkingAdvisor.class);

		// Preuzmi konfiguracione parametre iz konfiguracionog fajla koji je bio
		// parametar kod pokretanja aplikacije
		Yaml yaml = new Yaml();
		@SuppressWarnings("rawtypes")
		Map config = (Map) yaml.load(new FileInputStream(new File(args[0])));

		// Konfigurisanje aplikacije
		// HTTP port na kome je aplikacija dostupna (citamo iz konfiguracionog
		// fajla)
		port((int) config.get("port"));
		// Lokacija na kojoj su smesteni staticki sadrzaji (HTML, CSS, JavaScript, slike,...)
		staticFileLocation("/public");
		log.info("Aplikacija je pocela sa radom na port-u: " + config.get("port"));

		// RESTful endpoint koji vraca zonu parkiranja kodiranu bojom 
		get("/boja/*/*/*", "text/plain", (request, response) ->

		RedisGeolocBaza.upitBaze(Double.valueOf(request.splat()[0]), Double.valueOf(request.splat()[1]),
				Double.valueOf(request.splat()[2])),

				new StringTransformer());

	}
}
