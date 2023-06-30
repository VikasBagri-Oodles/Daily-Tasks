package com.vehicle.routing.problem.solver;

import com.vehicle.routing.problem.domain.Vehicle;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class VehicleRoutingConstraintProvider implements ConstraintProvider {

    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {

        return new Constraint[] {

                // only HARD constraints
//                notAllLocations(constraintFactory),
                notMinimumDistance(constraintFactory)

                // no SOFT constraints

        };

    }

    public Constraint notAllLocations(ConstraintFactory constraintFactory) {

        return constraintFactory.
                forEach(Vehicle.class)
                .filter((vehicle) -> vehicle.getRoute().size() != vehicle.getLocationCount())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Not All Locations");

    }

    public Constraint notMinimumDistance(ConstraintFactory constraintFactory) {

        return constraintFactory
                .forEach(Vehicle.class)
                .filter((vehicle) -> vehicle.getRouteDistance() > vehicle.getMinDistance())
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Not Minimum Distance");

    }



}
