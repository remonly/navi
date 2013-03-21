package edu.uw.cs.lil.navi.experiments.plat.resources;

import edu.uw.cs.lil.navi.data.Trace;
import edu.uw.cs.lil.navi.eval.Task;
import edu.uw.cs.lil.navi.exec.NaviSeqExecutor;
import edu.uw.cs.lil.navi.experiments.plat.NaviExperiment;
import edu.uw.cs.lil.navi.parse.NaviParser;
import edu.uw.cs.lil.tiny.data.sentence.Sentence;
import edu.uw.cs.lil.tiny.explat.IResourceRepository;
import edu.uw.cs.lil.tiny.explat.ParameterizedExperiment.Parameters;
import edu.uw.cs.lil.tiny.explat.resources.IResourceObjectCreator;
import edu.uw.cs.lil.tiny.explat.resources.usage.ResourceUsage;
import edu.uw.cs.lil.tiny.mr.lambda.LogicalExpression;
import edu.uw.cs.lil.tiny.parser.joint.model.JointModel;

public class NaviSeqExecutorCreator implements
		IResourceObjectCreator<NaviSeqExecutor> {
	
	@SuppressWarnings("unchecked")
	@Override
	public NaviSeqExecutor create(Parameters params, IResourceRepository repo) {
		return new NaviSeqExecutor(
				(NaviParser) repo.getResource(NaviExperiment.PARSER_RESOURCE),
				(JointModel<Sentence, Task, LogicalExpression, Trace>) repo
						.getResource(params.get("model")),
				Integer.valueOf(params.get("beam")), "true".equals(params
						.get("recover")), "true".equals(params
						.get("pruneFails")));
	}
	
	@Override
	public String type() {
		return "exec.set";
	}
	
	@Override
	public ResourceUsage usage() {
		return new ResourceUsage.Builder(type(), NaviSeqExecutor.class)
				.setDescription(
						"Executor for sequences of instructions with beam search.")
				.addParam("model", "id", "Joint model to use for inference")
				.addParam("beam", "int", "Beam for executing instructions.")
				.addParam("pruneFails", "boolean",
						"Consider failed execution as incomplete parses. Default: false.")
				.addParam("recover", "boolean",
						"Recover from inference failures by skipping instructions. Default: false.")
				.build();
	}
	
}