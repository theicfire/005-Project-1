package player;

/*
 * Fraction class for handling durations (to avoid any floating point nonsense)
 * we ?need? to add tests
 * toInt method will throw an error if it doesn't represent an integer...
 * this will be helpfil for debugging timing issues
 */
public class Fraction {

	public int num;
	public int dom;
	
	public Fraction(int n,int d) {
		num = n;
		dom = d;
	}

	public Fraction add(Fraction f) {
		
		int left_num = num * f.dom;
		int right_num = f.num * dom;
		int new_num = left_num + right_num;
		
		int new_dom = dom * f.dom;
		
		return new Fraction(new_num,new_dom).reduce();
	}
	
	public Fraction times(Fraction f) {
		
		int new_num = num * f.num;
		int new_dom = dom * f.dom;
		
		return new Fraction(new_num,new_dom).reduce();
	}
	
	public Fraction times(int i) {
		return this.times(new Fraction(i,1));
	}
	
	public Fraction inverse() {
		return new Fraction(dom,num);
	}
	
	public int toIntLax() {
		return num/dom;
	}
	
	//default is strict... will give us a problem if we cannot go to int
	public int toInt() {
		if (num%dom != 0) {
			throw new RuntimeException(String.format("Fraction cannot become an int! (%d/%d)",num,dom));
		} else {
			return num/dom;
		}
	}
	
	public Fraction reduce() {
		//returns a fraction reduced to its lowest form
		//find the gcd of a and b using pseudo code on wikipedia
		//http://en.wikipedia.org/wiki/Euclidean_algorithm#Greatest_common_divisor
		
		if (num == dom) {
			return new Fraction(1,1);
		}
		

		int a = (num < dom) ? num : dom;
		int b = (num < dom) ? dom : num;
		while (b!=0) {
			int temp = b;
			b = a%b;
			a = temp;
		}
		int gcd = a;
		
		int newNum = num/gcd;
		int newDom = dom/gcd;
		
		return new Fraction(newNum,newDom);
		
		
		
	}
	
	public static Fraction reduce(Fraction f) {
		return f.reduce();
	}
	


	@Override
	public String toString() {
		return "Fraction [num=" + num + ", dom=" + dom + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dom;
		result = prime * result + num;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		if (obj instanceof Integer) {
			return this.equals(new Fraction((Integer) obj,1));
		}
		
		if (getClass() != obj.getClass())
			return false;
		
		Fraction other = (Fraction) obj;
		
		//need to use cross multiply unless we can guarantee
		//that all fractions will be in lowest form,
		//which we can't yet.
		
		int left = num*other.dom;
		int right = dom*other.num;
		
		return left==right;

	}
	
	
	public String toShortString() {
		return String.format("%d/%d",num,dom);
	}
	
	
	
}
