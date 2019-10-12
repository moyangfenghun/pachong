package pachong;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class test {

	@Test
	public void tesa(){
		System.out.println("test......");
		 // 按指定模式在字符串查找
	      String line = "This order &was placed for QT3000! OK?"+"S";
	      String pattern = "S";
	 
	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(pattern);
	 
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(line);
	      System.out.println(m.find());
	      System.out.println(this.getClass().getResource(""));
	      System.out.println(this.getClass().getResource("/"));
	      System.out.println(this.getClass().getClassLoader().getResource(""));
	      System.out.println(this.getClass().getResourceAsStream("log4j.properties"));
//		String str="中文字符";
//		try {
//			String encode = URLEncoder.encode(str,"UTF-8");//编码
//			System.out.println(encode);
//			System.out.println(URLDecoder.decode(encode, "UTF-8"));//解码
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
