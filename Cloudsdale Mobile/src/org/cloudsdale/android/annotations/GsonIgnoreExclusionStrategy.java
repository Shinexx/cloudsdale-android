package org.cloudsdale.android.annotations;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class GsonIgnoreExclusionStrategy implements ExclusionStrategy {
	private final Class<?> typeToSkip;
	
	public GsonIgnoreExclusionStrategy(Class<?> typeToSkip) {
		this.typeToSkip = typeToSkip;
	}
	
	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(GsonIgnore.class) != null;
	}

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		return (clazz == typeToSkip);
	}

}