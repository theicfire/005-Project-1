package player;

import static org.junit.Assert.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

public class ABCPlayerTest {
	@Test
	public void playTest1() {
		//ABCPlayer.play("sample_abc/sample1.abc");
	}
	
	@Test
	public void playTest2() {			
		//ABCPlayer.play("sample_abc/sample2.abc");
	}
	
	@Test
	public void playTest3() {
		ABCPlayer.play("sample_abc/sample3.abc");
	}
	
	@Test
	public void playInvention() {
		lexer.Lexer l = new lexer.Lexer("sample_abc/invention.abc");
		ABCEnvironment e = ABCParser.parse(l.tokens);
		System.out.println(e.toString());
		ABCPlayer.playEnvironment(e);
	}
	
	
	@Test
	public void playTestHard() {
		
		lexer.Lexer l = new lexer.Lexer("sample_abc/fur_elise.abc");
		ABCEnvironment e = ABCParser.parse(l.tokens);
		System.out.println(e.toString());
		
		ABCPlayer.play("sample_abc/fur_elise.abc");
	}
}
