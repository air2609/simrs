package com.simrs.backend.audit;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String actionType, String entityType, String entityId, String performedBy, String details) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.setActionType(actionType);
        entity.setEntityType(entityType);
        entity.setEntityId(entityId);
        entity.setPerformedBy(performedBy);
        entity.setDetails(details);
        entity.setCreatedAt(LocalDateTime.now());
        auditLogRepository.save(entity);
    }
}
