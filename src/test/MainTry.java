package test;

import java.util.ArrayList;
import java.util.List;

public class MainTry {

	public static void main(String[] args) {
//		String a ="ale";
//				if("ale"==a){
//			System.out.println("AAAAAAAAA");
//		}else {System.out.println("BBBBBB");}

		List<Integer> list  = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		
		for(int i:list){
			System.out.println(i);
		}
		System.out.println();
		
		list.remove(2);
		list.add(2, 12);
		for(int i:list){
			System.out.println(i);
		}
		
	}

}
