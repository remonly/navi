package edu.uw.cs.lil.navi.experiments.plat.resources;

import edu.uw.cs.lil.navi.experiments.plat.NaviExperiment;
import edu.uw.cs.lil.navi.features.init.ReptFeaturesInit;
import edu.uw.cs.lil.tiny.explat.IResourceRepository;
import edu.uw.cs.lil.tiny.explat.ParameterizedExperiment.Parameters;
import edu.uw.cs.lil.tiny.explat.resources.IResourceObjectCreator;
import edu.uw.cs.lil.tiny.explat.resources.usage.ResourceUsage;
import edu.uw.cs.lil.tiny.mr.lambda.Ontology;

public class ReptFeaturesInitCreator<X> implements
		IResourceObjectCreator<ReptFeaturesInit<X>> {
	
	@Override
	public ReptFeaturesInit<X> create(Parameters params,
			IResourceRepository repo) {
		return new ReptFeaturesInit<X>(params.get("tag"), params.get("name"),
				Double.valueOf(params.get("weight")),
				(Ontology) repo.getResource(NaviExperiment.ONTOLOGY_RESOURCE));
	}
	
	@Override
	public String type() {
		return "init.feats.rept";
	}
	
	@Override
	public ResourceUsage usage() {
		return new ResourceUsage.Builder(type(), ReptFeaturesInit.class)
				.setDescription(
						"Intializer for logical expression repetition features")
				.addParam("tag", "string", "Repetition features tag")
				.addParam("name", "string",
						"Repetition features second identifier")
				.addParam("weight", "double",
						"Initial weight to give to each initialized feature")
				.build();
	}
	
}
