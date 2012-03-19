package player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SequencePlayer;

public class Scheduler implements SchedulableVisitor{
	private SequencePlayer PLAYER;
	private int clock;
	private final int TICKS_PER_QUARTER;
	
	
	public void visit(TuneSequence s) {
		for (Schedulable sch : s) {
			sch.accept(this);
		}
	}
	public void visit(TuneParallel p) {
		int beforeClock = clock;
		for (Schedulable s : p) {
			clock = beforeClock;
			s.accept(this);
		}
		
	}
	// IMPLAMENT WHEN KNOW HOW DO
	public void visit(TuneRepeatable r) {
		
	}
	// IMPLAMENT WHEN KNOW HOW DO
	public void visit(Tuple t) {
		int startClock = clock;
		Fraction frac = t.multiplier;
		for (Schedulable s : t) {
			s.accept(this);
			int diff = clock - startClock;
			diff = frac.times(diff).toInt();
			clock += diff;			
		}
	}
	
	public void visit(TunePrimitive p) {
		int start = clock;
		int change = p.duration.times(TICKS_PER_QUARTER).toInt();
		int finish = clock + change;
		if (p.isNote)
			PLAYER.addNote(p.midiNote, start, finish);
		clock = finish;
		
	}
	public Scheduler(int tempo, int ticksPerQuarterNote) {
		// DEAL WITH CATCHING THIS
		try {
			this.PLAYER = new SequencePlayer(tempo, ticksPerQuarterNote);
		} catch (MidiUnavailableException e) {
            e.printStackTrace();		
		} catch (InvalidMidiDataException e) {
            e.printStackTrace();		
		}
		this.clock = 0;
		this.TICKS_PER_QUARTER = ticksPerQuarterNote;
	}
}
