

package controllers;

import play.*;
import play.mvc.*;
import java.net.MalformedURLException;
import views.html.*;
import play.data.DynamicForm;
import play.data.Form ;
import java.util.Calendar;
import java.util.Date;
import static play.data.Form.form ;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result getUrl() {
    	return ok(getUrlFromUser.render());
    }
    
    public static Result getIssue(String repo, String owner) {
    	
		//by taking repo and owner create api readable
		String url="https://api.github.com/repos/"+owner+'/'+repo+"/issues";

    	long issueInOneDay =0 ;
    	long issueInOneWeek =0 ;
    	long issueInMoreThenWeek =0 ;
    	long TotalIssue =0 ;
    	
    	TotalIssue = issueInOneDay+issueInOneWeek+issueInMoreThenWeek;
    	
    	 
         String json = connectAndGetResponse(url, "GET");		//creating http connection by using HTTP client library
         
         Gson gson = new Gson();									//Gson library is used to prase the Json value
         
         Calendar oneDay = Calendar.getInstance();
         oneDay.setTime(new Date());
         oneDay.add(Calendar.DAY_OF_MONTH, -1);
         
         Date oneDayDate = oneDay.getTime();

         Calendar weekDay = Calendar.getInstance();
         weekDay.setTime(new Date());
         weekDay.add(Calendar.DAY_OF_MONTH, -7);
         Date weekDayDate = weekDay.getTime();
 		
         GitIssueWrapper []data = gson.fromJson(json, GitIssueWrapper[].class);			//a wrapper class is written which get the object by prasing the json
		 String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        DateTimeFormatter dtf = DateTimeFormat.forPattern(pattern);
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:MM:SS'Z'");
		
          if(data != null) {
            
                     for (GitIssueWrapper gitSummaryWrapper : data) {
							
                        if("open".equals(gitSummaryWrapper.getState())){
                        	 try {
								String stringDate = gitSummaryWrapper.getCreated();
								 Date date = dtf.parseDateTime(stringDate).toDate();
								 
								 
								 
								 if(date.after(oneDayDate)){
									 
									 issueInOneDay++;
								 }
								 else if(date.after(weekDayDate) ){
									
									 issueInOneWeek++;
								 }
								 else{
									 issueInMoreThenWeek++;
								 }
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }
                     }
                 
         }
         
         TotalIssue = issueInOneDay+issueInOneWeek+issueInMoreThenWeek;
         String dataVal = "issueInOneDay:"+issueInOneDay+":issueInOneWeek:"+issueInOneWeek+":moreThenOneWeek:"+issueInMoreThenWeek+":totalIssue:"+TotalIssue ;
         
    	return ok(dataVal);
    }

	public static String connectAndGetResponse(String urlStr,String method) 
    {
        URL url;
        HttpURLConnection connection = null;
        StringBuffer response = new StringBuffer();
        try {
            //Create connection
            url = new URL(urlStr);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;

            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();


        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }
}
