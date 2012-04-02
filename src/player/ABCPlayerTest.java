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


import lexer.*;


import org.junit.Test;

public class ABCPlayerTest {
	
	
	private void printAndPlay(String file) {
		Lexer l = new Lexer(file);
		ABCEnvironment e = ABCParser.parse(l.tokens);
		System.out.println(e);
		ABCPlayer.playEnvironment(e);
	}
	
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
		//ABCPlayer.play("sample_abc/sample3.abc");
	}
	
	@Test
	public void barbieGirl() {
		printAndPlay("sample_abc/little_night_music.abc");
	}
	
	@Test
	public void playTestHard() {
		ABCPlayer.play("sample_abc/fur_elise.abc");
	}
}
