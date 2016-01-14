package edu.uw.listdatademo;

import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //model
        //String[] data = downloadMovieData("Die%20Hard");//model in the list view, changed to the code below
        String[] data = new String[99];
        for(int i = 99; i>0; i--){
            data[99-1] = i +" bottles of beer on the wall";
        }


        MovieDownloadTask task = new MovieDownloadTask();
        task.execute("Die%20Hard");


        //View is in XML

        //Controller going to connect view and data
        //adapter is going to adapt model into views, adapater is an interface
        //using an array adapater because our model is an array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.list_item/*The view where you want to fill things in*/, R.id.txtItem/*the view item*/, data/*the data your filling it in with*/);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);//calls the adapter on the listView




    }
    public class MovieDownloadTask extends AsyncTask<String, Void,String[]>{
        protected String[] doInBackground(String...params){//ellipse can use any number of params of that type
            String movie = params[0];
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
                Log.v("ListActivity", results);
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

        @Override
        protected void onPostExecute(String[] movies) {
            adapter.clear();
            for(String movie: movies) {
                adapter.add(movie);
            }
        }
    }




}
