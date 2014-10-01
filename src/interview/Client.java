package interview;

import java.io.*;
import java.net.*;

public class Client {
	URL url;
	String urlStr;
	HttpURLConnection conn;
	BufferedReader reader;
	String line;
	String result = "";

	public Client(String urlToRead) {
		urlStr = urlToRead;
	}

	public String get(String param) {

		try {
			url = new URL(urlStr + param);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", "TrackMaster-SDK-JAVA");
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(),"UTF-8"));
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String post(String param, String json) {

		try {
			url = new URL(urlStr + param);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", "TrackMaster-SDK-JAVA");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length",
					"" + Integer.toString(json.getBytes().length));
			conn.setUseCaches(false);
			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());
			writer.write(json);
			writer.flush();
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			writer.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String args[]) {

		// 构造json，格式为{"client_id":"aaa","client_secrect":"bbb","grant_type":
		// "ccc","email":"ddd","password":"eee"}
		String client_id = "\"9d222de385945b93422e\"";
		String client_secret = "\"0083ff3ec0fc565f5776d2c6e8bded635e662ab1\"";
		String grant_type = "\"password\"";
		String password = "\"padfief6\"";
		String email = "\"jinyangyang@xunlei.com\"";
		String json = "{\"client_id\":" + client_id + ",\"client_secret\":"
				+ client_secret + ",\"grant_type\":" + grant_type
				+ ",\"email\":" + email + ",\"password\":" + password + "}";

		Client openClient = new Client("http://open.admaster.com.cn");

		System.out.println(openClient.post("/oauth/access_token", json)); // 获取access_token

		Client apiClient = new Client("http://track.admasterapi.com");
		String access_token = "989b0fcdac06ec4dfe90b71c82168fceb32d90f1";
		System.out.println(apiClient.get("/user?access_token=" + access_token)); // 获取用户信息
		System.out.println(apiClient.get("/user/networks?access_token="
				+ access_token)); // 获取用户网络信息

	}
}
