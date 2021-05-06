package lab_1;

public class Main {
	
	@SuppressWarnings("unchecked") /* unchecked cast from Object to T type*/
	public static void main(String args[])  {  
		
		TimeHistory<Double[]> th = new TimeHistory();
		System.out.println(th);
		
		Integer[] intTab = {1, 4, 5};
		TimeHistory<Integer[]> th2 = new TimeHistory("First device",
													"Not yet devised",
													12032012,
													1,
													"Kopiejki",
													0.3,
													intTab,
													0.5
													);
		System.out.println(th2);
		
		Float[] floTab = {1.42f, 1.5f, 1.8f};
		TimeHistory<Float[]> th3 = new TimeHistory("Second device",
												    "Not yet devised",
													12032012,
													2,
													"Weber",
													0.6,
													floTab,
													0.5
													);
		System.out.println(th3);
		
		Spectrum<Double[]> sp = new Spectrum();
		System.out.println(sp);
		
		Spectrum<Float[]> sp2 = new Spectrum("Third device",
											"Powerful device",
											12912020,
											3,
											"Amperozwój/Weber",
											0.4,
											floTab,
											"logarythmic");
		System.out.println(sp2);
		
		Alarm<Float> al = new Alarm();
		System.out.println(al);
		
		Alarm<Integer> al2 = new Alarm("Fourth device",
										"Not so powerful device",
										13042021,
										2,
										3,
										0
										);
		System.out.println(al2);

		
	}  
}
