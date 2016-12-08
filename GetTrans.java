package OnlineDic;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetTrans {
	public static void getFromYoudao(String word) throws IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//根据查找单词构造查找地址
        HttpGet getWordMean = new HttpGet("http://dict.youdao.com/w/" + word + "/#keyfrom=dict2.top");
        CloseableHttpResponse response = httpClient.execute(getWordMean);//取得返回的网页源码

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        //注意(?s)，意思是让'.'匹配换行符，默认情况下不匹配
        Pattern searchMeanPattern = Pattern.compile("(?s)<div class=\"trans-container\">.*?<ul>.*?</div>");
        Matcher m1 = searchMeanPattern.matcher(result); //m1是获取包含翻译的整个<div>的

        if (m1.find()) 
        {
            String means = m1.group();//所有解释，包含网页标签
            Pattern getChinese = Pattern.compile("(?m)<li>(.*?)</li>"); //(?m)代表按行匹配
            Matcher m2 = getChinese.matcher(means);

            System.out.println("有道释义:");
            while (m2.find()) {
                //在Java中(.*?)是第1组，所以用group(1)
                System.out.println("\t" + m2.group(1));
            }
        } 
        else 
        {
            System.out.println("未查找到释义.");
            return;
        }
	}
	public static void getFromJinshan(String word) throws IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//取得百度翻译的查找地址
		HttpGet getWordMean = new HttpGet("http://www.iciba.com/" + word);
		CloseableHttpResponse response = httpClient.execute(getWordMean);

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        
        Pattern searchMeanPattern = Pattern.compile("(?s)<ul class=\"base-list switch_part\" class=\"\">.*?</ul>");
        Matcher m1 = searchMeanPattern.matcher(result); //m1是获取包含翻译的整个<div>的
        
        if(m1.find())
        {
        	System.out.println("金山释义:");
        	String types = m1.group();//所有解释，包含网页标签
            Pattern getType = Pattern.compile("(?s)<span class=\"prop\">(.*?)</span>(.*?)</p>"); //(?m)代表按行匹配
            Matcher m2 = getType.matcher(types);
            
            while (m2.find()) 
            {
            	System.out.print("\t"+m2.group(1));
            	
            	String means = m2.group();
            	Pattern getChinese = Pattern.compile("<span>(.*?)</span>");
            	Matcher m3 = getChinese.matcher(means);
            	
            	while(m3.find())
            	{
            		System.out.print(m3.group(1));
            	}
            	System.out.println();
            }
        }
        else
        {
            System.out.println("未查找到释义.");
            return;
        }	
	}
	public static void getFromBing(String word) throws IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//根据查找单词构造查找地址
        HttpGet getWordMean = new HttpGet("http://cn.bing.com/dict/search?q=" + word + "&go=搜索&qs=bs&form=Z9LH5");
        CloseableHttpResponse response = httpClient.execute(getWordMean);//取得返回的网页源码

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        //注意(?s)，意思是让'.'匹配换行符，默认情况下不匹配
        Pattern searchMeanPattern = Pattern.compile("(?s)<meta name=\"description\"(.*?)/>");
        Matcher m1 = searchMeanPattern.matcher(result); //m1是获取包含翻译的整个<div>的

        if (m1.find()) 
        {
            String means = m1.group();//所有解释，包含网页标签
            Pattern getChinese = Pattern.compile("(?s)]，(.*?)\""); //(?m)代表按行匹配
            Matcher m2 = getChinese.matcher(means);
            
            System.out.println("必应释义:");
            if(m2.find())
            {
            	means = m2.group(1) + "##";
            	getChinese = Pattern.compile("(?s)]，(.*?)##");
            	Matcher m3 = getChinese.matcher(means);
            	if(m3.find())
            		System.out.println("\t"+m3.group(1));
            	else
            		System.out.println("\t"+m2.group(1));
            }
        } 
        else 
        {
            System.out.println("未查找到释义.");
            return;
        }
	}
	public static void get(String word) throws IOException
	{
		getFromYoudao(word);
		getFromJinshan(word);
		word = word.replaceAll("%20","+");
		getFromBing(word);
	}
}
