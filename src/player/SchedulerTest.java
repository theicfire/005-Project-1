package player;
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
	public void testSchedule3Note() {
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
	public void testRest() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TunePrimitive rest = TunePrimitive.Rest(new Fraction(1,2));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,2), new Pitch('E'));
		TuneSequence ts = new TuneSequence();
		ts.add(note);
		ts.add(rest);
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
	@Test
	public void testScheduleParallel() {
		TunePrimitive note1 = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TunePrimitive note2 = TunePrimitive.Note(new Fraction(1,1), new Pitch('D'));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,1), new Pitch('E'));
		TunePrimitive note4 = TunePrimitive.Note(new Fraction(1,1), new Pitch('F'));
		TunePrimitive note5 = TunePrimitive.Note(new Fraction(1,1), new Pitch('G'));
		TunePrimitive note6 = TunePrimitive.Note(new Fraction(1,1), new Pitch('A'));
		TunePrimitive note7 = TunePrimitive.Note(new Fraction(1,1), new Pitch('C').octaveTranspose(1));
		TuneSequence ts1 = new TuneSequence();
		TuneSequence ts2 = new TuneSequence();
		TuneSequence ts3 = new TuneSequence();
		ts1.add(note1);
		ts1.add(note2);
		ts1.add(note3);
		ts2.add(note3);
		ts2.add(note4);
		ts2.add(note5);
		ts3.add(note5);
		ts3.add(note6);
		ts3.add(note7);
		TuneParallel p = new TuneParallel();
		p.add(ts1);
		p.add(ts2);
		p.add(ts3);
		Scheduler s = new Scheduler(100, 1);
		p.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testScheduleTuple() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,2), new Pitch('C'));
		TunePrimitive note2 = TunePrimitive.Note(new Fraction(1,2), new Pitch('E'));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,2), new Pitch('G'));
		TunePrimitive note4 = TunePrimitive.Note(new Fraction(1,2), new Pitch('D'));
		Tuple t0 = new Tuple(2);
		Tuple t = new Tuple(3);
		Tuple t2 = new Tuple(4);
		TuneSequence ts = new TuneSequence();
		t0.add(note);
		t0.add(note3);
		t.add(note);
		t.add(note2);
		t.add(note3);
		t2.add(note);
		t2.add(note4);
		t2.add(note2);
		t2.add(note3);
		Scheduler s = new Scheduler(100, 24);
		ts.add(note);
		ts.add(note);
		ts.add(t0);
		ts.add(t);
		ts.add(t2);
		ts.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}		
	}
	@Test
	public void testRepeat() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TunePrimitive note2 = TunePrimitive.Note(new Fraction(1,2), new Pitch('D'));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,2), new Pitch('E'));
		TuneSequence t = new TuneSequence();
		t.add(note);
		t.add(note2);
		t.add(note3);
		TuneRepeatable r = TuneRepeatable.fromSchedulable(t);
		Scheduler s = new Scheduler(100, 4);
		r.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}		
	}
	@Test
	public void testMultibleEndings() {
		TunePrimitive note = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TunePrimitive note2 = TunePrimitive.Note(new Fraction(1,2), new Pitch('D'));
		TunePrimitive note3 = TunePrimitive.Note(new Fraction(1,2), new Pitch('E'));
		TunePrimitive noteE1 = TunePrimitive.Note(new Fraction(1,1), new Pitch('F'));
		TunePrimitive noteE2 = TunePrimitive.Note(new Fraction(1,1), new Pitch('C'));
		TuneSequence t = new TuneSequence();
		t.add(note);
		t.add(note2);
		t.add(note3);
		TuneRepeatable r = TuneRepeatable.fromSchedulable(t);
		r.add(noteE1);
		r.add(noteE2);
		Scheduler s = new Scheduler(100, 4);
		r.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}		
	}
	

}
