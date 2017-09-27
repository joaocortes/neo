package org.genesys.ml;

import java.util.List;

import org.genesys.models.Trio;

public interface XFeaturizer<T> {
	// (function n-gram, list values)
	public Trio<List<Integer>,List<Integer>,List<Integer>> getFeatures(T input, T output, List<String> ancestors);
}
