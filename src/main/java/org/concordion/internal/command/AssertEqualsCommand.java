package org.concordion.internal.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.CommandCallList;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertEqualsListener;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.internal.BrowserStyleWhitespaceComparator;
import org.concordion.internal.util.Announcer;
import org.concordion.internal.util.Check;

public class AssertEqualsCommand extends AbstractCommand {

    private Announcer<AssertEqualsListener> listeners = Announcer.to(AssertEqualsListener.class);
    private final Comparator<Object> comparator;

    public AssertEqualsCommand() {
        this(new BrowserStyleWhitespaceComparator());
    }
    
    public AssertEqualsCommand(Comparator<Object> comparator) {
        this.comparator = comparator;
    }
    
    public void addAssertEqualsListener(AssertEqualsListener listener) {
        listeners.addListener(listener);
    }

    public void removeAssertEqualsListener(AssertEqualsListener listener) {
        listeners.removeListener(listener);
    }
    
    public void verifyList(CommandCallList commandCallList, Evaluator evaluator, ResultRecorder resultRecorder) {
    	for(int i= 0; i< commandCallList.size(); i++) {
        	System.out.println("Expessions: " + commandCallList.get(i).getExpression());
        	System.out.println("Expessions: " + commandCallList.get(i).getExpression());
        	Object actual = evaluator.evaluate(commandCallList.get(i).getExpression());
        	Element element = commandCallList.get(i).getElement();
            String expected = element.getText();
            if (comparator.compare(actual, expected) == 0) {
            	
            	//System.out.println("ACTUAL: " + );
            	System.out.println("EXPECTED TRUE: " + expected);
            	System.out.println("EXPECTED TRUE: " + element.getText());
            	break;
            }
        	
        }
    }
    
    @Override
    public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        Check.isFalse(commandCall.hasChildCommands(), "Nesting commands inside an 'assertEquals' is not supported");
        
        Element element = commandCall.getElement();
        
        // ISG
        CommandCallList list = commandCall.getChildren();
        
        for(int i= 0; i< list.size(); i++) {
        	System.out.println("Expessions: " + list.get(i).getExpression());
        	System.out.println("Expessions: " + list.get(i).getExpression());
        	Object actual = evaluator.evaluate(list.get(i).getExpression());
            String expected = element.getText();
            if (comparator.compare(actual, expected) == 0) {
            	
            	//System.out.println("ACTUAL: " + );
            	System.out.println("EXPECTED: " + expected);
            	break;
            }
        	
        }
        
        // ISG
        
        Object actual = evaluator.evaluate(commandCall.getExpression());
        String expected = element.getText();

        
        if (comparator.compare(actual, expected) == 0) {
            resultRecorder.record(Result.SUCCESS);
            announceSuccess(element);
        } else {
            resultRecorder.record(Result.FAILURE);
            announceFailure(element, expected, actual);
        }
    }
    
    private void announceSuccess(Element element) {
        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    private void announceFailure(Element element, String expected, Object actual) {
        listeners.announce().failureReported(new AssertFailureEvent(element, expected, actual));
    }
}
