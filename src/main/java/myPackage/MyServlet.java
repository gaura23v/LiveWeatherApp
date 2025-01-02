package myPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//API kay Setup
		String apiKey = "Enter the API Key here";
		
		//Request City name
		String city = request.getParameter("city");
		
		//API URL 
		String apiURL = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
		
		//APi Integration
		URL url = new URL(apiURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//read the data from neetwork
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		
		//store in stringBuilder because String in immuatable
		StringBuffer responseContent = new StringBuffer();
				
	    //to take input from the reader;
		Scanner sc = new Scanner(reader);
		
		while(sc.hasNext()) {
			responseContent.append(sc.nextLine());
		}
		
		sc.close();
		
		//Typecasting the data or parsing the data into json form stringBuffer;
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		
		//Date & Time 	
		
		long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//        String formattedTime = timeFormat.format(date).toString();
		
		//Temperature 
		double tempKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int tempCelsius = (int) (tempKelvin-273.15);
		
		
		//Humidity
		int Humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//wind speed 
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//weather Condition
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//request.setAttribute("Time",formattedTime);		
		request.setAttribute("date",date);
		request.setAttribute("city",city);
		request.setAttribute("temp",tempCelsius);
		request.setAttribute("humidity",Humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherCondition",weatherCondition);
		request.setAttribute("weatherData",responseContent.toString());
		
		connection.disconnect();
		
		request.getRequestDispatcher("index.jsp").forward(request, response);
				
	}

}
