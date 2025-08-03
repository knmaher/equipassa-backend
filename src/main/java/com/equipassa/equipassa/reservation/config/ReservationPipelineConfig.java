package com.equipassa.equipassa.reservation.config;

import com.equipassa.equipassa.pipeline.Pipeline;
import com.equipassa.equipassa.reservation.pipeline.ReservationContext;
import com.equipassa.equipassa.reservation.pipeline.ReservationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ReservationPipelineConfig {

    @Bean
    Pipeline<ReservationContext, ReservationHandler> reservationPipeline(List<ReservationHandler> handlers) {
        return new Pipeline<>(handlers);
    }
}
