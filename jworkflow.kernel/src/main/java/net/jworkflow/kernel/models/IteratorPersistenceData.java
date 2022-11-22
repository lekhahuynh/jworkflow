package net.jworkflow.kernel.models;

public class IteratorPersistenceData extends ControlStepData {

	public int index = 0;

	public IteratorPersistenceData(boolean childrenActive) {
		super(childrenActive);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
