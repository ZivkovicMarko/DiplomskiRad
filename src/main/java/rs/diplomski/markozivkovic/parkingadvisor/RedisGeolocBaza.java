/**
 * 
 */
package rs.diplomski.markozivkovic.parkingadvisor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Rad sa Redis bazom
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;

/**
 * @author Marko Zivkovic
 *
 */
public class RedisGeolocBaza {
	static Logger log = LoggerFactory.getLogger(RedisGeolocBaza.class);

	public static String upitBaze(double lat, double lon, double radius) {
		log.info("Ulazim u upitBaze!");
		// Otvaramo konekciju prema lokalnoj instanci Redis baze na
		// podrazumevanom portu
		Jedis jedis = new Jedis("localhost");
		log.info("Zavrsio sam uspostavljanje konekcije sa Redis");
		String odgovor = "";
		// Nadji sve tacke geografskih koordinata unete u bazu "Beograd" koje se
		// nalaze unutar odredjenog radiusa sa centrom u trenutnoj geolokaciji
		List<GeoRadiusResponse> members = jedis.georadius("Beograd", lat, lon, radius, GeoUnit.M);
		log.info("Zavrsio sam upit u bazu Redis");
		// Formiraj skup zona bez ponavljanja na osnovu zona svih tacaka koje su
		// rezultat pozivanja prethodnog upita u bazu
		Set<String> bliskeZone = new HashSet<String>();
		for (GeoRadiusResponse geoRadiusResponse : members) {
			String[] lokacija = geoRadiusResponse.getMemberByString().split("\\-");
			String[] zona = lokacija[0].split("\\.");
			log.info(">>zona>>" + zona[0]);
			// log.info(">>>>"+zona[0].substring(1, zona[0].length()));
			bliskeZone.add(zona[0]);
		}
		// log.info("Zavrsio sam punjenje skupa bliskeZone");
		// Pretvori u niz stringova radi lakse obrade
		String[] sveBliskeZone = (String[]) bliskeZone.toArray(new String[bliskeZone.size()]);
		log.info("Broj pronadjenih tacaka u radijusu: "+sveBliskeZone.length);

		if (sveBliskeZone.length == 0) {
			// Po podatcima iz baze, ne nalazite se u
			// blizini neke zone
			odgovor = "orange";
		} else if (sveBliskeZone.length == 1) {
			// Geolokacija i podatci iz baze pokazuju da ste u
			odgovor = jedis.get(String.valueOf(sveBliskeZone[0]));
		} else {
			// Po podatcima iz baze, nalazite se u blizini vise razlicitih zona
			odgovor = "white";
		}

		jedis.close();
		// log.info("Zavrsio sam upitBaze!");
		log.info(">>boja>>" + odgovor);
		return odgovor;
	}

}
