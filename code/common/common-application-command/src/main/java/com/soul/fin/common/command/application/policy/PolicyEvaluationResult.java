package com.soul.fin.common.command.application.policy;

import java.util.List;

public record PolicyEvaluationResult(
        boolean allowed,
        List<PolicyTrace> traces
) {
}

