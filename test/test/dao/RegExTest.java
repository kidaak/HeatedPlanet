package test.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExTest {

	public static void main(String[] args) {
		String str = "rs19: org.h2.result.LocalResult@cffd2a8 columns: 15 rows: 4 pos: 1";
		
		Pattern p = Pattern.compile("rows: (\\d+)");
		Matcher m = p.matcher(str);
		if(m.find()){
			System.out.println(m.group(1));
		}

	}

}
