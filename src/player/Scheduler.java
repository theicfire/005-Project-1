package player;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import sound.SequencePlayer;


/*  
 * Scheduler implements SchedulableVisitor. Recursively walks down schedulable tree
 * 	and schedules notes to the scheduler's sequenceplayer. 
 */

public class Scheduler implements SchedulableVisitor{
	public SequencePlayer PLAYER;
	private int clock;
	private int TICKS_PER_QUARTER;
	
	
	public void visit(TuneSequence s) {
		for (Schedulable sch : s) {
			sch.accept(this);
		}
	}
	public void visit(TuneParallel p) {
		int beforeClock = clock;
		
		//keep track of the latest clock
		//for example, a chord with a half note and
		//a quarter note will move the clock two beats foward
		//regardless of the order in which they are played
		int latestNewClock = clock;
		
		for (Schedulable s : p) {
			clock = beforeClock;
			s.accept(this);
			latestNewClock = (clock > latestNewClock) ? clock : latestNewClock;
		}
		
		clock = latestNewClock;
		
	}
	public void visit(TuneRepeatable r) {
		if (!r.hasMultipleEndings()) {
			r.body().accept(this);
			r.body().accept(this);
		}
		else {
			for (Schedulable s : r.endings()) {
				r.body().accept(this);
				s.accept(this);				
			}
		}		
	}
	/*
	 * Assumes t.multiplier * TICKS_PER_QUARTER can be resolved to an int
	 */
	public void visit(Tuple t) {
		int oldTicks = TICKS_PER_QUARTER;
		Fraction frac = t.multiplier;
		TICKS_PER_QUARTER = frac.times(oldTicks).toInt();
		for (Schedulable s : t) {
			s.accept(this);			
		}
		TICKS_PER_QUARTER = oldTicks;
	}
	/* 
	 * Assumes that ticks_per_quarter * p.duration is an int
	 */
	public void visit(TunePrimitive p) {
		int start = clock;
		int change = p.duration.times(TICKS_PER_QUARTER).toInt();
		int finish = clock + change;
		if (p.isNote)
			PLAYER.addNote(p.midiNote, start, change);
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
