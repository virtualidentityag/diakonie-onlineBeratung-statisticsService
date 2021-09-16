package de.caritas.cob.statisticsservice.api.statistics.repository;

import de.caritas.cob.statisticsservice.api.statistics.model.StatisticsEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsEventRepository extends MongoRepository<StatisticsEvent, String> {

}
