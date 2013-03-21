package edu.uw.cs.lil.navi.experiments.plat.resources;

import edu.uw.cs.lil.navi.data.Trace;
import edu.uw.cs.lil.navi.eval.NaviEvaluationServicesFactory;
import edu.uw.cs.lil.navi.eval.Task;
import edu.uw.cs.lil.navi.experiments.plat.NaviExperiment;
import edu.uw.cs.lil.navi.learn.lexicalgen.NaviJointTemplatedAbstractLexiconGenerator;
import edu.uw.cs.lil.tiny.data.sentence.Sentence;
import edu.uw.cs.lil.tiny.data.utils.IValidator;
import edu.uw.cs.lil.tiny.explat.IResourceRepository;
import edu.uw.cs.lil.tiny.explat.ParameterizedExperiment.Parameters;
import edu.uw.cs.lil.tiny.explat.resources.IResourceObjectCreator;
import edu.uw.cs.lil.tiny.explat.resources.usage.ResourceUsage;
import edu.uw.cs.lil.tiny.mr.lambda.LogicalExpression;
import edu.uw.cs.lil.tiny.mr.lambda.Ontology;
import edu.uw.cs.lil.tiny.parser.IParser;
import edu.uw.cs.lil.tiny.parser.ccg.joint.genlex.JointTemplatedAbstractLexiconGenerator;
import edu.uw.cs.lil.tiny.parser.ccg.joint.genlex.JointTemplatedAbstractLexiconGenerator.Builder;
import edu.uw.cs.lil.tiny.parser.ccg.model.IModelImmutable;
import edu.uw.cs.lil.tiny.parser.joint.IJointParser;
import edu.uw.cs.lil.tiny.parser.joint.model.IJointModelImmutable;
import edu.uw.cs.utils.composites.Pair;

public class NaviJointTemplatedAbstractLexiconGeneratorCreator
		implements
		IResourceObjectCreator<JointTemplatedAbstractLexiconGenerator<Task, Trace, Trace>> {
	
	@SuppressWarnings("unchecked")
	@Override
	public JointTemplatedAbstractLexiconGenerator<Task, Trace, Trace> create(
			Parameters parameters, IResourceRepository resourceRepo) {
		final Builder<Task, Trace, Trace> builder = new NaviJointTemplatedAbstractLexiconGenerator.Builder(
				Integer.valueOf(parameters.get("maxTokens")),
				(IJointModelImmutable<Sentence, Task, LogicalExpression, Trace>) resourceRepo
						.getResource(parameters.get("model")),
				(IParser<Sentence, LogicalExpression>) resourceRepo
						.getResource(NaviExperiment.BASE_PARSER_RESOURCE),
				Integer.valueOf(parameters.get("beam")),
				((NaviEvaluationServicesFactory) resourceRepo
						.getResource(NaviExperiment.EVAL_SERVICES_FACTORY))
						.getNaviEvaluationConsts(),
				(IJointParser<Sentence, Task, LogicalExpression, Trace, Trace>) resourceRepo
						.getResource(NaviExperiment.PARSER_RESOURCE),
				(IValidator<Pair<Sentence, Task>, Pair<LogicalExpression, Trace>>) resourceRepo
						.getResource(parameters.get("validator")))
				.addTemplatesFromModel(
						(IModelImmutable<?, LogicalExpression>) resourceRepo
								.getResource(parameters.get("model")))
				.addConstants(
						(Ontology) resourceRepo
								.getResource(NaviExperiment.DOMAIN_ONTOLOGY_RESOURCE));
		
		if (parameters.contains("margin")) {
			builder.setMargin(Double.valueOf(parameters.get("margin")));
		}
		
		return builder.build();
	}
	
	@Override
	public String type() {
		return "genlex.templated.abstract.joint.navi";
	}
	
	@Override
	public ResourceUsage usage() {
		return new ResourceUsage.Builder(type(),
				JointTemplatedAbstractLexiconGenerator.class)
				.setDescription(
						"Lexical generator that uses abstract constants for coarse-to-fine pruning of lexical entries and joint inference for choosing a threshold model score for parses")
				.addParam("maxTokens", "int",
						"Max number of tokens to include in lexical entries")
				.addParam("model", "id",
						"Joint model to use for inference and obtain the set of templates")
				.addParam("validator", "id",
						"Validation function to select valid parses")
				.addParam(
						"margin",
						"double",
						"Parses that resepect this margin against the top valid current parses, are pruned during generation. Default: 0.0.")
				.addParam("beam", "int", "Beam to use for inference").build();
	}
	
}
