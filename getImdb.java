package getWeb_hw2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class hw2 {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String line = "", source = "";
		String regular_str = "";
		URL url = new URL("http://www.rarbt.com/");
		InputStream inStream = url.openStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream,"UTF-8"));
		while((line = bufferedReader.readLine())!=null){
			source += line;
		}
		/*--------------------------------第1部分--------------------------------*/
		Pattern pattern = Pattern.compile("<p class=['\"]tt cl['\"]>?.*?</p>");
		Matcher matcher = pattern.matcher(source);
		ArrayList<String> result_List = new ArrayList<String>();
		ArrayList<String> result_List2 = new ArrayList<String>();
		while(matcher.find()){
			result_List.add(matcher.group());
			//System.out.println(matcher.group()); 
        }
		int loc = 0;
		for(int i=0; i<result_List.size(); i++){
			String temp = result_List.get(i);
			temp = temp.replaceAll(""
				+ "<p class=['\"]tt cl['\"]><span><font color='red'>?.*?</font></span><a href=['\"]?.*?['\"] title=['\"]", "");
			temp = temp.replaceAll(""
				+ "['\"] target=['\"]_blank['\"]?.*?</p>", "");
			result_List2.add(temp);
			//result_List.set(i, temp);
			//System.out.printf(++loc + ":" + temp); 
		}
		/*--------------------------------第2部分--------------------------------*/
		ArrayList<String> her_List = new ArrayList<String>();
		ArrayList<String> imdb_List = new ArrayList<String>();
		for(int i=0; i<result_List.size(); i++){
			String herf_str = result_List.get(i);
			herf_str = herf_str.replaceAll(""
				+ "<p class=['\"]tt cl['\"]><span><font color='red'>?.*?</font></span><a href=['\"]", "");
			herf_str = herf_str.replaceAll(""
				+ "['\"] title=['\"]?.*?</p>", "\n");
			
			herf_str = "http://www.rarbt.com" + herf_str;
			her_List.add(herf_str);
			//System.out.printf(herf_str); 
		}
		loc = 0;
		for(int j=0; j<her_List.size(); j++){
			String urlTemp = her_List.get(j);
			URL sub_url = new URL(urlTemp);
			InputStream sub_inStream = sub_url.openStream();
			BufferedReader sub_bufferedReader = new BufferedReader(new InputStreamReader(sub_inStream,"UTF-8"));
			line = "";
			source = "";
			while((line = sub_bufferedReader.readLine())!=null){
				source += line;
			}
			Pattern sub_pattern = Pattern.compile("<a title=['\"]imdb['\"]?.*?</a>");
			Matcher sub_matcher = sub_pattern.matcher(source);
			while(sub_matcher.find()){
				imdb_List.add(sub_matcher.group());
				//System.out.println(++loc + ":" + sub_matcher.group()); 
	        }
		}
		for(int k=0; k<imdb_List.size(); k++){
			String temp = imdb_List.get(k);
			//System.out.println(temp); 
			temp = temp.replaceAll(""
				+ "<a title=['\"]?.*?imdb=", "");
			temp = temp.replaceAll(""
				+ "['\"]>?.*?</a>", "");
			System.out.println(++loc+":"+result_List2.get(k)+" imdb:"+temp);
		}
	}

}