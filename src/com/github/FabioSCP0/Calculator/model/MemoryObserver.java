package com.github.FabioSCP0.Calculator.model;
@FunctionalInterface
public interface MemoryObserver {
	void changedValue(String newValue);
}
