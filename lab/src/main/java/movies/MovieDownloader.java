package movies;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A class for downloading movie data from the internet.
 * Code adapted from Google.
 *
 * YOUR TASK: Add comments explaining how this code works!
 * 
 * @author Joel Ross & Kyungmin Lee
 */
public class MovieDownloader {

	public static String[] downloadMovieData(String movie) {

		//construct the url for the omdbapi API
		String urlString = "http://www.omdbapi.com/?s=" + movie + "&type=movie";
		
		HttpURLConnection urlConnection = null;//creates a new urlCOnnection
		BufferedReader reader = null;//creates a null bufferreader

		String movies[] = null;//creates a null string array of movies

		try {

			URL url = new URL(urlString);//new URL object, for the database for the movie

			urlConnection = (HttpURLConnection) url.openConnection();//casts to httpURLConnection
			urlConnection.setRequestMethod("GET");//Sets the request to "get" the URL
			urlConnection.connect();//connects to the database

			InputStream inputStream = urlConnection.getInputStream();//reads the value given from the database
            StringBuffer buffer = new StringBuffer();//creates new Stringbuffer
			if (inputStream == null) {//if user does not input anything will return null
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));//new bufferreader

			String line;
            //Takes in the input from the database(until at the end/null), maing a new line for each that is imported
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
            //checks to make sure the imported buffer is not null/nothing
			if (buffer.length() == 0) {
				return null;
			}
            
			String results = buffer.toString();//changes the buffer to a string
            //replaces results with the given seperations
			results = results.replace("{\"Search\":[","");
			results = results.replace("]}","");
			results = results.replace("},", "},\n");

			movies = results.split("\n");//splits the different movie results by a new line
		}
        //"occurs when an exception is found"
		catch (IOException e) {
			return null;
		} 
		finally {
            //If the URL is found(not null) and is completed gathering results disconnects
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
            //Similarily, when done finding results closes the reader
			if (reader != null) {
				try {
					reader.close();
				}
                //if there is an exception this is executed
				catch (final IOException e) {
				}
			}
		}
        
		return movies;//returns the array of movies
	}


	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter a movie name to search for: ");//For the user in the command line
		String searchTerm = sc.nextLine().trim();//Takes in the input from the user
		String[] movies = downloadMovieData(searchTerm);//Calls the method to search the database
		for(String movie : movies) {//Takes the found array of movies and prints them out
			System.out.println(movie);
		}
		
		sc.close();//closes the scanner
	}
}
