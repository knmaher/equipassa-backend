package com.equipassa.equipassa.pipeline;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;
import java.util.Objects;

public class Pipeline<C, H extends PipelineHandler<C>> {
    private final List<H> handlers;

    public Pipeline(final List<H> handlers) {
        this.handlers = handlers.stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .toList();
    }

    public C run(C context) {
        Objects.requireNonNull(context, "context must not be null");

        C current = context;
        for (H h : handlers) {
            current = h.handle(current);
        }
        return current;
    }
}
