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
		
		//���ݲ��ҵ��ʹ�����ҵ�ַ
        HttpGet getWordMean = new HttpGet("http://dict.youdao.com/w/" + word + "/#keyfrom=dict2.top");
        CloseableHttpResponse response = httpClient.execute(getWordMean);//ȡ�÷��ص���ҳԴ��

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        //ע��(?s)����˼����'.'ƥ�任�з���Ĭ������²�ƥ��
        Pattern searchMeanPattern = Pattern.compile("(?s)<div class=\"trans-container\">.*?<ul>.*?</div>");
        Matcher m1 = searchMeanPattern.matcher(result); //m1�ǻ�ȡ�������������<div>��

        if (m1.find()) 
        {
            String means = m1.group();//���н��ͣ�������ҳ��ǩ
            Pattern getChinese = Pattern.compile("(?m)<li>(.*?)</li>"); //(?m)������ƥ��
            Matcher m2 = getChinese.matcher(means);

            System.out.println("�е�����:");
            while (m2.find()) {
                //��Java��(.*?)�ǵ�1�飬������group(1)
                System.out.println("\t" + m2.group(1));
            }
        } 
        else 
        {
            System.out.println("δ���ҵ�����.");
            return;
        }
	}
	public static void getFromJinshan(String word) throws IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//ȡ�ðٶȷ���Ĳ��ҵ�ַ
		HttpGet getWordMean = new HttpGet("http://www.iciba.com/" + word);
		CloseableHttpResponse response = httpClient.execute(getWordMean);

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        
        Pattern searchMeanPattern = Pattern.compile("(?s)<ul class=\"base-list switch_part\" class=\"\">.*?</ul>");
        Matcher m1 = searchMeanPattern.matcher(result); //m1�ǻ�ȡ�������������<div>��
        
        if(m1.find())
        {
        	System.out.println("��ɽ����:");
        	String types = m1.group();//���н��ͣ�������ҳ��ǩ
            Pattern getType = Pattern.compile("(?s)<span class=\"prop\">(.*?)</span>(.*?)</p>"); //(?m)������ƥ��
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
            System.out.println("δ���ҵ�����.");
            return;
        }	
	}
	public static void getFromBing(String word) throws IOException
	{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//���ݲ��ҵ��ʹ�����ҵ�ַ
        HttpGet getWordMean = new HttpGet("http://cn.bing.com/dict/search?q=" + word + "&go=����&qs=bs&form=Z9LH5");
        CloseableHttpResponse response = httpClient.execute(getWordMean);//ȡ�÷��ص���ҳԴ��

        String result = EntityUtils.toString(response.getEntity());
        response.close();
        //ע��(?s)����˼����'.'ƥ�任�з���Ĭ������²�ƥ��
        Pattern searchMeanPattern = Pattern.compile("(?s)<meta name=\"description\"(.*?)/>");
        Matcher m1 = searchMeanPattern.matcher(result); //m1�ǻ�ȡ�������������<div>��

        if (m1.find()) 
        {
            String means = m1.group();//���н��ͣ�������ҳ��ǩ
            Pattern getChinese = Pattern.compile("(?s)]��(.*?)\""); //(?m)������ƥ��
            Matcher m2 = getChinese.matcher(means);
            
            System.out.println("��Ӧ����:");
            if(m2.find())
            {
            	means = m2.group(1) + "##";
            	getChinese = Pattern.compile("(?s)]��(.*?)##");
            	Matcher m3 = getChinese.matcher(means);
            	if(m3.find())
            		System.out.println("\t"+m3.group(1));
            	else
            		System.out.println("\t"+m2.group(1));
            }
        } 
        else 
        {
            System.out.println("δ���ҵ�����.");
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
