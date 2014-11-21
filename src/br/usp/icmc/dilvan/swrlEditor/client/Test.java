package br.usp.icmc.dilvan.swrlEditor.client;

class Test {}
//
//	public static void main(String[] args) throws SerializationException {
//
//		final Object obj = new Test2();
//		String buffer = Streamer.get().toString( obj );
//
//		System.out.println(buffer);
//		//...
//		Test2 person = (Test2) Streamer.get().fromString( buffer );
//
//		System.out.println(person);
//
//		person = (Test2) Streamer.get().fromString("C42_br.usp.icmc.dilvan.swrlEditor.client.Test2I1_B5_ggvggI1_B6_yuyggt");
//
//		System.out.println(person);
//		//		final Object obj = new Test2();
//		//	    final HostedModeClientOracle hmco =new HostedModeClientOracle();
//		//	    final HasValues command = new ReturnCommand();
//		//	    final HasValuesCommandSink hvcs = new HasValuesCommandSink(command);
//		//	    final CommandServerSerializationStreamWriter out = new CommandServerSerializationStreamWriter(hmco, hvcs);
//		//	    try {
//		//	        out.writeObject(obj);
//		//	    } catch (Exception e) {
//		//	        e.printStackTrace(System.err);
//		//	        System.out.println("Object couldn't be serialized:" + e.getMessage());
//		//	    }
//		//	    out.
//
//
//		//		//HostedModeClientOracle hmco =new HostedModeClientOracle();
//		//		HasValues command = new ReturnCommand();
//		//		HasValuesCommandSink hvcs = new HasValuesCommandSink(command);
//		//		CommandServerSerializationStreamWriter out = new CommandServerSerializationStreamWriter(hvcs);
//		//
//		//		Test2 tst = new Test2();
//		//
//		//		out.writeObject(tst);
//		//
//		//		System.out.println("out "+command.getValues().get(0).toString());
//		//
//		//		CommandServerSerializationStreamReader in = new CommandServerSerializationStreamReader();
//		//		InstantiateCommand ic = new InstantiateCommand(RuleSet.class);
//		//		List<ValueCommand> lst = new ArrayList<ValueCommand>();
//		//		lst.add(ic);
//		//		in.prepareToRead(lst);
//		//		//		in.
//	}
//
//}
//
//@SuppressWarnings("serial")
//class Test2 implements Serializable, Streamable {
//	public String x1 = "lllll";
//	public String y2 = "kkkkkk";
//
//	@Override
//	public String toString() {
//		return x1+"----"+y2;
//	}
//}
