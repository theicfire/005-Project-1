package sound;

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

public class SequencePlayerTest {
	@Test
	public void testPlaySample1() {
		SequencePlayer player;
        try {

            // create a new player, with 120 beats (i.e. quarter note) per
            // minute, with 2 tick per quarter note
            player = new SequencePlayer(140, 12);

            player.addNote(new Pitch('C').toMidiNote(), 0, 4*3);
            player.addNote(new Pitch('C').toMidiNote(), 4*3, 8*3);
            player.addNote(new Pitch('C').toMidiNote(), 8*3, 11*3);
            player.addNote(new Pitch('D').toMidiNote(), 11*3, 12*3);
            player.addNote(new Pitch('E').toMidiNote(), 12*3, 16*3);
            player.addNote(new Pitch('E').toMidiNote(), 16*3, 19*3);
            player.addNote(new Pitch('D').toMidiNote(), 19*3, 20*3);
            player.addNote(new Pitch('E').toMidiNote(), 20*3, 23*3);
            player.addNote(new Pitch('F').toMidiNote(), 23*3, 24*3);
            player.addNote(new Pitch('G').toMidiNote(), 24*3, 32*3);
            
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 32*3, 32*4 + 4);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 32*3 + 4, 32*3 + 8);
            player.addNote(new Pitch('C').transpose(Pitch.OCTAVE).toMidiNote(), 32*3 + 8, 32*3 + 12);
            
            player.addNote(new Pitch('G').toMidiNote(), 10, 1);
            player.addNote(new Pitch('G').toMidiNote(), 10, 1);
            player.addNote(new Pitch('G').toMidiNote(), 10, 1);
            
            player.addNote(new Pitch('E').toMidiNote(), 10, 1);
            player.addNote(new Pitch('E').toMidiNote(), 10, 1);
            player.addNote(new Pitch('E').toMidiNote(), 10, 1);
            
            player.addNote(new Pitch('C').toMidiNote(), 10, 1);
            player.addNote(new Pitch('C').toMidiNote(), 10, 1);
            player.addNote(new Pitch('C').toMidiNote(), 10, 1);
            
            player.addNote(new Pitch('F').toMidiNote(), 11, 1);
            player.addNote(new Pitch('E').toMidiNote(), 12, 1);
            player.addNote(new Pitch('D').toMidiNote(), 13, 1);
            player.addNote(new Pitch('C').toMidiNote(), 14, 1);

            System.out.println(player);

            // play!
            player.play();

            /*
             * Note: A possible weird behavior of the Java sequencer: Even if the
             * sequencer has finished playing all of the scheduled notes and is
             * manually closed, the program may not terminate. This is likely
             * due to daemon threads that are spawned when the sequencer is
             * opened but keep on running even after the sequencer is killed. In
             * this case, you need to explicitly exit the program with
             * System.exit(0).
             */
            // System.exit(0);

        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

	}
	
	@Test
	public void testPlaySample2() {
		
	}
}
