import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Задание 2
//
//Используя JSON подключиться к НБУ и узнать текущий курс валют (RUB, UAN, USD, EURO).
public class CurrencyRates {
    private static final String RUB = "RUB";
    private static final String USD = "USD";
    private static final String EUR = "EUR";

    public static void main(String[] args) {
        try {
            URL url = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String currency = item.getString("cc");
                    if (currency.equals(RUB) || currency.equals(USD) || currency.equals(EUR)) {
                        double rate = item.getDouble("rate");
                        System.out.println("текущий курс -  " + currency + ": " + rate);
                    }
                }
            } else {
                System.out.println("Помилка підключення до НБУ. Код відповіді: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

