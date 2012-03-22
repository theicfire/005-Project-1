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

import sound.Pitch;

public class SchedulerTest {
	@Test
	public void testSchedule1Note() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		Scheduler s = new Scheduler(100,1);
		note.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSchedule12Note() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TunePrimitive note2 = TunePrimitive.Note(new Fraction(1,2), new Pitch('D'));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,2), new Pitch('E'));
		TuneSequence ts = new TuneSequence();
		ts.add(note);
		ts.add(note2);
		ts.add(note3);
		Scheduler s = new Scheduler(100, 2);
		ts.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testScheduleChord() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TunePrimitive note2 = TunePrimitive.Note(new Fraction(1,1), new Pitch('E'));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,1), new Pitch('G'));
		Chord c = new Chord();
		c.add(note);
		c.add(note2);
		c.add(note3);
		Scheduler s = new Scheduler(100, 1);
		c.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

}
