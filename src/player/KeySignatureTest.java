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
public class KeySignatureTest {
	@Test
	public void keySigTest1() {
		KeySignature Cmaj = new KeySignature("C");
		Pitch middleC = new Pitch('C');
		assertEquals(middleC, Cmaj.getPitch('C', 0));
		Pitch highE = new Pitch('E').octaveTranspose(1);
		assertEquals(highE, Cmaj.getPitch('E', 1));
		assert (!Cmaj.modified);
	}
	@Test
	public void keySigTest2() {
		KeySignature Amaj = new KeySignature("A");
		Pitch middleCsharp = new Pitch('C').accidentalTranspose(1);
		assertEquals(middleCsharp, Amaj.getPitch('C', 0));
		Pitch reallyHighGsharp = new Pitch('G').accidentalTranspose(1).octaveTranspose(2);
		assertEquals(reallyHighGsharp, Amaj.getPitch('G', 2));
		assert (!Amaj.modified);
	}
	@Test
	public void keySigTest3() {
		KeySignature Dflat = new KeySignature("Dbm");
		KeySignature DflatMod = Dflat.fromAccidental('D', "=", 0);
		Pitch dNatural = new Pitch('D');
		Pitch highDflat = new Pitch('D').accidentalTranspose(-1).octaveTranspose(1);
		assert (DflatMod.modified);
		assertEquals(dNatural, DflatMod.getPitch('D', 0));
		assertEquals(highDflat,DflatMod.getPitch('D', 1));
	}
	

}
