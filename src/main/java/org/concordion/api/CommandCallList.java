package org.concordion.api;

import java.util.ArrayList;
import java.util.List;

import org.concordion.internal.command.TestCommand;


public class CommandCallList {

    private List<CommandCall> commandCalls = new ArrayList<CommandCall>();
    
    public boolean isEmpty() {
        return commandCalls.isEmpty();
    }

    public void setUp(Evaluator evaluator, ResultRecorder resultRecorder) {
        for(CommandCall call : commandCalls) call.setUp(evaluator, resultRecorder);
    }
    
    public void execute(Evaluator evaluator, ResultRecorder resultRecorder) {
        for(CommandCall call : commandCalls) call.execute(evaluator, resultRecorder);
    }

    public void verify(Evaluator evaluator, ResultRecorder resultRecorder) {
        for(CommandCall call : commandCalls) {
        	if(call.getCommand() instanceof TestCommand) {
        		((TestCommand)call.getCommand()).verifyAll(commandCalls, evaluator, resultRecorder);
        	}
        	System.out.println("VERIFY: " + call.getExpression());
        	call.verify(evaluator, resultRecorder);
        }
    }

    public void processSequentially(Evaluator evaluator, ResultRecorder resultRecorder) {
        for(CommandCall call : commandCalls) {
        	System.out.println("SEQUENTIALLY");
            call.setUp(evaluator, resultRecorder);
            call.execute(evaluator, resultRecorder);
            call.verify(evaluator, resultRecorder);
        }
    }
    
    public void append(CommandCall commandCall) {
        commandCalls.add(commandCall);
    }

    public int size() {
        return commandCalls.size();
    }
    
    public CommandCall get(int index) {
        return commandCalls.get(index);
    }
}
