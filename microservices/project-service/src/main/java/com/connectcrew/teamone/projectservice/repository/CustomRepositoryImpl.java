package com.connectcrew.teamone.projectservice.repository;

import com.connectcrew.teamone.api.project.values.*;
import com.connectcrew.teamone.projectservice.entity.ProjectCustomEntity;
import com.connectcrew.teamone.projectservice.entity.ProjectCustomFindOption;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomRepositoryImpl implements CustomRepository {
    private final DatabaseClient dc;

    public CustomRepositoryImpl(DatabaseClient dc) {
        this.dc = dc;
    }

    @Override
    public Flux<ProjectCustomEntity> findAllByOption(ProjectCustomFindOption option) {
        /* 예시 SQL
        SELECT p.*,
               GROUP_CONCAT(DISTINCT cat.name ORDER BY cat.name ASC) AS categories
        FROM project p
                 LEFT JOIN part pt ON p.id = pt.project
                 LEFT JOIN category cat ON p.id = cat.project
                 LEFT JOIN skill sk ON p.id = sk.project
        WHERE p.id <= 10
          # 목표
          AND p.goal = 'PORTFOLIO'
          # 경력
          AND p.career_min <= 5
          AND p.career_max >= 5
          # 지역
          AND p.region IN ('SEOUL', 'INCHEON')
          # 온라인
          AND p.with_online = 1
          # 직무
          AND pt.part = 'BACKEND' # or pt.part_category = 'DEVELOP'
          # 상태
          AND p.state IN ('RECRUITING')
          # 카테고리
          AND p.id IN (SELECT project
                       FROM category
                       WHERE name IN ('HOUSE', 'APP'))
          # 기술
          AND p.id IN (SELECT project
                       FROM skill
                       WHERE name IN ('Kotlin', 'Spring')
                       GROUP BY project
                       HAVING COUNT(DISTINCT name) =
                              (SELECT COUNT(DISTINCT name) FROM skill WHERE name IN ('Kotlin', 'Spring')))

        GROUP BY p.id
        ORDER BY p.id DESC
        LIMIT 10;
         */
        List<String> optionSql = new ArrayList<>();
        if (option.lastId() != null && option.lastId() >= 0) optionSql.add(String.format("p.id < %d", option.lastId()));
        if (option.goal() != null) optionSql.add(String.format("p.goal = '%s'", option.goal().name()));
        if (option.career() != null)
            optionSql.add(String.format("p.career_min <= %d AND p.career_max >= %d", option.career().getId(), option.career().getId()));
        if (option.region() != null && option.region().size() > 0)
            optionSql.add(String.format("p.region IN (%s)", String.join(",", option.region().stream().map(r -> String.format("'%s'", r.name())).toList())));
        if (option.online() != null) optionSql.add(String.format("p.with_online = %d", option.online() ? 1 : 0));

        if (option.part() != null) {
            if (option.part().name().startsWith("TOTAL_")) {
                optionSql.add(String.format("p.part_category = '%s'", option.part().getCategory().name()));
            } else {
                optionSql.add(String.format("p.part = '%s'", option.part().name()));
            }

        }
        if (option.states() != null && option.states().size() > 0)
            optionSql.add(String.format("p.state IN (%s)", String.join(",", option.states().stream().map(s -> String.format("'%s'", s.name())).toList())));
        if (option.skills() != null && option.skills().size() > 0) {
            String skills = String.join(",", option.skills().stream().map(s -> String.format("'%s'", s.name())).toList());
            optionSql.add(String.format("p.id IN (SELECT project FROM skill WHERE name IN (%s) GROUP BY project HAVING COUNT(DISTINCT name) = (SELECT COUNT(DISTINCT name) FROM skill WHERE name IN (%s)))", skills, skills));
        }
        if (option.category() != null && option.category().size() > 0)
            optionSql.add(String.format("p.id IN (SELECT project FROM category WHERE name IN (%s))", String.join(",", option.category().stream().map(c -> String.format("'%s'", c.name())).toList())));

        String whereSql = optionSql.size() > 0 ? "WHERE " + String.join(" AND ", optionSql) : "";

        String sql = String.format(
                "SELECT p.*," +
                        "GROUP_CONCAT(DISTINCT cat.name ORDER BY cat.name ASC) AS categories " +
                        "FROM project p " +
                        "LEFT JOIN part pt ON p.id = pt.project " +
                        "LEFT JOIN category cat ON p.id = cat.project " +
                        "LEFT JOIN skill sk ON p.id = sk.project " +
                        "%s " +
                        "GROUP BY p.id " +
                        "ORDER BY p.id DESC " +
                        "LIMIT %d",
                whereSql,
                option.size()
        );

        return dc.sql(sql)
                .map((row, meta) -> {
                    Long id = row.get("id", Long.class);
                    String title = row.get("title", String.class);
                    Career careerMin = Career.valueOf(row.get("career_min", Integer.class));
                    Career careerMax = Career.valueOf(row.get("career_max", Integer.class));
                    Integer withOnlineInt = row.get("with_online", Integer.class);
                    Boolean withOnline = withOnlineInt != null && withOnlineInt == 1;
                    Region region = Region.valueOf(row.get("region", String.class));
                    LocalDateTime createdAt = row.get("created_at", LocalDateTime.class);
                    ProjectState state = ProjectState.valueOf(row.get("state", String.class));
                    ProjectGoal goal = ProjectGoal.valueOf(row.get("goal", String.class));
                    Integer favorite = row.get("favorite", Integer.class);
                    String categoriesStr = row.get("categories", String.class);
                    List<ProjectCategory> categories = new ArrayList<>();
                    if (categoriesStr != null) {
                        categories = Arrays.stream(categoriesStr.split(","))
                                .map(ProjectCategory::valueOf)
                                .toList();
                    }

                    return new ProjectCustomEntity(id, title, region, withOnline, careerMin, careerMax, createdAt, state, favorite, categories, goal);
                })
                .all();
    }
}
