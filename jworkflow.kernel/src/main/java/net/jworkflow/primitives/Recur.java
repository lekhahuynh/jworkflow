package net.jworkflow.primitives;

import java.time.Duration;
import java.util.ArrayList;

import net.jworkflow.kernel.interfaces.StepBody;
import net.jworkflow.kernel.models.ExecutionResult;
import net.jworkflow.kernel.models.StepExecutionContext;

public class Recur implements StepBody {
    
    public Duration interval;
    public boolean stopCondition;    
    
    @Override
    public ExecutionResult run(StepExecutionContext context) {
                
        if (stopCondition) {
            return ExecutionResult.next();
        }
        ExecutionResult result = new ExecutionResult();
        result.setProceed(false);
        result.setSleepFor(interval);
        result.setBranches(new ArrayList<>(1));
        
        return result;
    }
}
