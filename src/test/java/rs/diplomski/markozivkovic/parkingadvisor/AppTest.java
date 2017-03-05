package rs.diplomski.markozivkovic.parkingadvisor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

/**
 * Unit testovi za aplikaciju.
 */
public class AppTest extends TestCase {

    protected final String ZONA_REZULTAT = "orange";

    protected double latitude;
    protected double longitude;
    protected double radius;

    /**
     * Postavljanje inicialnih podesavanja za testove.
     */
    protected void setUp() {
        latitude = 44.817084;
        longitude = 20.476769;
        radius = 3.5;
        konekcijaNaRedis();
    }

    /**
     * Metoda za konektovanje na Redis.
     */
    protected void konekcijaNaRedis() {
        Yaml yaml = new Yaml();
        try {
            @SuppressWarnings("rawtypes")
            FileInputStream inputStream = new FileInputStream(new File("/Users/markozivkovic/Desktop/ParkingAdvisor/config_real.yml"));
            Map config = (Map) yaml.load(inputStream);
            port((int) config.get("port"));
            staticFileLocation("/public");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Podesavanje testova koji ce se izvrsiti.
     * @return
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void runTest() {
        // pokratanje testa metode za upit baze
        testUpitBaze();
    }

    /**
     * Testiranje upita baze za parking zonu.
     */
    public void testUpitBaze() {
        String zona = RedisGeolocBaza.upitBaze(latitude, longitude, radius);
        assertEquals(ZONA_REZULTAT, zona);
    }
}

