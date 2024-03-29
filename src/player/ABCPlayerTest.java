package player;

import org.junit.Test;

public class ABCPlayerTest {
	private void printAndPlay(String file) {
		Lexer l = new Lexer(file);
		ABCEnvironment e = ABCParser.parse(l.tokens);
		System.out.println(e);
		ABCPlayer.playEnvironment(e);
	}
	
//	@Test
//	public void playTest1() {
//		ABCPlayer.play("sample_abc/sample1.abc");
//	}
//	
//	@Test
//	public void playTest2() {			
//		ABCPlayer.play("sample_abc/sample2.abc");
//	}
//	
//	@Test
//	public void playTest3() {
//		ABCPlayer.play("sample_abc/sample3.abc");
//	}
//	
//	@Test
//	public void barbieGirl() {
//		ABCPlayer.play("sample_abc/barbieGirl.abc");
//	}
//	
//	@Test
//	public void yesterDay() {
//		printAndPlay("sample_abc/yesterday.abc");
//	}
//	
//	@Test
//	public void playTestHard() {
//		printAndPlay("sample_abc/invention.abc");
//	}
	
	@Test
	public void playFur() {
		printAndPlay("sample_abc/fur_elise.abc");
	}
	@Test
     public void testBadLineLengths() {
         try{
             ABCPlayer.play("broken.abc");
             assert false;
         } catch (ABCParserException e) {
             assert true;
         }
   }
	   

}
