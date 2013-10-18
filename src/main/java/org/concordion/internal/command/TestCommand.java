package org.concordion.internal.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.CommandCallList;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.api.listener.TestCommandListener;
import org.concordion.internal.BrowserStyleWhitespaceComparator;
import org.concordion.internal.util.Announcer;
import org.concordion.internal.util.Check;

public class TestCommand extends AbstractCommand {
	
	private List<Element> elements = new ArrayList<Element>();
	private List<Evaluator> evaluators = new ArrayList<Evaluator>();
	private List<ResultRecorder> resultRecorders = new ArrayList<ResultRecorder>();
	
	private List<CommandCall> commandCalls = new ArrayList<CommandCall>();
	
	private Announcer<TestCommandListener> listeners = Announcer.to(TestCommandListener.class);
	
    private final Comparator<Object> comparator;

    public TestCommand() {
        this(new BrowserStyleWhitespaceComparator());
    }
    
    public TestCommand(Comparator<Object> comparator) {
        this.comparator = comparator;
    }

    public void addTestCommandListener(TestCommandListener listener) {
        listeners.addListener(listener);
    }

    public void removeTestCommandListener(TestCommandListener listener) {
        listeners.removeListener(listener);
    }
	
	 //@SuppressWarnings("unchecked")
	 //@Override
	 public void verifyAll(List<CommandCall> commandCallList, Evaluator evaluator, ResultRecorder resultRecorder) {
	 
		 System.out.println("TEST COMMAND ALL");
		 
	 }
	 
	 @Override
	 public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
	 
		 Check.isFalse(commandCall.hasChildCommands(), "Nesting commands inside an 'assertEquals' is not supported");
	     
		 addChild(commandCall, evaluator, resultRecorder);
         //Element element = commandCall.getElement();
        
         // ISG
         CommandCallList list = commandCall.getChildren();
         System.out.println("TEST COMMAND");
         System.out.println("SIZE: " + list.size());
         //System.out.println("ELEMENT: " + element.getText());
         
         for(int i= 0; i< commandCalls.size(); i++) {
        	 
        	 CommandCall command = commandCalls.get(i);
        	 Element element = command.getElement();
        	 for (int j=0; j<evaluators.size(); j++) {
        		 Object actual = evaluators.get(j).evaluate(command.getExpression());
                 String expected = element.getText();
                 
                 if (comparator.compare(actual, expected) == 0) {
                	 resultRecorders.get(j).record(Result.SUCCESS);
                     announceSuccess(element);
                     resultRecorders.remove(j);
                     evaluators.remove(j);
                     commandCalls.remove(i);
                 }
//                 } else {
//                	 resultRecorders.get(j).record(Result.FAILURE);
//                     announceFailure(element, expected, actual);
//                 }
        	 }
//        	 Object actual = evaluator.evaluate(commandCall.getExpression());
//             String expected = element.getText();
//
//             
//             
         
         }
		 
	 }
	 
	 private void addChild(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
		 commandCalls.add(commandCall);
		 evaluators.add(evaluator);
		 resultRecorders.add(resultRecorder);
	 }
	 
	 private void announceSuccess(Element element) {
	     listeners.announce().successReported(new AssertSuccessEvent(element));
     }

     private void announceFailure(Element element, String expected, Object actual) {
         listeners.announce().failureReported(new AssertFailureEvent(element, expected, actual));
     }

}
