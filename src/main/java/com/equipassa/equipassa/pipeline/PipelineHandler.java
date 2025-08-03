package com.equipassa.equipassa.pipeline;

/**
 * Generic step in a processing pipeline.
 * @param <C>  a mutable context object that carries everything
 *             the chain needs (request + domain + side-effects).
 */
public interface PipelineHandler<C> {
    C handle(C context);
}
