package com.soul.fin.common.application.policy;

import java.util.List;

public record PolicyEvaluationResult(
        boolean allowed,
        List<PolicyTrace> traces
) {
}

