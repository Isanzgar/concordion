package org.concordion.api;

import java.util.List;


public interface Command {

    void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder);

    void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder);

    void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder);
    
    //void abstract verifyAll(List<CommandCall> commandCallList, Evaluator evaluator, ResultRecorder resultRecorder);
}
