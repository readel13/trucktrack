package edu.trucktrack.repository.jooq;

import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.jooq.tables.EmployeeExpensesTag;
import edu.trucktrack.jooq.tables.Tag;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.field;

@Repository
@RequiredArgsConstructor
public class TagJooqRepository {

    private final DSLContext ctx;

    public Map<Integer, List<TagDTO>> getForExpenses(Set<Long> expensesIds) {
        var tag = new Tag(Names.TAG);
        var eet = new EmployeeExpensesTag(Names.EET);


        return ctx.select(
                        tag.ID.as(Names.ID),
                        eet.EXPENSE_ID.as(Names.EXPENSE_ID),
                        tag.NAME.as(Names.NAME)
                )
                .from(tag)
                .join(eet).on(eet.TAG_ID.eq(tag.ID.cast(Integer.class)))
                .where(eet.EXPENSE_ID.in(expensesIds))
                .collect(
                        Collectors.groupingBy(
                                r -> r.get(field(Names.EXPENSE_ID, Integer.class)),
                                Collectors.mapping(r -> r.into(TagDTO.class), Collectors.toCollection(ArrayList::new))
                        )
                );
    }
}
