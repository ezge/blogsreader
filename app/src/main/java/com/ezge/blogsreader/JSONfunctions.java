package com.ezge.blogsreader;

/**
 * Created by ezge on 2/21/2018.
 */

// JSONFunctions class downloads the JSON file

import android.content.Context;
import android.content.res.Resources;
import android.os.StrictMode;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class JSONfunctions {

    Context context;
    String message = null;
    Resources res;

    public JSONfunctions(Context context){
        this.context = context;
    }

    private static JSONObject readJSONResponse(InputStream is, String encoding){
        String line = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        JSONObject json = null;

        try {
            reader = new BufferedReader( new InputStreamReader( is, encoding ), 8 );
            while ((line = reader.readLine()) != null){
                sb.append( line );
            }
            json = new JSONObject( sb.toString() );

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    private String writeJSONCredentials(Credentials credential)
    {
        String result = null;

        try{
            JSONObject credentials = new JSONObject(  );
            credentials.accumulate(  "email", credential.getEmail());
            credentials.accumulate( "password", credential.getPassword() );

            result = credentials.toString();

        }catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    private void sendErrorMessage(){
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
    }

    public int postJSONCredentials(String url, Credentials credInfo){

        HttpURLConnection connection = null;
        String response = null;
        String encoding = null;
        String credentials = null;
        JSONObject json = null;
        int responseCode = 0;

        // POST Request
        try {
            res = context.getResources();

            URL myURL = new URL(url);
            connection = (HttpURLConnection)myURL.openConnection();

            connection.setReadTimeout(30000);
            connection.setConnectTimeout(30000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host",res.getString( R.string.host));
            connection.setRequestProperty("Accept", res.getString( R.string.requestPropAccept));
            connection.setRequestProperty("Content-Type", res.getString( R.string.requestPropContentType));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // write the request body
            credentials = writeJSONCredentials(credInfo);
            OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream() );
            out.write( credentials);
            out.flush();
            out.close();

            responseCode = connection.getResponseCode();

            // read the response
            if (responseCode == connection.HTTP_OK) {
                encoding = connection.getContentEncoding() == null ? "UTF-8" : connection.getContentEncoding();
                InputStream is = connection.getInputStream();

                json = readJSONResponse( is, encoding );
                response = json.getString( "token" );

                if (!response.equals( res.getString( R.string.token ) )) {
                    responseCode = 401;
                }
                is.close();
            }
            setMessage( responseCode );

        }
         catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
         catch (JSONException e){
            e.printStackTrace();
         }
         finally {
            connection.disconnect();
        }

        sendErrorMessage(  );
        return responseCode;
    }

    private void setMessage(int response){
        res = context.getResources();

        switch (response){
            case 200:
                message = res.getString( R.string.code_200 );
                break;
            case 400:
                message = res.getString( R.string.code_400 );
                break;
            case 401:
                message = res.getString( R.string.code_401 );
                break;
            case 404:
                message = res.getString( R.string.code_404 );
                break;
            case 406:
                message = res.getString( R.string.code_406 );
                break;
            case 415:
                message = res.getString( R.string.code_415 );
                break;
            case 503:
                message = res.getString( R.string.code_503 );
                break;
            default:
                message = res.getString( R.string.errorUnknown ) + response;
                break;
        }
    }

    public String getResponseFromURL(String url) {
        InputStream is = null;
        String encoding = null;
        String line = null;
        String result = null;
        URLConnection uc = null;
        BufferedReader reader = null;
        StringBuilder sb = null;

        res = context.getResources();
        // Download JSON data from URL

        try {
            URL myUrl = new URL( url );
            uc = myUrl.openConnection();

            uc.setRequestProperty( "Accept", res.getString( R.string.requestPropAccept ) );
            uc.setRequestProperty( "X-Authorize", res.getString( R.string.token ) );
            uc.setReadTimeout( 30000 );
            uc.setConnectTimeout( 30000 );
            uc.setDoInput( true );

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy( policy );

            uc.connect();

            encoding = uc.getContentEncoding() == null ? "UTF-8" : uc.getContentEncoding();

            is = uc.getInputStream();

            sb = new StringBuilder();

            reader = new BufferedReader( new InputStreamReader( is, encoding ), 8 );
            while ((line = reader.readLine()) != null) {
                sb.append( line );
            }
            result = sb.toString();

            is.close();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
