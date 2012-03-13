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
            
            player.addNote(new Pitch('G').toMidiNote(), 32*3 + 12, 32*3 + 16);
            player.addNote(new Pitch('G').toMidiNote(), 32*3 + 16, 32*3 + 20);
            player.addNote(new Pitch('G').toMidiNote(), 32*3 + 20, 32*3 + 24);
            
            player.addNote(new Pitch('E').toMidiNote(), 32*3 + 24, 32*3 + 28);
            player.addNote(new Pitch('E').toMidiNote(), 32*3 + 28, 32*3 + 32);
            player.addNote(new Pitch('E').toMidiNote(), 32*3 + 32, 32*3 + 36);
            
            player.addNote(new Pitch('C').toMidiNote(), 32*3 + 36, 32*3 + 40);
            player.addNote(new Pitch('C').toMidiNote(), 32*3 + 40, 32*3 + 44);
            player.addNote(new Pitch('C').toMidiNote(), 32*3 + 44, 32*3 + 48);
            
            player.addNote(new Pitch('G').toMidiNote(), 32*3 + 48, 32*3 + 48 + 9);
            player.addNote(new Pitch('F').toMidiNote(), 32*3 + 48 + 9, 32*3 + 48 + 9 + 3);
            player.addNote(new Pitch('E').toMidiNote(), 32*3 + 60, 32*3 + 69);
            player.addNote(new Pitch('D').toMidiNote(), 32*3 + 69, 32*3 + 72);
            player.addNote(new Pitch('C').toMidiNote(), 32*3 + 72, 32*3 + 72 + 24);

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
