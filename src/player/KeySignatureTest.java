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
		
		KeySignature CmajMod1 = Cmaj.fromAccidental('F', 1, 0);
		KeySignature CmajMod2 = CmajMod1.fromAccidental('A', -1, 1);
		Pitch fSharp = new Pitch('F').accidentalTranspose(1);
		Pitch aFlatHigh = new Pitch('A').octaveTranspose(1).accidentalTranspose(-1);
		Pitch lowA = new Pitch('A').octaveTranspose(-1);
		assert (CmajMod2.modified);
		assertEquals(middleC, CmajMod2.getPitch('C', 0));
		assertEquals(fSharp, CmajMod2.getPitch('F', 0));
		assertEquals(aFlatHigh, CmajMod2.getPitch('A', 1));
		assertEquals(lowA, CmajMod2.getPitch('A', -1));
		
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
		KeySignature DflatMod = Dflat.fromAccidental('D', 0, 0);
		Pitch dNatural = new Pitch('D');
		Pitch highDflat = new Pitch('D').accidentalTranspose(-1).octaveTranspose(1);
		assert (DflatMod.modified);
		assertEquals(dNatural, DflatMod.getPitch('D', 0));
		assertEquals(highDflat,DflatMod.getPitch('D', 1));
	}
	@Test
	public void keySigTest4() {
		KeySignature Fb = new KeySignature("Fb");
		KeySignature FbMod = Fb.fromAccidental('D', 0, 0);
		Pitch dNatural = new Pitch('D');
		Pitch highDflat = new Pitch('D').accidentalTranspose(-1).octaveTranspose(1);
		Pitch Bbb = new Pitch('B').accidentalTranspose(-2);
		assert (FbMod.modified);
		assertEquals(Bbb, FbMod.getPitch('B', 0));
		assertEquals(dNatural, FbMod.getPitch('D', 0));
		assertEquals(highDflat,FbMod.getPitch('D', 1));
	}
	

}
