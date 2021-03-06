/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.components.similarity;

import eu.sisob.components.rwrapper.RWrapperAgent;
import com.google.gson.JsonObject;
import eu.sisob.components.framework.util.ClassLoaderUtility;


/**
 * @author doberstein
 */
public class SimilarityAgent extends RWrapperAgent {

	private static final String SCRIPT1 = "regular_similarity.R";
	private static final String SCRIPT2 = "structural_similarity.R";

	private String method;

	public SimilarityAgent(JsonObject commandMsg) {
		super(commandMsg);
		String selectedScript = null;

		this.method = this.getFilterParameters().get(SimilarityManager.METHOD).toString();

		if (this.method.equals("regular")) {
			selectedScript = SCRIPT1;
		} else if (this.method.equals("structural")) {
			selectedScript = SCRIPT2;
		}
		
		if (selectedScript != null) {
			this.scriptStream = ClassLoaderUtility.getClassLoader().getResourceAsStream(selectedScript);
		} else {
			indicateError("could not find the selected script");
		}
		
	}

}
