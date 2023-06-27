package com.school.timetabling.solver;

import com.school.timetabling.domain.Lesson;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;

public class TimeTableConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {

                // HARD CONSTRAINTS
                roomConflict(constraintFactory),
                teacherConflict(constraintFactory),
                studentConflict(constraintFactory),

                // SOFT CONSTRAINTS

        };
    }

    private Constraint roomConflict(ConstraintFactory constraintFactory) {

        // One room can accommodate at most one lesson at the same time
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getRoom),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Room Conflict");

    }

    private Constraint teacherConflict(ConstraintFactory constraintFactory) {

        // A teacher can teach at most one lesson at the same time
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getTeacher),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher Conflict");

    }

    private Constraint studentConflict(ConstraintFactory constraintFactory) {

        // A student can attend at most one lesson at the same time
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.lessThan(Lesson::getId))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student Conflict");

    }

    private Constraint teacherRoomStability(ConstraintFactory constraintFactory) {

        // A teacher prefers to teach in a single room
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                Joiners.equal(Lesson::getTeacher))
                .filter((lesson1, lesson2) -> lesson1.getRoom() != lesson2.getRoom())
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher Room Stability");

    }

    private Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) {

        // A teacher prefers to teach sequential lessons and dislikes gaps between lessons
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getTeacher),
                        Joiners.equal(lesson -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher Time Efficiency");

    }


}
