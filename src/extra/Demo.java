package extra;

public class Demo {

	public static void main(String[] args) {
//		StringSimilarity ss= new StringSimilarity();
		String s1 ="NEW YORK RED BULLS";
		String s2 ="New York RB";
		String s3 ="New York City";
		
		System.out.println(StringSimilarity.diceCoefficient(s1, s2));
		System.out.println(StringSimilarity.diceCoefficient(s1, s3));
		
		System.out.println(StringSimilarity.diceCoefficientOptimized(s1, s2));
		System.out.println(StringSimilarity.diceCoefficientOptimized(s1, s3));

		System.out.println(StringSimilarity.teamSimilarity(s1, s2));
		System.out.println(StringSimilarity.teamSimilarity(s1, s3));
		
//		System.out.println(StringSimilarity.diceCoefficient("Juventude marseglie", "juve"));
//		System.out.println(StringSimilarity.diceCoefficient("juventus marseglie", "juve lyonais"));
//		
//		System.out.println(StringSimilarity.diceCoefficientOptimized("Juventude", "juve"));
//		System.out.println(StringSimilarity.diceCoefficientOptimized("juventus", "juve"));
		
//		System.out.println(5/13);
//		float f= 5/13;
//		System.out.println(f);
//		System.out.println((float)5/13);
//		System.out.println((double)5/13);
//		System.out.println(5/15);
//		float hits = (float)(2 * 2 + 1) / (9 + 4);
//		float dist = (float) (5 / (9+4));
//		
//		System.out.println(hits);
//		System.out.println(dist);
		
//		System.out.println(StringSimilarity.teamSimilarity(s1, s2));
//		System.out.println(StringSimilarity.teamSimilarity(s1, s2));
	}

}

