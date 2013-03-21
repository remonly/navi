package edu.uw.cs.lil.navi.experiments.plat.resources;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.uw.cs.lil.navi.data.LabeledInstructionSeqTrace;
import edu.uw.cs.lil.navi.data.LabeledInstructionSeqTraceDataset;
import edu.uw.cs.lil.navi.data.LabeledInstructionTrace;
import edu.uw.cs.lil.navi.data.LabeledInstructionTraceDataset;
import edu.uw.cs.lil.navi.eval.Task;
import edu.uw.cs.lil.navi.experiments.plat.NaviExperiment;
import edu.uw.cs.lil.navi.map.NavigationMap;
import edu.uw.cs.lil.tiny.ccg.categories.ICategoryServices;
import edu.uw.cs.lil.tiny.data.sentence.Sentence;
import edu.uw.cs.lil.tiny.explat.IResourceRepository;
import edu.uw.cs.lil.tiny.explat.ParameterizedExperiment.Parameters;
import edu.uw.cs.lil.tiny.explat.resources.IResourceObjectCreator;
import edu.uw.cs.lil.tiny.explat.resources.usage.ResourceUsage;
import edu.uw.cs.lil.tiny.parser.ccg.genlex.ILexiconGenerator;
import edu.uw.cs.lil.tiny.parser.joint.model.JointDataItemWrapper;

public class LabeledInstructionTraceDatasetCreator<Y> implements
		IResourceObjectCreator<LabeledInstructionTraceDataset<Y>> {
	
	@SuppressWarnings("unchecked")
	@Override
	public LabeledInstructionTraceDataset<Y> create(Parameters parameters,
			IResourceRepository repo) {
		if (parameters.contains("sets")) {
			final LabeledInstructionSeqTraceDataset<Y> sets = repo
					.getResource(parameters.get("sets"));
			final List<LabeledInstructionTrace<Y>> items = new LinkedList<LabeledInstructionTrace<Y>>();
			for (final LabeledInstructionSeqTrace<Y> set : sets) {
				for (final LabeledInstructionTrace<Y> lst : set) {
					items.add(lst);
				}
			}
			return new LabeledInstructionTraceDataset<Y>(items);
		} else {
			try {
				return LabeledInstructionTraceDataset
						.readFromFile(
								parameters.getAsFile("file"),
								(Map<String, NavigationMap>) repo
										.getResource(NaviExperiment.MAPS_RESOURCE),
								(ICategoryServices<Y>) repo
										.getResource(NaviExperiment.CATEGORY_SERVICES_RESOURCE),
								(ILexiconGenerator<JointDataItemWrapper<Sentence, Task>, Y>) repo
										.getResource(parameters.get("genlex")));
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public String type() {
		return "data.ccgtrc";
	}
	
	@Override
	public ResourceUsage usage() {
		return new ResourceUsage.Builder(type(),
				LabeledInstructionTraceDataset.class)
				.setDescription(
						"Dataset of single instructions paired with a pair of logical expression and execution trace.")
				.addParam(
						"sets",
						"id",
						"Dataset of labeled instruction sequences to construct a labeled instruction dataset from (may be used instead of file and genlex).")
				.addParam("file", "file", "Dataset file")
				.addParam("genlex", "id", "Lexical generator").build();
	}
	
}
