package com.soul.fin.common.application.policy;

public interface Policy<I> {

    PolicyDecision evaluate(PolicyContext<I> context);

    PolicyMetadata metadata();
}


