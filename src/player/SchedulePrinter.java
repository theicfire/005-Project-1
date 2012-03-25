package player;



public class SchedulePrinter implements SchedulableVisitor {

	
	int depth = 0;
	StringBuilder outString = new StringBuilder();

	@Override
	public void visit(TuneSequence s) {
		
		
		addTabs(outString,depth);
		outString.append("(\n");
		depth++;
		for (Schedulable sp : s) {
			sp.accept(this);
		}
		depth--;
		addTabs(outString,depth);
		outString.append(")\n");
		
	}

	@Override
	public void visit(TuneParallel p) {
		
		addTabs(outString,depth);
		outString.append("[\n");
		depth++;
		for (Schedulable sp : p) {
			sp.accept(this);
		}
		depth--;
		addTabs(outString,depth);
		outString.append("]\n");
		
	}

	@Override
	public void visit(TuneRepeatable r) {
		
		addTabs(outString,depth);
		outString.append("<\n");
		addTabs(outString,depth);
		outString.append("b:\n");
		
		depth++;
		r.body().accept(this);
		depth--;
		
		int i = 1;
		if (r.hasMultipleEndings()) {
			for (Schedulable e : r.endings()) {
				addTabs(outString, depth);
				outString.append(String.format("e%d\n",i));
				depth++;
				e.accept(this);
				depth--;
				i++;
			}
		}
		
		addTabs(outString,depth);
		outString.append(">\n");
		
	}

	@Override
	public void visit(Tuple t) {
		
		addTabs(outString,depth);
		outString.append(String.format("T(%s):\n",t.multiplier.toShortString()));
		
		depth++;
		for (Schedulable s : t) {
			s.accept(this);
		}
		depth--;
		addTabs(outString,depth);
		outString.append(":T\n");		
	}

	@Override
	public void visit(TunePrimitive p) {
		
		addTabs(outString,depth);
		
		if (p.isNote) {
			outString.append(p.midiPitch.toString());
		} else {
			outString.append("z");
		}
		
		outString.append(":");
		outString.append(p.duration.toShortString());
		outString.append("\n");
		
		
	}
	
	
	private static void addTabs(StringBuilder builder,int depth) {
		for (int i=0;i<depth;i++) {
			builder.append("\t");
		}
	}
	
}
