package edu.trucktrack.dao.repository.jooq;

import edu.trucktrack.api.dto.TagDTO;
import edu.trucktrack.dao.jooq.tables.EmployeeExpensesTag;
import edu.trucktrack.dao.jooq.tables.Tag;
import edu.trucktrack.util.Names;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.inline;

@Repository
@RequiredArgsConstructor
public class TagJooqRepository {

    private final DSLContext ctx;

    public List<TagDTO> getAll() {
        var tag = new Tag(Names.TAG);

        return ctx.select(
                        tag.ID.as(Names.ID),
                        inline(0).as(Names.EXPENSE_ID),
                        tag.NAME.as(Names.NAME),
                        tag.IS_SYSTEM.as(Names.IS_SYSTEM),
                        tag.CREATED_BY_EMPLOYEE_ID.as(Names.CREATED_BY_EMPLOYEE_ID),
                        tag.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(tag)
                .fetchInto(TagDTO.class);
    }

    public Map<Integer, Set<TagDTO>> getForExpenses(Set<Long> expensesIds) {
        var tag = new Tag(Names.TAG);
        var eet = new EmployeeExpensesTag(Names.EET);

        return ctx.select(
                        tag.ID.as(Names.ID),
                        eet.EXPENSE_ID.as(Names.EXPENSE_ID),
                        tag.NAME.as(Names.NAME),
                        tag.IS_SYSTEM.as(Names.IS_SYSTEM),
                        tag.CREATED_BY_EMPLOYEE_ID.as(Names.CREATED_BY_EMPLOYEE_ID),
                        tag.CREATED_AT.as(Names.CREATED_AT)
                )
                .from(tag)
                .join(eet).on(eet.TAG_ID.eq(tag.ID.cast(Integer.class)))
                .where(eet.EXPENSE_ID.in(expensesIds))
                .collect(
                        Collectors.groupingBy(
                                r -> r.get(eet.EXPENSE_ID),
                                Collectors.mapping(r -> r.into(TagDTO.class), Collectors.toCollection(LinkedHashSet::new))
                        )
                );
    }
}
