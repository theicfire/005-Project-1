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
	@Test
	public void testAminor() {
		KeySignature Am = new KeySignature("Am");
		Pitch A = new Pitch('A');
		Pitch B = new Pitch('B');
		Pitch C = new Pitch('C');
		Pitch D = new Pitch('D');
		Pitch E = new Pitch('E');
		Pitch F = new Pitch('F');
		Pitch G = new Pitch('G');
		assertEquals(A, Am.getPitch('A', 0));
		assertEquals(B, Am.getPitch('B', 0));
		assertEquals(C, Am.getPitch('C', 0));
		assertEquals(D, Am.getPitch('D', 0));
		assertEquals(E, Am.getPitch('E', 0));
		assertEquals(F, Am.getPitch('F', 0));
		assertEquals(G, Am.getPitch('G', 0));
		KeySignature AmFlats = Am.fromAccidental('A', -1, 0);
		AmFlats = AmFlats.fromAccidental('B', -1, 0);
		AmFlats = AmFlats.fromAccidental('C', -1, 0);
		AmFlats = AmFlats.fromAccidental('D', -1, 0);
		AmFlats = AmFlats.fromAccidental('E', -1, 0);
		AmFlats = AmFlats.fromAccidental('F', -1, 0);
		AmFlats = AmFlats.fromAccidental('G', -1, 0);
		assertEquals(A.accidentalTranspose(-1), AmFlats.getPitch('A', 0));
		assertEquals(B.accidentalTranspose(-1), AmFlats.getPitch('B', 0));
		assertEquals(C.accidentalTranspose(-1), AmFlats.getPitch('C', 0));
		assertEquals(D.accidentalTranspose(-1), AmFlats.getPitch('D', 0));
		assertEquals(E.accidentalTranspose(-1), AmFlats.getPitch('E', 0));
		assertEquals(F.accidentalTranspose(-1), AmFlats.getPitch('F', 0));
		assertEquals(G.accidentalTranspose(-1), AmFlats.getPitch('G', 0));
		KeySignature AmSharps = Am.fromAccidental('A', 1, 0);
		AmSharps = AmSharps.fromAccidental('B', 1, 0);
		AmSharps = AmSharps.fromAccidental('C', 1, 0);
		AmSharps = AmSharps.fromAccidental('D', 1, 0);
		AmSharps = AmSharps.fromAccidental('E', 1, 0);
		AmSharps = AmSharps.fromAccidental('F', 1, 0);
		AmSharps = AmSharps.fromAccidental('G', 1, 0);
		assertEquals(A.accidentalTranspose(1), AmSharps.getPitch('A', 0));
		assertEquals(B.accidentalTranspose(1), AmSharps.getPitch('B', 0));
		assertEquals(C.accidentalTranspose(1), AmSharps.getPitch('C', 0));
		assertEquals(D.accidentalTranspose(1), AmSharps.getPitch('D', 0));
		assertEquals(E.accidentalTranspose(1), AmSharps.getPitch('E', 0));
		assertEquals(F.accidentalTranspose(1), AmSharps.getPitch('F', 0));
		assertEquals(G.accidentalTranspose(1), AmSharps.getPitch('G', 0));
	}
	

}
