package main;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {
	PARENT,
	CHILD,
	EQUIV;
}
