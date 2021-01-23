package canvas_formatter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class WebAccess {

	private String prefix;
	private String suffix;
	private String token;

	WebAccess(String prefix, String suffix, String token) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.token = token + "&per_page=200"; // dont ask
	}

	/*
	 * @param api_reference give the part of the url between prefix and suffix
	 */

	public JSONArray getJSON(String api_reference) throws Exception {

		String response = getHTTP(prefix + api_reference + suffix + token);

		JSONTokener json_tokener = new JSONTokener(response);
		JSONArray json_array = (JSONArray) json_tokener.nextValue();

		return json_array;
	}

	public String getHTTP(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

}
