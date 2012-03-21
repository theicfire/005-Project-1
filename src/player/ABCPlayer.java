package player;

import javax.sound.midi.MidiUnavailableException;

import lexer.Lexer;

public class ABCPlayer {
	
	
	public void play(String filename) {
		Lexer l = new Lexer(filename);
		ABCEnvironment env = ABCParser.parse(l.tokens);
		Scheduler s = new Scheduler(env.tempo, env.ticksPerDefaultNote);
		Schedulable root = env.getTreeRoot();
		root.accept(s);
		try {
			s.PLAYER.play();
		} catch (MidiUnavailableException e) {
            e.printStackTrace();
		}
	}

}
