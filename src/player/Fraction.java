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
		
		return new Fraction(new_num,new_dom);
	}
	
	public Fraction times(Fraction f) {
		
		int new_num = num * f.num;
		int new_dom = dom * f.dom;
		
		return new Fraction(new_num,new_dom);
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
	
	public boolean equals(Fraction f) {
		return (f.num*num == f.dom*dom);
	}
	
	public boolean equals(int d) {
		return this.equals(new Fraction(d,1));
	}

	@Override
	public String toString() {
		return "Fraction [num=" + num + ", dom=" + dom + "]";
	}
	
	public String toShortString() {
		return String.format("%d/%d",num,dom);
	}
	
	
	
}
