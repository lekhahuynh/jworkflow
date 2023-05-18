package net.jworkflow.primitives;

import java.util.Arrays;
import java.util.List;

import net.jworkflow.kernel.interfaces.StepBody;
import net.jworkflow.kernel.models.ControlStepData;
import net.jworkflow.kernel.models.ExecutionResult;
import net.jworkflow.kernel.models.IteratorPersistenceData;
import net.jworkflow.kernel.models.StepExecutionContext;

public class Foreach implements StepBody {

	public List<Object> collection;
	public boolean isParallel = false;

	@Override
	public ExecutionResult run(StepExecutionContext context) {

		if (context.getPersistenceData() == null) {
			if (collection == null || collection.isEmpty()) {
				return ExecutionResult.next();
			}
			if (isParallel) {
				// Run with Parallel.
				return ExecutionResult.branch(collection, new IteratorPersistenceData(true));
			} else {
				// Run with sync.
				List<Object> syncData = Arrays.asList(collection.get(0));
				return ExecutionResult.branch(syncData, new IteratorPersistenceData(true));
			}
		}

		if (context.getPersistenceData() instanceof ControlStepData) {

			if (context.getPersistenceData() instanceof IteratorPersistenceData) {
				IteratorPersistenceData persistenceData = (IteratorPersistenceData) context.getPersistenceData();

				if (persistenceData.childrenActive) {
					if (context.getWorkflow().isBranchComplete(context.getExecutionPointer().id)) {
						if (!isParallel) {
							persistenceData.index++;
							if (persistenceData.index < collection.size()) {
								List<Object> syncData = Arrays.asList(collection.get(persistenceData.index));
								return ExecutionResult.branch(syncData, persistenceData);
							}
						}
						return ExecutionResult.next();
					}
					return ExecutionResult.persist(persistenceData);
				}
			} else {
				ControlStepData persistenceData = (ControlStepData) context.getPersistenceData();

				if (persistenceData.childrenActive) {
					if (context.getWorkflow().isBranchComplete(context.getExecutionPointer().id))
						return ExecutionResult.next();
				}
			}

		}

		return ExecutionResult.persist(context.getPersistenceData());
	}
}
